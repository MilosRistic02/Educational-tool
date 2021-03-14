package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
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

public class QuestionLecturerController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    @FXML
    private Text greetings;

    @FXML
    private TextField pollField;

    @FXML
    private ChoiceBox numOptions;

    @FXML
    private ChoiceBox correctAnswer;

    @FXML
    private BarChart pollChart;

    private Users users;

    private LectureRoom lectureRoom;

    public Poll currentPoll;

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
        this.users = users;
        this.lectureRoom = lectureRoom;

        greetings.setText("Welcome, " + users.getUsername()
                + " you are in room: " + lectureRoom.getLecturePin());
        // Update question list every 2 seconds.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> displayAllQuestion());
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
     * Method that is called when refreshing a poll, refreshes the chart.
     * @throws JsonProcessingException Thrown when something goes wrong while processing
     */
    public void refreshPoll() throws JsonProcessingException {
        currentPoll = ServerCommunication.getPoll(currentPoll.getId());

        int size = currentPoll.getSize();
        int[] results = currentPoll.getVotes();

        XYChart.Series set1 = new XYChart.Series<>();

        for (int i = 0; i < size; i++) {
            set1.getData().add(new XYChart.Data(Character.toString((char) (i + 65)), results[i]));
        }
        pollChart.getData().clear();
        pollChart.getData().addAll(set1);
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
        Poll poll = new Poll(lectureRoom.getLecturePin(), size, answer, pollQuestion);
        currentPoll = new ObjectMapper()
                .readValue(ServerCommunication.createPoll(poll), Poll.class);

        pollChart.setAnimated(true);
        pollChart.getData().clear();

        correctAnswer.setDisable(true);
        correctAnswer.getItems().clear();
        numOptions.setValue("Choose an option");
        pollField.setText("");
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
}
