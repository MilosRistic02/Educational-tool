package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
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
     * Method called when a letter is typed in the search bar or the search icon is clicked.
     * Adds all found rooms to the Vbox.
     */
    @FXML
    public void searchCourse() {
        String course = searchBar.getText().toLowerCase();
        stack.getChildren().clear();
        stack.setSpacing(20);

        for (LectureRoom room : this.rooms) {
            if (room.getCourseId().toLowerCase().contains(course)) {
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
        stack.getChildren().clear();
        stack.setSpacing(20);

        List<Question> questions = ServerCommunication.getAllQuestion(lecturePin);

        if (questions.isEmpty()) {
            emptyArchive.setVisible(true);
            emptyArchive.setText("Archive is empty!");
        }

        Collections.sort(questions, new QuestionComparator());
        for (Question q : questions) {
            QuestionFormatLecturerComponent questionFormat =
                    new QuestionFormatLecturerComponent(q, user);

            stack.getChildren().add(questionFormat);
        }
    }

    @FXML
    private void showPolls() {
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
                PollFormatComponent pollFormatComponent = new PollFormatComponent();
                pollFormatComponent.creationDate.setText(String.valueOf(poll.getCreationDate()));
                pollFormatComponent.question.setText(poll.getQuestion());
                BarChart pollChart = pollFormatComponent.pollChart;
                int size = poll.getSize();
                int[] results = poll.getVotes();

                XYChart.Series set1 = new XYChart.Series<>();

                for (int i = 0; i < size; i++) {
                    set1.getData().add(
                            new XYChart.Data(Character.toString((char) (i + 65)), results[i]));
                }
                if (!poll.isOpen()) {
                    pollChart.getData().clear();
                    pollChart.getData().addAll(set1);
                    pollChart.lookup(".data" + (poll.getRightAnswer() - 65)
                            + ".chart-bar").setStyle("-fx-bar-fill: green");
                    pollChart.setAnimated(false);
                }
                stack.getChildren().add(pollFormatComponent);
            }
        }
    }

    /**
     * Exports an archived room to a text file.
     */
    @FXML
    private void writeArchive() {
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

            // Write content to the file.
            String response = ServerCommunication.exportRoom(export, lecturePin);
            export = new ObjectMapper().readValue(response, File.class);

            Alerts.alertInfo("Export succesfull",
                    "Succesfully exported all questions of " + lecturePin);

        } catch (Exception e) {
            e.printStackTrace();
            //Alerts.alertError("Exporting failed", "Oops. Something went wrong! Try again later.");
        }

    }

    private void addRoom(LectureRoom room) {
        ArchiveFormatComponent archiveFormatComponent = new ArchiveFormatComponent();

        archiveFormatComponent.setCurrentPin(room.getLecturePin());
        archiveFormatComponent.setUser(user);

        archiveFormatComponent.lectureNameText.setText(room.getLectureName());
        archiveFormatComponent.lecturePinText.setText("Pin: " + room.getLecturePin());
        archiveFormatComponent.creationDate.setText(room.getCreationDate().toString());
        archiveFormatComponent.courseId.setText(String.valueOf(room.getCourseId()));

        stack.getChildren().add(archiveFormatComponent);
    }


    public void setUsers(Users user) {
        this.user = user;
    }

    /**
     * Displays all of the rooms closed by the lecturer in the archive.
     */
    public void showPins() {
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

        Collections.sort(rooms, new RoomComparator());
        for (LectureRoom room : rooms) {
            addRoom(room);
        }
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
