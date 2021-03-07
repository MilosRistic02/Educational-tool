package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.QuoteDisplay;

public class LogInController {

    @FXML
    AnchorPane rootPane;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label emptyFields;

    @FXML
    Label wrongPassword;

    @FXML
    Label invalidUser;

    /**
     * Activated upon a click on the login button.
     * Throws error messages if:
     * some fields are empty
     * user doesn't exist
     * password is wrong
     * @throws JsonProcessingException if the json couldn't be processed
     */
    public void logInButtonClicked() throws JsonProcessingException, IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        reset();
        if (username.length() == 0 || password.length() == 0) {
            emptyFields.setVisible(true);
            usernameField.requestFocus();
            return;
        }

        String response = ServerCommunication.sendCredentials(username, password);
        if (!checkValidResponse(response)) {
            return;
        }

        Users loggedUser = new ObjectMapper().readValue(response, Users.class);
        QuoteDisplay.showQuestion(loggedUser);;
    }

    /**
     * Resets the labels and TextFields.
     */
    private void reset() {
        emptyFields.setVisible(false);
        wrongPassword.setVisible(false);
        invalidUser.setVisible(false);
        passwordField.setText("");
    }

    /**
     * Checks if the response of the server is valid.
     * @param response Json string that is the server's response
     * @return true if valid
     */
    private boolean checkValidResponse(String response) {
        if (response.contains("{")) {
            return true;
        }

        if (response.equals("User doesn't exist")) {
            invalidUser.setVisible(true);
            usernameField.setText("");
            usernameField.requestFocus();
            return false;
        }

        wrongPassword.setVisible(true);
        passwordField.requestFocus();
        return false;
    }

    public void signupButtonClicked() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/register.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
