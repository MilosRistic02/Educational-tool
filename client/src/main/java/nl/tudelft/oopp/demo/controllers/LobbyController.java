package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.views.LobbyLecturerDisplay;
import nl.tudelft.oopp.demo.views.LobbyStudentDisplay;

import java.io.IOException;

public class LobbyController {

    @FXML
    TextField pinText;

    @FXML
    AnchorPane rootPane;


    @FXML
    private void checkPin() {
        String pin = pinText.getText();

        //TODO
        // lobby voor mod/lecturer
        // issues gitlab


        // Check if the pinText is an existing lecturePin with queries
        // if it exists, joinRoom(lecturePin)
        // else give an alert that the pin is not correct
        if (ServerCommunication.checkPin(pin)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Correct pin");
            alert.setHeaderText(null);
            alert.setContentText("This lecture pin is valid");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incorrect pin");
            alert.setHeaderText(null);
            alert.setContentText("This lecture pin is not valid");
            alert.showAndWait();
        }


    }

    public void enterPin() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/lobbyStudent.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
