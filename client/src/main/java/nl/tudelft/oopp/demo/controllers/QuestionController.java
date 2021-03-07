package nl.tudelft.oopp.demo.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;

public class QuestionController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    private Users users;


    @FXML
    private void displayQuestion() {
        Question q = new Question(questionText.getText(), "20", users.getUsername());
        ServerCommunication.saveQuestion(q);
        displayAllQuestion();
    }

    @FXML
    private void displayAllQuestion() {
        List<Question> qs = ServerCommunication.getAllQuestion();
        List<ScoringLog> votes = ServerCommunication.getVotes();



        stack.getChildren().clear();
        stack.setSpacing(15);
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

            QuestionFormatComponent questionFormatComponent = new QuestionFormatComponent();
            questionFormatComponent.question.setText(q.getQuestion());
            questionFormatComponent.score.setText(Integer.toString(q.getScore()));
            questionFormatComponent.author.setText("By " + q.getAuthor());

            // Set to current question.
            questionFormatComponent.setCurrentQuestion(q);
            // Set current logged user.
            questionFormatComponent.setLoggedUser(users);

            // Date format to be displayed.
            String pattern = "HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String date = df.format(q.getCreationDate());

            questionFormatComponent.creationDate.setText(date);
            stack.getChildren().add(questionFormatComponent);
        }
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    /*
     * Alert alert = new Alert(Alert.AlertType.INFORMATION);
     * alert.setTitle("Quote for you");
     * alert.setHeaderText(null);
     * alert.setContentText();
     * alert.showAndWait();
     */
}
