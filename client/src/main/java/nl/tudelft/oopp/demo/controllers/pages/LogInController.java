package nl.tudelft.oopp.demo.controllers.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;

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

    @FXML
    Label userBannedLabel;

    /**
     * Activated upon a click on the login button.
     * Throws error messages if:
     * some fields are empty
     * user doesn't exist
     * password is wrong
     * @throws JsonProcessingException if the json couldn't be processed
     */
    public void logInButtonClicked() throws IOException {
        String username = usernameField.getText().toLowerCase();
        int passwordHash = Objects.hash(passwordField.getText());
        String password = String.valueOf(passwordHash);

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
        if (loggedUser.getRole().equals("student")) {
            Display.showStudent(loggedUser);
        } else {
            Display.showLecturer(loggedUser);
        }
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

        if (response.equals("This user is banned!")) {
            userBannedLabel.setVisible(true);
            usernameField.setText("");
            usernameField.requestFocus();
            return false;
        }

        wrongPassword.setVisible(true);
        passwordField.requestFocus();
        return false;
    }

    /**
     * Loads the signup page.
     * @throws IOException Thrown when something goes wrong with IO
     */
    public void signupButtonClicked() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/register.fxml"));
        rootPane.getChildren().setAll(pane);
        //System.out.println(Alerts.createAlert("this is a question", 10));
    }
}
