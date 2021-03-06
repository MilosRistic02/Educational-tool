package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class QuestionController {

    @FXML
    private VBox stack;

    @FXML
    private TextField questionText;

    @FXML
    private void displayQuestion() {
        Question q = new Question(questionText.getText(), "20", "Rodrigo");
        ServerCommunication.saveQuestion(q);
        displayAllQuestion();
    }

    @FXML
    private void displayAllQuestion() {
        List<Question> qs = ServerCommunication.getAllQuestion();

        stack.getChildren().clear();
        for (Question q : qs) {
            QuestionFormatComponent questionFormatComponent = new QuestionFormatComponent();
            questionFormatComponent.question.setText(q.getQuestion());
            questionFormatComponent.score.setText(Integer.toString(q.getScore()));
            questionFormatComponent.author.setText(q.getAuthor());

            // Date format to be displayed.
            String pattern = "HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String date = df.format(q.getCreationDate());

            questionFormatComponent.creationDate.setText(date);
            stack.getChildren().add(questionFormatComponent);
        }
    }

    /**
     * Alert alert = new Alert(Alert.AlertType.INFORMATION);
     * alert.setTitle("Quote for you");
     * alert.setHeaderText(null);
     * alert.setContentText();
     * alert.showAndWait();
     */
}
