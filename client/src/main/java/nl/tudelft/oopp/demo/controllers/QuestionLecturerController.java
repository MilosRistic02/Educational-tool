package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
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

    private Users users;

    private LectureRoom lectureRoom;


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
    public void setUsers(Users users, LectureRoom lectureRoom) {
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
                    updateSlider();
                });
            }
        }, 0, 2000);
    }

    /**
     * Method to set the slider to the average of the scores in the database.
     */
    public void updateSlider() {
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
        } else {
            selectedSpeed.setText("Your pace is very fast");
            progress.setStyle("-fx-accent: #c3312f;");
            selectedSpeed.setFill(Color.valueOf("#c3312f"));
        }
    }
}
