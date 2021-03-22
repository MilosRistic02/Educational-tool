package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;


public class LobbyController {

    @FXML
    AnchorPane rootPane;

    @FXML
    TextField pinText;

    @FXML
    TextField courseIdField;

    @FXML
    ChoiceBox scheduleChoiceBox;

    @FXML
    DatePicker startingDate;

    @FXML
    ComboBox startingTimeHours;

    @FXML
    ComboBox startingTimeMinutes;

    @FXML
    Label wrongId;

    @FXML
    Label wrongDate;

    @FXML
    Label pastDate;

    @FXML
    ImageView backToLobby;

    @FXML
    Button logOutStudent;

    @FXML
    Button logOutLecturer;

    private Users users;

    /**
     * Redirects the user to the next scene, where they can enter courseID.
     *
     * @throws IOException if lobbyCreateRoom.fxml cannot be loaded
     */
    @FXML
    public void showLobbyCreateRoom() throws IOException {
        Display.showLobbyCreateRoom(users);
    }

    /**
     * Checks if entered course ID is valid.
     * @throws IOException if a room cannot be created.
     */
    @FXML
    public void createRoomButtonClicked() throws IOException, ParseException {
        String courseId;
        try {
            courseId = courseIdField.getText();
        } catch (NumberFormatException e) {
            wrongId.setVisible(true);
            courseIdField.setText("");
            courseIdField.requestFocus();
            return;
        }
        wrongId.setVisible(false);
        createRoom(courseId);
    }

    /**
     * Creates a new lectureRoom and redirects the lecturer to that room.
     */
    @FXML
    public void createRoom(String courseId) throws IOException, ParseException {
        LectureRoom lectureRoom = null;
        String scheduleChoice = scheduleChoiceBox.getValue().toString();

        if (scheduleChoice.equals("Now")) {
            lectureRoom = new LectureRoom(users.getUsername(), courseId, new Date());
        } else if (scheduleChoice.equals("Custom Time")) {
            Date date = checkDate();
            if (date == null) {
                return;
            }
            lectureRoom = new LectureRoom(users.getUsername(), courseId, date);
        } else {
            lectureRoom = new LectureRoom(users.getUsername(), courseId);
        }

        String response = ServerCommunication.addLectureRoom(lectureRoom);
        if (response.equals("Too many rooms created under this host")) {
            Alerts.alertError("Too many rooms", response);
        } else {
            lectureRoom.setLecturePin(response);
            Alerts.alertInfoCopyAble("Lecture pin", "Your lecture pin is: " + response, 300, 25);
            if (scheduleChoice.equals("Now")) {
                Display.showQuestionLecturer(users, lectureRoom);
            } else {
                Display.showLecturer(users);
            }
        }
    }

    private Date checkDate() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String time = startingTimeHours.getValue() + ":" + startingTimeMinutes.getValue();
        Date lectureStartingTime = null;

        try {
            lectureStartingTime = dateTimeFormat.parse(startingDate.getValue() + " " + time);
        } catch (Exception e) {
            wrongDate.setVisible(true);
            startingDate.setValue(null);
            startingDate.requestFocus();
            return null;
        }
        Date currentDate = new Date();
        if (lectureStartingTime.compareTo(currentDate) < 0) {
            pastDate.setVisible(true);
            startingDate.setValue(null);
            startingDate.requestFocus();
            return null;
        }

        pastDate.setVisible(false);
        wrongDate.setVisible(false);
        return lectureStartingTime;
    }

    /**
     * Disables the date- and timepickers when the lecture is scheduled for now.
     * Enables the date- and timepickers when the lecture is scheduled for a custom time.
     */
    @FXML
    public void enableDatePicker() {
        String scheduleChoice = scheduleChoiceBox.getValue().toString();
        if (scheduleChoice.equals("Now")) {
            startingDate.setDisable(true);
            startingDate.setValue(null);
            startingTimeHours.setDisable(true);
            startingTimeMinutes.setDisable(true);
        } else if (scheduleChoice.equals("Custom Time")) {
            startingDate.setDisable(false);
            startingTimeHours.setDisable(false);
            startingTimeMinutes.setDisable(false);
        }
    }

    /**
     * Redirects the user to the next scene, where they can enter a lecture pin.
     *
     * @throws IOException if lobbyStudent cannot be loaded
     */
    @FXML
    public void joinLobby() throws IOException {
        Display.showStudent(users);
    }

    @FXML
    public void showBackButton() {
        logOutStudent.setVisible(false);
        backToLobby.setVisible(true);
    }

    @FXML
    public void logOut() throws IOException {
        Display.showLogin();
    }

    /**
     * Checks if the lecture pin is currently in the database.
     * In that case, the player joins the lectureRoom with the corresponding pin,
     * else, the player is alerted that the pin is not valid, and can try to enter another pin.
     */
    @FXML
    private void checkPin() throws IOException {
        String pin = pinText.getText();
        LectureRoom response = ServerCommunication.getLectureRoom(pin);
        Date currentDate = new Date();

        if (response == null) {
            Alerts.alertError("Incorrect pin", "This lecture pin is not valid");
        } else if (!response.isOpen()) {
            Alerts.alertError("Closed room", "This lecture room is closed");
            pinText.requestFocus();
        } else {
            Date startingTime = response.getStartingTime();

            if (startingTime != null && startingTime.compareTo(currentDate) > 0) {
                Alerts.alertError("Room is not open",
                        "This lecture room has not yet started. It will open at: " + startingTime);
            } else {
                if (users.getRole().equals("student")) {
                    Display.showQuestion(users, response);
                } else {
                    Display.showQuestionLecturer(users, response);
                }
            }
        }
    }

    /**
     * Setter for the users of this controller.
     * @param users Users we want to set it to.
     */
    public void setUsers(Users users) {
        this.users = users;
    }

    public void showArchiveList() throws IOException {
        Display.showArchiveList(this.users);
    }

    public void loadLobbyLecturer() throws IOException {
        Display.showLecturer(this.users);
    }
}
