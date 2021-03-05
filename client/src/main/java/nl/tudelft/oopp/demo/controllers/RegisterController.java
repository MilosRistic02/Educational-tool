package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import nl.tudelft.oopp.demo.communication.ServerCommunication;



public class RegisterController {

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

    public void registerButtonClicked() throws JsonProcessingException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String reEnteredPassword = reEnteredPasswordField.getText();
        reset();
        if (username.length() == 0
                || password.length() == 0
                || email.length() == 0
                || reEnteredPassword.length() == 0) {
            emptyFields.setVisible(true);
            usernameField.requestFocus();
            return;
        }
        if (!password.equals(reEnteredPassword)) {
            nonMatchingPassword.setVisible(true);
            reEnteredPasswordField.requestFocus();
            return;
        }
        if (!isValidEmailAddress(email)) {
            emailInvalid.setVisible(true);
            emailField.requestFocus();
            return;
        }
        String response = ServerCommunication.sendCredentials(username, email, password);
        if (response.equals("This user already exists!")) {
            userExists.setVisible(true);
            usernameField.requestFocus();
            return;
        }
        if (response.equals("This email address is already used!")) {
            emailAlreadyExists.setVisible(true);
            usernameField.requestFocus();
            return;
        }




        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration successful");
        alert.setContentText(response);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void reset() {
        emptyFields.setVisible(false);
        nonMatchingPassword.setVisible(false);
        userExists.setVisible(false);
        emailAlreadyExists.setVisible(false);
        emailInvalid.setVisible(false);

        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        reEnteredPasswordField.setText("");


        usernameField.requestFocus();
    }

    //https://stackoverflow.com/questions/8204680/java-regex-email
    public static final Pattern validEmailRegex =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmailAddress(String email) {
        Matcher matcher = validEmailRegex.matcher(email);
        return matcher.find();
    }
}
