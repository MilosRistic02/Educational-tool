package nl.tudelft.oopp.demo.controllers.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.controllers.components.QuestionFormatLecturerComponent;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;
import org.controlsfx.control.CheckComboBox;

public class QuestionLecturerController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    @FXML
    private Text greetings;

    @FXML
    private TextField currentRoomPin;

    @FXML
    private Slider speedSlider;

    @FXML
    private ProgressBar progress;

    @FXML
    private Text selectedSpeed;

    @FXML
    private TextArea pollField;

    @FXML
    private ChoiceBox numOptions;

    @FXML
    private CheckComboBox correctAnswers;

    @FXML
    private BarChart pollChart;

    @FXML
    private Button closePollButton;

    @FXML Button adminSettings;

    @FXML
    private ToggleButton changeList;

    @FXML
    private Pane list;

    @FXML
    private Text listTitle;

    @FXML
    private Button createPollButton;


    private Users users;

    private LectureRoom lectureRoom;

    public Poll currentPoll;

    private Timer pollTimer;

    public Timer timer;

    /**
     * Set a new user for the view and update the question list
     * every 2 seconds.
     * @param users - the current logged user.
     */
    public void init(Users users, LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
        this.users = users;
        greetings.setText("Welcome, " + users.getUsername());
        currentRoomPin.setText("Lecture pin: " + lectureRoom.getLecturePin());

        if (users.getRole().equals("admin")) {
            adminSettings.setVisible(true);
            adminSettings.setDisable(false);
        }

        // Update question list every 2 seconds.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (checkRoomClosed()) {
                            timer.cancel();
                            Alerts.alertInfo("Lecture has ended",
                                    "You are redirected to the lobby");
                        }
                        updateSlider();
                        displayAllQuestion();

                    } catch (Exception e) {
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
     * Action when the admin settings button is pressed.
     */
    @FXML
    public void adminAction() {
        Optional<Integer> questionFrequency =  Alerts.numberInputDialog("0",
                "Admin Settings", "Seconds between questions: ",
                "The frequency should be a positive integer");
        if (questionFrequency.isPresent()) {
            lectureRoom.setQuestionFrequency(questionFrequency.get());
            ServerCommunication.updateFrequency(lectureRoom, users.getUsername());
        }
    }

    /**
     * Function that is called when the back to lobby button is clicked.
     * @throws IOException Exception thrown when something goes wrong with IO
     */
    @FXML
    public void backToLobby() throws IOException {
        Display.showLecturer(users);
        timer.cancel();
        pollTimer.cancel();
    }

    @FXML
    private void displayQuestion() throws JsonProcessingException {
        Question q = new Question(questionText.getText(),
                lectureRoom.getLecturePin(),
                users.getUsername());
        ServerCommunication.saveQuestion(q, users.getUsername());
        displayAllQuestion();
    }

    @FXML
    private void displayAllQuestion() throws JsonProcessingException {
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

        stack.getChildren().clear();
        stack.setSpacing(20);   // Space between questions.

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
        ServerCommunication.closeRoom(this.lectureRoom, users.getUsername());
        timer.cancel();
        pollTimer.cancel();
        if (this.users.getRole().equals("lecturer")) {
            Display.showLecturer(this.users);
        } else {
            Display.showStudent(this.users);
        }
    }

    /**
     * Method to set the slider to the average of the scores in the database.
     */
    public void updateSlider() throws JsonProcessingException {
        double speedScore = ServerCommunication.speedGetVotes(lectureRoom.getLecturePin());

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
            selectedSpeed.setText("Your pace is okay");
            progress.setStyle("-fx-accent: #99d28c;");
            selectedSpeed.setFill(Color.valueOf("#99d28c"));
        }
    }

    private boolean checkRoomClosed() throws IOException {
        boolean closed = false;
        LectureRoom room = ServerCommunication.getLectureRoom(this.lectureRoom.getLecturePin());

        if (!room.isOpen()) {
            closed = true;
            timer.cancel();
            pollTimer.cancel();
            if (this.users.getRole().equals("student")) {
                Display.showStudent(users);
            } else {
                Display.showLecturer(users);
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
        for (Character c : currentPoll.getRightAnswer()) {
            pollChart.lookup(".data" + (c - 65)
                    + ".chart-bar").setStyle("-fx-bar-fill: green");
        }
        pollChart.setAnimated(false);
    }

    /**
     * Method for creating a poll.
     * @throws JsonProcessingException Thrown when something goes wrong while processing
     */
    public void createPoll() throws JsonProcessingException {

        String pollQuestion = pollField.getText();
        if (pollQuestion.length() > 254) {
            Alerts.alertError("Poll error",
                    "Please write a question that is smaller than 255 tokens!");
            return;
        }
        Object sizeInput = numOptions.getValue();
        if (!(sizeInput instanceof Integer)) {
            Alerts.alertError("Poll error", "Please pick a size");
            return;
        }

        int size = (int) sizeInput;
        List<Character> answers = correctAnswers.getCheckModel().getCheckedItems();

        closePoll();
        createPollButton.setText("Update poll");
        Poll poll = new Poll(lectureRoom.getLecturePin(), size, answers, pollQuestion);
        currentPoll = new ObjectMapper()
                .readValue(ServerCommunication.createPoll(poll, users.getUsername()), Poll.class);

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

        correctAnswers.setDisable(true);
        correctAnswers.getItems().clear();
        numOptions.setValue("Choose an option");
        pollField.setText("");
        closePollButton.setVisible(true);
    }

    /**
     * Triggered when an option for the number of answers in the poll is picked.
     */
    public void optionPicked() {
        Object input = numOptions.getValue();
        correctAnswers.getItems().clear();
        if (!(numOptions.getValue() instanceof Integer)) {
            correctAnswers.setDisable(true);
            correctAnswers.setTitle("Pick answers");
            return;
        }

        int n = (int) input;
        correctAnswers.getItems().clear();
        for (int i = 65; i < n + 65; i++) {
            correctAnswers.getItems().add((char) i);
            correctAnswers.getCheckModel().clearCheck(i - 65);
        }
        correctAnswers.setDisable(false);
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
        ServerCommunication.closePoll(currentPoll, users.getUsername());
        createPollButton.setText("Create poll");
    }

}
