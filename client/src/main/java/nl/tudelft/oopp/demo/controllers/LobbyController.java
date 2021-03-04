package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class LobbyController {

    @FXML
    private TextField pinText;

    @FXML
    private void checkPin() {
        String question = pinText.getText();

        // Check if the pinText is an existing lecturePin with queries
        // if it exists, joinRoom(lecturePin)
        // else give an alert that the pin is not correct
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Incorrect pin");
        alert.setHeaderText(null);
        alert.setContentText("This lecturepin is not valid");
        alert.showAndWait();
    }
}
