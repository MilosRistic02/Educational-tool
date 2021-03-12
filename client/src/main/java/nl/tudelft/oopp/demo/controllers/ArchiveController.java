package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Users;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ArchiveController {

    @FXML
    private VBox stack;

    private Users user;

    private List<LectureRoom> rooms;

    @FXML
    public void showArchive(String lecturePin) {
        stack.getChildren().clear();
        stack.setSpacing(20);

        LectureRoom room = ServerCommunication.getLectureRoom(lecturePin);
        List<Question> questions = ServerCommunication.getAllQuestion(lecturePin);
        for (Question q: questions) {
            System.out.println(q.toString());
        }
        for (Question q : questions) {
            QuestionFormatComponent questionFormatComponent = new QuestionFormatComponent();
            questionFormatComponent.question.setText(q.getQuestion());
            questionFormatComponent.score.setText(Integer.toString(q.getScore()));
            questionFormatComponent.author.setText("By " + q.getAuthor());

            String pattern = "HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String date = df.format(q.getCreationDate());
            questionFormatComponent.creationDate.setText(date);



//            ArchiveFormatComponent archiveFormatComponent = new ArchiveFormatComponent();
//
//            archiveFormatComponent.question.setText(q.getQuestion());
//            archiveFormatComponent.creationDate.setText(Integer.toString(q.getScore()));
//            archiveFormatComponent.author.setText("By " + q.getAuthor());

            stack.getChildren().add(questionFormatComponent);
        }

    }

    public void setUsers(Users user) {
        this.user = user;
    }

    public void showPins() {
        this.rooms =  ServerCommunication.getClosedLecturePins(this.user.getUsername());

        stack.getChildren().clear();
        stack.setSpacing(20);

        for (LectureRoom room : rooms) {
//            QuestionFormatComponent questionFormatComponent = new QuestionFormatComponent();
//            questionFormatComponent.question.setText(room.getLecturePin());
//            questionFormatComponent.creationDate.setText(room.getCreationDate().toString());
//            questionFormatComponent.author.setText(String.valueOf(room.getCourseId()));



            ArchiveFormatComponent archiveFormatComponent = new ArchiveFormatComponent();

            archiveFormatComponent.setCurrentPin(room.getLecturePin());

            archiveFormatComponent.question.setText(room.getLecturePin());
            archiveFormatComponent.creationDate.setText(room.getCreationDate().toString());
            archiveFormatComponent.author.setText(String.valueOf(room.getCourseId()));

            stack.getChildren().add(archiveFormatComponent);
        }

    }
}
