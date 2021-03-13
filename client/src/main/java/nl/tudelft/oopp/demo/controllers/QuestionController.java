package nl.tudelft.oopp.demo.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;

public class QuestionController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    @FXML
    private Text greetings;

    private Users users;

    private LectureRoom lectureRoom;


    @FXML
    private void displayQuestion() {
        if(questionText.getText().isEmpty()) {
            Alerts.alertInfo("Question is empty",
                    "Please fill out the field before pressing send");
        } else if (questionText.getText().length() > 255) {
            Alerts.alertInfo("Question too long",
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


        for (Question q: qs) {
            // Create a new generic question format and fill it with
            // the specific information of the current question.
            QuestionFormatComponent questionFormatComponent = new QuestionFormatComponent();
            questionFormatComponent.question.setText(q.getQuestion());
            questionFormatComponent.score.setText(Integer.toString(q.getScore()));
            questionFormatComponent.author.setText("By " + q.getAuthor());

            if (q.isAnswered()) {
                // Make answer visible and update its content.
                questionFormatComponent.answerHeading.setVisible(true);
                questionFormatComponent.answer.setVisible(true);
                questionFormatComponent.answer.setText(q.getAnswer());
                // change border of the question to green.
                questionFormatComponent.qanda
                        .setStyle("-fx-border-color: #99d28c ; "
                                + "-fx-border-width: 4; -fx-border-radius: 18");
                // change status to "Answered" and its color to green.
                questionFormatComponent.isAnswered.setText("Answered");
                questionFormatComponent.isAnswered.setFill(Color.valueOf("#99d28c"));
            }

            // Set current question.
            questionFormatComponent.setCurrentQuestion(q);
            // Set current logged user.
            questionFormatComponent.setLoggedUser(users);

            // Date format to be displayed.
            String pattern = "HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String date = df.format(q.getCreationDate());
            questionFormatComponent.creationDate.setText(date);

            Optional<ScoringLog> scoringLog = votes.stream()
                    .filter(x -> x.getQuestion().equals(q) && x.getUsers().equals(users))
                    .findFirst();

            if(!scoringLog.isEmpty()) {
                if (scoringLog.get().getScore() == 1) {
                    questionFormatComponent.setLiked();
                } else if (scoringLog.get().getScore() == -1) {
                    questionFormatComponent.setDisliked();
                }
            }

            // Enable the delete button, only for the questions made by the current logged user.
            if (q.getAuthor().equals(users.getUsername())) {
                questionFormatComponent.delete.setDisable(false);
                questionFormatComponent.delete.setVisible(true);
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
                Platform.runLater(() -> displayAllQuestion());
            }
        }, 0, 2000);
    }

    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }
}
