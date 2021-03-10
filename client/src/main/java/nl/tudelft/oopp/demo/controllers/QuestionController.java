package nl.tudelft.oopp.demo.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
            QuestionFormatComponent questionFormatComponent = new QuestionFormatComponent();
            questionFormatComponent.question.setText(q.getQuestion());
            questionFormatComponent.score.setText(Integer.toString(q.getScore()));
            questionFormatComponent.author.setText("By " + q.getAuthor());

            // Set current question.
            questionFormatComponent.setCurrentQuestion(q);
            // Set current logged user.
            questionFormatComponent.setLoggedUser(users);

            // Date format to be displayed.
            String pattern = "HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String date = df.format(q.getCreationDate());
            questionFormatComponent.creationDate.setText(date);

            // Select the like and dislike buttons that were pressed
            // for certain questions by the current logged user.
            for (ScoringLog scoringLog : votes) {
                if (scoringLog.getQuestion().equals(q)
                        && scoringLog.getUsers().equals(users)) {
                    if (scoringLog.getScore() == 1) {
                        questionFormatComponent.like.setSelected(true);
                        questionFormatComponent.dislike.setSelected(false);
                    } else if (scoringLog.getScore() == -1) {
                        questionFormatComponent.dislike.setSelected(true);
                        questionFormatComponent.like.setSelected(false);
                    } else {
                        questionFormatComponent.like.setSelected(false);
                        questionFormatComponent.dislike.setSelected(false);
                    }
                }
            }

            // Show which buttons have been pressed by the current logged user,
            // by changing their color.
            if (questionFormatComponent.like.isSelected()) {
                questionFormatComponent.like.setStyle("-fx-background-color: #99d28c;");
                questionFormatComponent.dislike.setStyle("-fx-background-color: #FFFFFF;");
            } else if (questionFormatComponent.dislike.isSelected()) {
                questionFormatComponent.dislike.setStyle("-fx-background-color: #c3312f;");
                questionFormatComponent.like.setStyle("-fx-background-color: #FFFFFF;");
            } else {
                questionFormatComponent.like.setStyle("-fx-background-color: #FFFFFF;");
                questionFormatComponent.dislike.setStyle("-fx-background-color: #FFFFFF;");
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
        greetings.setText("Welcome, " + users.getUsername());

        // Update question list every 2 seconds.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        displayAllQuestion();
                    }
                });
            }
        }, 0, 2000);
    }

    public void setLectureRoom(LectureRoom lectureRoom) {
        this.lectureRoom = lectureRoom;
    }
}
