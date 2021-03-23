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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
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
    private BarChart pollChart;

    @FXML
    private ToggleButton changeList;

    @FXML
    private Pane list;

    @FXML
    private Text listTitle;

    private Poll currentPoll;

    private Users users;

    @FXML
    private Slider speedSlider;

    @FXML
    private Text selectedSpeed;

    private Users loggedUser;

    private LectureRoom lectureRoom;

    private SpeedLog speedLog;

    public Timer timer;


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
        List<Question> qs = null;
        if (changeList.isSelected()) {
            qs = ServerCommunication.getAllAnsweredQuestions(lectureRoom.getLecturePin());
            changeList.setText("questions");
            changeList.setStyle("-fx-background-color: #00A6D6;");
            listTitle.setText("Answers");
            list.setStyle("-fx-background-color: #99d28c;"
                            + "-fx-background-radius: 18;");

        } else {
            qs = ServerCommunication.getAllNonAnsweredQuestions(lectureRoom.getLecturePin());
            changeList.setText("answers");
            changeList.setStyle("-fx-background-color: #99d28c");
            listTitle.setText("Questions");
            list.setStyle("-fx-background-color: #00A6D6;"
                    + "-fx-background-radius: 18;");
        }

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
    public void init(Users users, LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
        this.loggedUser = users;
        greetings.setText("Welcome, " + users.getUsername());
        currentRoom.setText("You are in lecture " + lectureRoom.getLecturePin());
        // set the speed log to 0
        this.speedLog = new SpeedLog(this.loggedUser, this.lectureRoom, 50);
        // send speedlog to the server to reset any old values
        ServerCommunication.speedVote(this.speedLog);
        // change listener added to the slider
        speedSlider.valueProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    try {
                        updateSlider();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }));

        // Update question list every 2 seconds.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    displayAllQuestion();
                    try {
                        if (checkRoomClosed()) {
                            timer.cancel();
                            Alerts.alertInfo("Lecture has ended",
                                    "You are redirected to the lobby");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        refreshPoll();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    try{
                        checkBanned();
                    }catch(Exception e ){
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 2000);
    }

    private void checkBanned() throws IOException {
        boolean isUserBanned = ServerCommunication.isUserBanned(loggedUser.getUsername());

        if (isUserBanned) {
            timer.cancel();
            Display.showLogin();
            Alerts.alertError("BAN", "You are banned from this application");
        }
    }

    private boolean checkRoomClosed() throws IOException {
        boolean closed = false;
        LectureRoom room = ServerCommunication.getLectureRoom(this.lectureRoom.getLecturePin());

        if (!room.isOpen()) {
            closed = true;
            if (this.loggedUser.getRole().equals("lecturer")) {
                Display.showLecturer(loggedUser);
            } else {
                Display.showStudent(loggedUser);
            }
        }
        return closed;
    }

    /**
     * The value selected by the current logged user for
     * the pace of the lecture is sent to the database. A
     * message shows what the value means in words.
     */
    @FXML
    public void updateSlider() throws JsonProcessingException {
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

    /**
     * Setter for LectureRoom.
     * @param lectureRoom LectureRoom object
     */
    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    /**
     * Action on the logout button and sets the speed to the default.
     * @throws IOException can throw an exception
     */
    @FXML
    public void logOut() throws IOException {
        this.speedLog.setSpeed(50);
        ServerCommunication.speedVote(this.speedLog);
        Display.showLogin();
    }

    /**
     * Action on the change lecture button and sets the speed to the default.
     * @throws IOException can throw an exception
     */
    @FXML
    public void changeLecture() throws IOException {
        this.speedLog.setSpeed(50);
        ServerCommunication.speedVote(this.speedLog);
        Display.showStudent(loggedUser);
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
            for(Character c : currentPoll.getRightAnswer()) {
                pollChart.lookup(".data" + (c - 65)
                        + ".chart-bar").setStyle("-fx-bar-fill: green");
            }
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
