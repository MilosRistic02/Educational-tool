package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class QuestionController {

    @FXML
    private TextField questionText;

    @FXML
    private void displayQuestion() {
        String question = questionText.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quote for you");
        alert.setHeaderText(null);
        alert.setContentText(ServerCommunication.saveQuestion("{\"question\" : "
                + "\"" + question + "\","
                + "\"lecturePin\":\"342\",\"author\":\"hello\"}"));
        alert.showAndWait();
    }
}
