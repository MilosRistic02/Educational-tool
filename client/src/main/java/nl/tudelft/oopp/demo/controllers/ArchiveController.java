package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
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
    private Text title;

    private Users user;

    private List<LectureRoom> rooms;

    private boolean archiveView;


    /**
     * Opens a closed lecture in the archive view.
     * @param lecturePin - Pin of the lecture that is displayed in archive view
     */
    @FXML
    public void showArchive(String lecturePin) {
        archiveView = true;
        title.setText("Archive of room " + lecturePin);
        stack.getChildren().clear();
        stack.setSpacing(20);

        List<Question> questions = ServerCommunication.getAllQuestion(lecturePin);



        if (questions.isEmpty()) {
            emptyArchive.setVisible(true);
        }

        Collections.sort(questions, new QuestionComparator());
        for (Question q : questions) {
            QuestionFormatLecturerComponent questionFormat =
                    new QuestionFormatLecturerComponent(q, user);

            stack.getChildren().add(questionFormat);
        }
    }

    public void setUsers(Users user) {
        this.user = user;
    }

    /**
     * Displays all of the rooms closed by the lecturer in the archive.
     */
    public void showPins() {
        //emptyArchive.setVisible(false);
        archiveView = false;
        title.setText("Archive");
        this.rooms =  ServerCommunication.getClosedLecturePins(this.user.getUsername());

        stack.getChildren().clear();
        stack.setSpacing(20);

        Collections.sort(rooms, new RoomComparator());
        for (LectureRoom room : rooms) {
            ArchiveFormatComponent archiveFormatComponent = new ArchiveFormatComponent();

            archiveFormatComponent.setCurrentPin(room.getLecturePin());
            archiveFormatComponent.setUser(user);

            archiveFormatComponent.question.setText(room.getLecturePin());
            archiveFormatComponent.creationDate.setText(room.getCreationDate().toString());
            archiveFormatComponent.author.setText(String.valueOf(room.getCourseId()));

            stack.getChildren().add(archiveFormatComponent);
        }
    }

    /**
     * Loads the list with pins if user is currently in archiveView.
     * Else the lobby for the lecturer will be loaded.
     * @throws IOException if the fxml page cannot be loaded
     */
    public void loadLobbyLecturer() throws IOException {
        if (archiveView) {
            showPins();
        } else {
            Display.showLecturer(this.user);
        }

    }
}
