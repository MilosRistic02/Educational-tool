package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.SpeedLog;
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
    private Text currentRoom;

    @FXML
    private Slider speedSlider;

    @FXML
    private Text selectedSpeed;

    private Users loggedUser;

    private LectureRoom lectureRoom;

    private SpeedLog speedLog;


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
                    loggedUser.getUsername());

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
                    new QuestionFormatComponent(q, loggedUser);

            Optional<ScoringLog> scoringLog = votes.stream()
                    .filter(x -> x.getQuestion().equals(q) && x.getUsers().equals(loggedUser))
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
        this.loggedUser = users;
        greetings.setText("Welcome, " + users.getUsername());
        currentRoom.setText("You are in lecture " + lectureRoom.getLecturePin());
        // set the speed log to 0
        this.speedLog = new SpeedLog(this.loggedUser, this.lectureRoom, 0);
        // send speedlog to the server to reset any old values
        ServerCommunication.speedVote(this.speedLog);
        // change listener added to the slider
        speedSlider.valueProperty()
                .addListener(((observable, oldValue, newValue) -> updateSlider()));

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
            if (this.loggedUser.getRole().equals("lecturer")) {
                Display.showLecturer(loggedUser);
            } else {
                Display.showStudent(loggedUser);
            }
        }
        return closed;
    }

    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    @FXML
    public void updateSlider() {
        int s = (int) speedSlider.getValue();
        this.speedLog.setSpeed(s);
        selectedSpeed.setVisible(true);

        if (s <= 15) {
            selectedSpeed.setText("Very Slow");
            selectedSpeed.setFill(Color.valueOf("#00b7d3"));
        } else if (s <= 35) {
            selectedSpeed.setText("Slow");
            selectedSpeed.setFill(Color.valueOf("#00a390"));
        } else if (s <= 65) {
            selectedSpeed.setText("Okay");
            selectedSpeed.setFill(Color.valueOf("#99d28c"));
        } else if (s <= 85) {
            selectedSpeed.setText("Fast");
            selectedSpeed.setFill(Color.valueOf("#f1be3e"));
        } else {
            selectedSpeed.setText("Very fast");
            selectedSpeed.setFill(Color.valueOf("#c3312f"));
        }
        ServerCommunication.speedVote(this.speedLog);
    }

    @FXML
    public void logOut() throws IOException {
        Display.showLogin();
    }

    @FXML
    public void changeLecture() throws IOException {
        Display.showStudent(loggedUser);
    }

}
