package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class LogInController {

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
     * user doesn't exists
     * @throws JsonProcessingException if the json couldn't be processed
     */
    public void logInButtonClicked() throws JsonProcessingException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        reset();
        if (username.length() == 0 || password.length() == 0) {
            emptyFields.setVisible(true);
            usernameField.requestFocus();
            return;
        }

        String response = ServerCommunication.sendCredentials(username, password);
        if (response.equals("User doesn't exist")) {
            invalidUser.setVisible(true);
            usernameField.setText("");
            usernameField.requestFocus();
            return;
        }
        if (response.equals("Wrong password")) {
            wrongPassword.setVisible(true);
            passwordField.setText("");
            passwordField.requestFocus();
            return;
        }
        if (!checkValidResponse(response)) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You logged in");
        alert.setContentText(response);
        alert.setHeaderText(null);
        alert.showAndWait();
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
     * Checks if the reponse of the server is valid.
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
}
