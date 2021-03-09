package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import nl.tudelft.oopp.demo.communication.ServerCommunication;



public class RegisterController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField reEnteredPasswordField;

    @FXML
    private Label emptyFields;

    @FXML
    private Label userExists;

    @FXML
    private Label nonMatchingPassword;

    @FXML
    private Label emailAlreadyExists;

    @FXML
    private Label emailInvalid;

    @FXML
    private Label userSuccess;

    /**
     * Activated upon a click on the register button.
     * Throws error messages if:
     * some fields are empty
     * passwords do not match
     * user already exists
     * email is not valid
     * email is already being used by another user
     * @throws IOException if login page cannot be loaded
     */
    public void registerButtonClicked() throws IOException, InterruptedException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String reEnteredPassword = reEnteredPasswordField.getText();

        reset();
        if (!checkRequest(username, password, email, reEnteredPassword)) {
            return;
        }

        String response = ServerCommunication.sendCredentials(username, email, password);
        if (!checkResponse(response)) {
            return;
        }

        userSuccess.setVisible(true);
        new Timeline(new KeyFrame(Duration.millis(750), event -> {
            try {
                loadLoginPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        })).play();
    }

    /**
     * Loads the login page.
     * @throws IOException Thrown when something goes wrong with IO
     */
    public void loadLoginPage() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    /**
     * Checks the request to see if it is valid.
     * @param user String with the username entered by the user
     * @param password String with the password the user provided
     * @param email String with the email the user provided
     * @param rePassword String with the confirmation password the user entered
     * @return Boolean that is true iff the request is valid
     */
    private boolean checkRequest(String user, String password, String email, String rePassword) {
        if (user.length() == 0
                || password.length() == 0
                || email.length() == 0
                || rePassword.length() == 0) {
            emptyFields.setVisible(true);
            usernameField.requestFocus();
            return false;
        }
        if (!password.equals(rePassword)) {
            nonMatchingPassword.setVisible(true);
            passwordField.requestFocus();
            return false;
        }
        if (!isValidEmailAddress(email)) {
            emailInvalid.setVisible(true);
            emailField.setText("");
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Checks the response to see if it is valid.
     * @param response String with the response message
     * @return Boolean that is true iff the response is valid
     */
    private boolean checkResponse(String response) {
        if (response.equals("This user already exists!")) {
            userExists.setVisible(true);
            usernameField.setText("");
            usernameField.requestFocus();
            return false;
        }
        if (response.equals("This email address is already used!")) {
            emailAlreadyExists.setVisible(true);
            emailField.setText("");
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Reset all the Labels and TextFields.
     */
    private void reset() {
        emptyFields.setVisible(false);
        nonMatchingPassword.setVisible(false);
        userExists.setVisible(false);
        emailAlreadyExists.setVisible(false);
        emailInvalid.setVisible(false);
        passwordField.setText("");
        reEnteredPasswordField.setText("");
    }

    //https://stackoverflow.com/questions/8204680/java-regex-email
    public static final Pattern validEmailRegex =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Checks to see if an email is valid.
     * @param email being analyzed
     * @return true if the email is valid.
     */
    public static boolean isValidEmailAddress(String email) {
        Matcher matcher = validEmailRegex.matcher(email);
        return matcher.find();
    }


}
