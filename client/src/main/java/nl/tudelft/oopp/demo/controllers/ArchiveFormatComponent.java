package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;

public class ArchiveFormatComponent extends Pane {

    @FXML
    public Text score;

    @FXML
    public Text question;

    @FXML
    public Text author;

    @FXML
    public Text creationDate;

    @FXML
    public Button showArchive;

    private String lecturePin;
    private Users user;

    @FXML
    public void showArchive() throws IOException {
        Display.showArchive(this.lecturePin, this.user);
    }

    /** constructor for Archive Format component.
     *  This method loads an instance of archive format and sets the controller to be this file.
     *
     */
    public ArchiveFormatComponent() {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("/archiveListFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }
    }

    public void setCurrentPin(String lecturePin) {
        this.lecturePin = lecturePin;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}