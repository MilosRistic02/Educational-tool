package nl.tudelft.oopp.demo.controllers.pages;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.controllers.components.ArchiveFormatComponent;
import nl.tudelft.oopp.demo.controllers.components.PollFormatComponent;
import nl.tudelft.oopp.demo.controllers.components.QuestionFormatLecturerComponent;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;


public class ArchiveController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox stack;

    @FXML
    private Label emptyArchive;

    @FXML
    private Text archiveHeader;

    @FXML
    private Text archiveRoomHeader;

    @FXML
    private TextField searchBar;

    @FXML
    private ImageView glass;

    @FXML
    private Button showPolls;

    @FXML
    private Button writeArchive;

    private Users user;
    private List<LectureRoom> rooms;
    private String lecturePin;

    /**
     * String that displays in which view the user is in the archive.
     * The default is "pins", later "questions" and "polls" are used.
     */
    private String archiveView;

    /**
     * Displays all of the rooms closed by the lecturer in the archive.
     */
    public void showPins() throws JsonProcessingException {
        showButtons(false);
        searchBar.setVisible(true);
        glass.setVisible(true);
        emptyArchive.setVisible(false);
        archiveView = "pins";
        archiveHeader.setVisible(true);
        archiveRoomHeader.setVisible(false);
        this.rooms =  ServerCommunication.getClosedLecturePins();

        stack.getChildren().clear();
        stack.setSpacing(20);

        if (rooms.isEmpty()) {
            emptyArchive.setText("No lecture rooms found!");
            emptyArchive.setVisible(true);
            searchBar.setDisable(true);
        } else {
            searchBar.setDisable(false);
            for (LectureRoom room : rooms) {
                addRoom(room);
            }
        }
    }

    /**
     * Method called when a letter is typed in the search bar or the search icon is clicked.
     * Adds all found rooms to the Vbox.
     */
    @FXML
    public void searchCourse() {
        String course = searchBar.getText().toLowerCase();
        stack.getChildren().clear();
        stack.setSpacing(20);

        for (LectureRoom room : this.rooms) {
            if (room.getCourseId().toLowerCase().contains(course)
                || room.getLectureName().toLowerCase().contains(course)
                || room.getLecturePin().toLowerCase().contains(course)) {
                addRoom(room);
                emptyArchive.setVisible(false);
            }
        }
        if (stack.getChildren().isEmpty()) {
            emptyArchive.setVisible(true);
            emptyArchive.setText("Nothing to show");
        }
    }

    /**
     * Opens a closed lecture in the archive view.
     * @param lecturePin - Pin of the lecture that is displayed in archive view
     */
    @FXML
    public void showArchive(String lecturePin) {
        showButtons(true);
        this.lecturePin = lecturePin;
        searchBar.setVisible(false);
        glass.setVisible(false);
        emptyArchive.setVisible(false);
        archiveHeader.setVisible(false);
        archiveRoomHeader.setVisible(true);
        archiveView = "questions";
        archiveRoomHeader.setText("Archive of room " + lecturePin);

        try {
            showQuestions(lecturePin);
        } catch (JsonProcessingException e) {
            emptyArchive.setVisible(true);
            emptyArchive.setText("Something went wrong..");
        }
    }

    /**
     * Shows all of the questions in a lectureRoom.
     * @throws JsonProcessingException when the lectureRoom is not found.
     */
    @FXML
    private void showQuestions(String lecturePin) throws JsonProcessingException {
        stack.getChildren().clear();
        stack.setSpacing(20);

        List<Question> questions = ServerCommunication.getAllQuestion(lecturePin);

        if (questions.isEmpty()) {
            emptyArchive.setVisible(true);
            emptyArchive.setText("Archive is empty!");
        }

        for (Question q : questions) {
            QuestionFormatLecturerComponent questionFormat =
                    new QuestionFormatLecturerComponent(q, user);
            questionFormat.verbal.setVisible(false);
            questionFormat.banButton.setVisible(false);
            questionFormat.delete.setVisible(false);
            stack.getChildren().add(questionFormat);
        }
    }

    @FXML
    private void showPolls() throws JsonProcessingException {
        showButtons(false);
        searchBar.setVisible(false);
        glass.setVisible(false);
        emptyArchive.setVisible(false);
        archiveView = "polls";
        archiveRoomHeader.setText("Polls of " + lecturePin);
        stack.getChildren().clear();
        stack.setSpacing(20);

        List<Poll> polls = ServerCommunication.getAllPolls(this.lecturePin);
        if (polls.isEmpty()) {
            emptyArchive.setText("No polls used in this room");
            emptyArchive.setVisible(true);
        } else {
            for (Poll poll : polls) {
                PollFormatComponent pollFormatComponent = new PollFormatComponent(poll);

                stack.getChildren().add(pollFormatComponent);
            }
        }
    }

    /**
     * Exports an archived room to a text file.
     * FileChooser tutorial used: https://www.youtube.com/watch?v=7lnVelyHxrc
     */
    @FXML
    private void writeArchive() throws JsonProcessingException {
        // Create a fileChooser
        FileChooser fileChooser = new FileChooser();
        Window stage = rootPane.getScene().getWindow();

        fileChooser.setTitle("Exporting Archived Room");
        LectureRoom room = ServerCommunication.getLectureRoom(lecturePin);
        String lectureName = room.getLectureName().replaceAll(" ", "_");
        fileChooser.setInitialFileName(lectureName + "_export");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );

        try {
            // Create the file named by the user.
            File export = fileChooser.showSaveDialog(stage);
            fileChooser.setInitialDirectory(export.getParentFile());

            // Write content to the file and open it afterwards.
            ServerCommunication.exportRoom(export, lecturePin);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(export);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoom(LectureRoom room) {
        ArchiveFormatComponent archiveFormatComponent = new ArchiveFormatComponent(room, user);
        stack.getChildren().add(archiveFormatComponent);
    }


    public void setUsers(Users user) {
        this.user = user;
    }


    /**
     * Loads the list with pins if user is currently in archiveView.
     * Else the lobby for the lecturer will be loaded.
     * @throws IOException if the fxml page cannot be loaded
     */
    public void loadLobbyLecturer() throws IOException {
        if (archiveView.equals("questions")) {
            showPins();
        } else if (archiveView.equals("polls")) {
            showArchive(this.lecturePin);
        } else {
            Display.showLecturer(this.user);
        }
    }

    @FXML
    public void showButtons(boolean show) {
        writeArchive.setVisible(show);
        showPolls.setVisible(show);
    }
}
