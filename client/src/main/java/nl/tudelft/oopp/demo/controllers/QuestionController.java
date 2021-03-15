package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;

public class QuestionController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    @FXML
    private Text greetings;

    @FXML
    private BarChart pollChart;

    private Poll currentPoll;

    private Users users;

    private LectureRoom lectureRoom;


    @FXML
    private void displayQuestion() {
        if (questionText.getText().isEmpty()) {
            Alerts.alertError("Question is empty",
                    "Please fill out the field before pressing send");
        } else if (questionText.getText().length() > 255) {
            Alerts.alertError("Question too long",
                    "Question too long, can only be 255 characters");
        } else {
            Question q = new Question(questionText.getText(),
                    lectureRoom.getLecturePin(),
                    users.getUsername());

            ServerCommunication.saveQuestion(q);
            questionText.clear();
            displayAllQuestion();
        }
    }

    @FXML
    private void displayAllQuestion() {
        List<Question> qs = ServerCommunication.getAllQuestion(lectureRoom.getLecturePin());
        List<ScoringLog> votes = ServerCommunication.getVotes();

        stack.getChildren().clear();
        stack.setSpacing(20);   // Space between questions.

        // Update the scores for each question.
        for (Question q : qs) {
            for (ScoringLog scoringLog : votes) {
                if (scoringLog.getQuestion().equals(q)) {
                    q.setScore(q.getScore() + scoringLog.getScore());
                }
            }
        }

        // Sort questions first by their score, then by their creation date.
        Collections.sort(qs, new QuestionComparator());

        for (Question q : qs) {
            // Create a new generic question format and fill it with
            // the specific information of the current question.
            QuestionFormatComponent questionFormatComponent =
                    new QuestionFormatComponent(q, users);

            Optional<ScoringLog> scoringLog = votes.stream()
                    .filter(x -> x.getQuestion().equals(q) && x.getUsers().equals(users))
                    .findFirst();

            if (!scoringLog.isEmpty()) {
                if (scoringLog.get().getScore() == 1) {
                    questionFormatComponent.setLiked();
                } else if (scoringLog.get().getScore() == -1) {
                    questionFormatComponent.setDisliked();
                }
            }

            // Add the updated question to the VBox (i.e. the main questions view).
            stack.getChildren().add(questionFormatComponent);
        }
    }

    /**
     * Set a new user for the view and update the question list
     * every 2 seconds.
     * @param users - the current logged user.
     */
    public void setUsers(Users users) {
        this.users = users;
        greetings.setText("Welcome, " + users.getUsername()
                + " you are in room: " + lectureRoom.getLecturePin());

        // Update question list every 2 seconds.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    displayAllQuestion();
                    try {
                        if (checkRoomClosed()) {
                            timer.cancel();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        refreshPoll();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    // one in 10? times, check if room is still open
                });
            }
        }, 0, 5000);
    }

    private boolean checkRoomClosed() throws IOException {
        boolean closed = false;
        LectureRoom room = ServerCommunication.getLectureRoom(this.lectureRoom.getLecturePin());

        if (!room.isOpen()) {
            closed = true;
            Alerts.alertInfo("Lecture has ended", "You are redirected to the lobby");
            if (this.users.getRole().equals("lecturer")) {
                Display.showLecturer(users);
            } else {
                Display.showStudent(users);
            }
        }
        return closed;
    }

    /**
     * Setter for LectureRoom.
     * @param lectureRoom LectureRoom object
     */
    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    /**
     * Requests the a new poll from the server, updates previous poll results.
     * @throws JsonProcessingException if json is not processed
     */
    public void refreshPoll() throws JsonProcessingException {
        Poll p = ServerCommunication.getPoll(lectureRoom.getLecturePin());

        if (p != null && !p.equals(currentPoll)) {
            currentPoll = p;
            askForVote(p);
        }
        if (p == null) {
            return;
        }
        currentPoll = p;
        int size = currentPoll.getSize();
        int[] results = currentPoll.getVotes();

        XYChart.Series set1 = new XYChart.Series<>();

        for (int i = 0; i < size; i++) {
            set1.getData().add(new XYChart.Data(Character.toString((char) (i + 65)), results[i]));
        }
        if (!currentPoll.isOpen()) {
            pollChart.getData().clear();
            pollChart.getData().addAll(set1);
            pollChart.lookup(".data" + (currentPoll.getRightAnswer() - 65)
                    + ".chart-bar").setStyle("-fx-bar-fill: green");
            pollChart.setAnimated(false);
        }
    }

    /**
     * Creates an alert which contains the poll, sends it to the server.
     * @param poll Poll object
     */
    public void askForVote(Poll poll) {
        if (poll.isOpen()) {
            Optional<Character> c = Alerts.createPoll(poll.getQuestion(), poll.getSize());
            if (!c.isEmpty()) {
                ServerCommunication.vote(c.get(), poll.getId());
            }
        }
    }
}
