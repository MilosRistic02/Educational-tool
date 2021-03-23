package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

import com.sun.jdi.request.ExceptionRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;

public class QuestionLecturerController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    @FXML
    private Text greetings;

    @FXML
    private Slider speedSlider;

    @FXML
    private ProgressBar progress;

    @FXML
    private Text selectedSpeed;

    @FXML
    private TextField pollField;

    @FXML
    private ChoiceBox numOptions;

    @FXML
    private ChoiceBox correctAnswer;

    @FXML
    private BarChart pollChart;

    @FXML
    private Button closePollButton;

    @FXML Button adminSettings;

    private Users users;

    private LectureRoom lectureRoom;

    public Poll currentPoll;

    private Timer pollTimer;

    @FXML
    public void adminAction() {
        Optional<Integer> frequency =  Alerts.numberInputDialog("0",
                "Admin Settings", "Seconds between questions: ", "The frequency should be a positive integer");
        lectureRoom.setFrequency(frequency.get());
        ServerCommunication.updateFrequency(lectureRoom);
    }


    @FXML
    private void displayQuestion() {
        Question q = new Question(questionText.getText(),
                lectureRoom.getLecturePin(),
                users.getUsername());
        ServerCommunication.saveQuestion(q);
        displayAllQuestion();
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

        for (Question q: qs) {
            // Create a new generic question format and fill it with
            // the specific information of the current question.
            QuestionFormatLecturerComponent questionFormatLecturerComponent =
                    new QuestionFormatLecturerComponent(q, users);

            // Add the updated question to the VBox (i.e. the main questions view).
            stack.getChildren().add(questionFormatLecturerComponent);
        }
    }

    /**
     * Closes the lecture room.
     * @throws IOException if server communication fails.
     */
    @FXML
    public void closeRoom() throws IOException {
        this.lectureRoom.setOpen(false);
        String response = ServerCommunication.closeRoom(this.lectureRoom);

        if (this.users.getRole().equals("lecturer")) {
            Display.showLecturer(this.users);
        } else {
            Display.showStudent(this.users);
        }
    }

    /**
     * Set a new user for the view and update the question list
     * every 2 seconds.
     * @param users - the current logged user.
     */
    public void init(Users users, LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
        this.users = users;
        greetings.setText("Welcome, " + users.getUsername());

        // Update question list every 2 seconds.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    displayAllQuestion();
                    try {
                        updateSlider();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (checkRoomClosed()) {
                            timer.cancel();
                            Alerts.alertInfo("Lecture has ended",
                                    "You are redirected to the lobby");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 2000);

        numOptions.getItems().add("Choose an option");
        for (int i = 2; i <= 10; i++) {
            numOptions.getItems().add(i);
        }
        numOptions.setValue("Choose an option");
        numOptions.setOnAction((EventHandler<ActionEvent>) event -> optionPicked());
    }

    /**
     * Method to set the slider to the average of the scores in the database.
     */
    public void updateSlider() throws JsonProcessingException {
        List<SpeedLog> speedLogs = ServerCommunication.speedGetVotes();

        double speedScore = 0;
        int speedLenght = 0;
        for (SpeedLog speedLog : speedLogs) {
            if (speedLog.getLectureRoom().getLecturePin().equals(lectureRoom.getLecturePin())) {
                speedScore += speedLog.getSpeed();
                speedLenght++;
            }
        }

        speedScore = speedScore / speedLenght;
        progress.setProgress(speedScore / 100);
        selectedSpeed.setVisible(true);
        progress.setVisible(true);

        if (speedScore <= 15) {
            selectedSpeed.setText("Your pace is very slow");
            progress.setStyle("-fx-accent: #00b7d3;");
            selectedSpeed.setFill(Color.valueOf("#00b7d3"));
        } else if (speedScore <= 35) {
            selectedSpeed.setText("Your pace is slow");
            progress.setStyle("-fx-accent: #00a390;");
            selectedSpeed.setFill(Color.valueOf("#00a390"));
        } else if (speedScore <= 65) {
            selectedSpeed.setText("Your pace is okay");
            progress.setStyle("-fx-accent: #99d28c;");
            selectedSpeed.setFill(Color.valueOf("#99d28c"));
        } else if (speedScore <= 85) {
            selectedSpeed.setText("Your pace is fast");
            progress.setStyle("-fx-accent: #f1be3e;");
            selectedSpeed.setFill(Color.valueOf("#f1be3e"));
        } else if (speedScore > 85) {
            selectedSpeed.setText("Your pace is very fast");
            progress.setStyle("-fx-accent: #c3312f;");
            selectedSpeed.setFill(Color.valueOf("#c3312f"));
        } else {
            progress.setVisible(false);
        }
    }

    private boolean checkRoomClosed() throws IOException {
        boolean closed = false;
        LectureRoom room = ServerCommunication.getLectureRoom(this.lectureRoom.getLecturePin());

        if (!room.isOpen()) {
            closed = true;
            if (this.users.getRole().equals("lecturer")) {
                Display.showLecturer(users);
            } else {
                Display.showStudent(users);
            }
        }
        return closed;
    }

    /**
     * Method that is called when refreshing a poll, refreshes the chart.
     * @throws JsonProcessingException Thrown when something goes wrong while processing
     */
    public void refreshPoll() throws JsonProcessingException {
        currentPoll = ServerCommunication.getPoll(lectureRoom.getLecturePin());

        int size = currentPoll.getSize();
        int[] results = currentPoll.getVotes();

        XYChart.Series set1 = new XYChart.Series<>();

        for (int i = 0; i < size; i++) {
            set1.getData().add(new XYChart.Data(Character.toString((char) (i + 65)), results[i]));
        }
        pollChart.getData().clear();
        pollChart.getData().addAll(set1);
        pollChart.lookup(".data" + (currentPoll.getRightAnswer() - 65)
                + ".chart-bar").setStyle("-fx-bar-fill: green");
        pollChart.setAnimated(false);
    }

    /**
     * Method for creating a poll.
     * @throws JsonProcessingException Thrown when something goes wrong while processing
     */
    public void createPoll() throws JsonProcessingException {

        String pollQuestion = pollField.getText();
        Object sizeInput = numOptions.getValue();
        if (!(sizeInput instanceof Integer)) {
            Alerts.alertError("Poll error", "Please pick a size");
            return;
        }

        int size = (int) sizeInput;
        Character answer = (char) correctAnswer.getValue();

        closePoll();
        Poll poll = new Poll(lectureRoom.getLecturePin(), size, answer, pollQuestion);
        currentPoll = new ObjectMapper()
                .readValue(ServerCommunication.createPoll(poll), Poll.class);

        pollTimer = new Timer();
        pollTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        refreshPoll();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 2000);

        pollChart.setAnimated(true);
        pollChart.getData().clear();

        correctAnswer.setDisable(true);
        correctAnswer.getItems().clear();
        numOptions.setValue("Choose an option");
        pollField.setText("");
        closePollButton.setVisible(true);
    }

    /**
     * Triggered when an option for the number of answers in the poll is picked.
     */
    public void optionPicked() {
        Object input = numOptions.getValue();
        correctAnswer.getItems().clear();
        if (!(numOptions.getValue() instanceof Integer)) {
            correctAnswer.setDisable(true);
            return;
        }
        int n = (int) input;
        for (int i = 65; i < n + 65; i++) {
            correctAnswer.getItems().add((char) i);
        }
        correctAnswer.setDisable(false);
        correctAnswer.setValue('A');
    }

    /**
     * Method for closing a poll.
     */
    public void closePoll() {
        if (currentPoll == null) {
            return;
        }
        pollTimer.cancel();
        closePollButton.setVisible(false);
        currentPoll.setOpen(false);
        ServerCommunication.closePoll(currentPoll);
    }
}
