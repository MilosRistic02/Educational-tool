package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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

    private Users users;

    // TODO
    // Show archive
    // close lecture
    // schedule lecture
    // validate input everywhere

    /**
     * Redirects the user to the next scene, where they can enter courseID.
     *
     * @throws IOException if lobbyCreateRoom.fxml cannot be loaded
     */
    @FXML
    public void showLobbyCreateRoom() throws IOException {
        System.out.println(new Date());
        Display.showLobbyCreateRoom(users);
    }

    /**
     * Checks if entered course ID is valid.
     * @throws IOException if a room cannot be created.
     */
    @FXML
    public void createRoomButtonClicked() throws IOException {
        int courseId;
        try {
            courseId = Integer.parseInt(courseIdField.getText());
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
    public void createRoom(int courseId) throws IOException {
        LectureRoom lectureRoom = null;
        String scheduleChoice = scheduleChoiceBox.getValue().toString();

        if(scheduleChoice.equals("Now")) {
            lectureRoom = new LectureRoom(users.getUsername(), courseId, new Date());
        } else if(scheduleChoice.equals("Custom Time")) {
//            Time lectureStartingTime = new LocalTime();
//
//            if(startingDate.getValue().toString().length() == 9){
//                String time = startingTimeHours + ":" + startingTimeMinutes;
//                lectureStartingTime.setTime(lectureStartingTime);
//            } else{
//                // Fill in a valid date
//            }

            lectureRoom = new LectureRoom(users.getUsername(), courseId, new Date());
        } else {
            lectureRoom = new LectureRoom(users.getUsername(), courseId);
        }

        String response = ServerCommunication.addLectureRoom(lectureRoom);
        if (response.equals("Too many rooms created under this host")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Too many rooms");
            alert.setHeaderText(null);
            alert.setContentText(response);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("lecture pin");
            alert.setHeaderText(null);
            alert.setContentText("Your lecture pin is: " + response);
            alert.showAndWait();
            Display.showQuestion(users, lectureRoom);
        }
    }

    /**
     * Disables the date- and timepickers when the lecture is scheduled for now.
     * Enables the date- and timepickers when the lecture is scheduled for a custom time.
     */
    @FXML
    public void enableDatePicker(){
        String scheduleChoice = scheduleChoiceBox.getValue().toString();
        if(scheduleChoice.equals("Now")) {
            startingDate.setDisable(true);
            startingDate.setValue(null);
            startingTimeHours.setDisable(true);
            startingTimeMinutes.setDisable(true);
        } else if(scheduleChoice.equals("Custom Time")) {
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect pin");
            alert.setHeaderText(null);
            alert.setContentText("This lecture pin is not valid");
            alert.showAndWait();
        } else if (!response.isOpen()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Closed room");
            alert.setHeaderText(null);
            alert.setContentText("This lecture room is closed");
            alert.showAndWait();
            pinText.requestFocus();
        } else if (response.getStartingTime().compareTo(currentDate) > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Room not open");
            alert.setHeaderText(null);
            alert.setContentText("This lecture room has not yet started");
            alert.showAndWait();
            pinText.requestFocus();
        } else {
            Display.showQuestion(users, response);
        }
    }

    /**
     * Setter for the users of this controller.
     * @param users Users we want to set it to.
     */
    public void setUsers(Users users) {
        this.users = users;
    }
}
