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

    /**
     * Creates a new lectureRoom and redirects the lecturer to that room.
     */
    @FXML
    public void createRoom(){
        // use the create database method
        // TODO

        // redirect the user to the next scene
        // TODO
    }

    /**
     * Redirects the user to the next scene, where they can enter a lecture pin.
     *
     * @throws IOException
     */
    @FXML
    public void joinLobby() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/lobbyStudent.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * Checks if the lecture pin is currently in the database.
     * In that case, the player joins the lectureRoom with the corresponding pin,
     * else, the player is alerted that the pin is not valid, and can try to enter another pin.
     */
    @FXML
    private void checkPin() {
        String pin = pinText.getText();
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

}
