package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;

public class QuestionController {

    @FXML
    private TextField questionText;

    @FXML
    private void displayQuestion() {
        Question q = new Question(questionText.getText(), "20", "Rodrigo");
        ServerCommunication.saveQuestion(q);
    }

    /*
     * Alert alert = new Alert(Alert.AlertType.INFORMATION);
     * alert.setTitle("Quote for you");
     * alert.setHeaderText(null);
     * alert.setContentText();
     * alert.showAndWait();
     */
}
