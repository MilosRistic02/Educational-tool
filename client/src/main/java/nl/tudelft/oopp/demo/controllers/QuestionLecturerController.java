package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.*;
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
    private TextField numOptionsField;

    @FXML
    private TextField correctAnswer;

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
    public void setUsers(Users users) {
        this.users = users;
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
    }

    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public void refreshPoll() throws JsonProcessingException {
        currentPoll = ServerCommunication.getPoll(currentPoll.getId());

        int size = currentPoll.getSize();
        int results[] = currentPoll.getVotes();

        BarChart.Series set1 = new BarChart.Series<>();

        for(int i = 0; i<size; i++){
            set1.getData().add(new XYChart.Data(Character.toString((char) (i + 65)), results[i]));
        }
        pollChart.getData().addAll(set1);

    }

    public void createPoll() throws JsonProcessingException {
       String pollQuestion = pollField.getText();
       int size = Integer.parseInt(numOptionsField.getText());
       char correctAnswerForTheQuestion = correctAnswer.getText().charAt(0);

       if(size < 2 || size > 10){
           Alerts.alertError("Incorrect number of options", "The number of options should be within 2 and 10");
       }
        Poll poll = new Poll(lectureRoom.getLecturePin(), size, correctAnswerForTheQuestion, pollQuestion);
       String s = ServerCommunication.createPoll(poll);
       currentPoll = new ObjectMapper().readValue(s, Poll.class);


    }
}
