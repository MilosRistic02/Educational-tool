package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.views.Display;

import java.io.IOException;
import java.util.List;

public class UsersListController {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox stack;

    @FXML
    private Label emptyArchive;

    @FXML
    private TextField enterUserField;


    private Users user;
    private List<Users> usersList;
    private String lecturePin;

    public void setUsers(Users users) {
        user = users;
    }


    /**
     * Loads the list with pins if user is currently in archiveView.
     * Else the lobby for the lecturer will be loaded.
     * @throws IOException if the fxml page cannot be loaded
     */
    public void loadLobbyLecturer() throws IOException {
        Display.showLecturer(this.user);
    }


    /**
     * Method called when a letter is typed in the search bar or the search icon is clicked.
     * Adds all found rooms to the Vbox.
     */
    @FXML
    public void searchForUser() throws JsonProcessingException {
        String search = enterUserField.getText().toLowerCase();
        stack.getChildren().clear();
        stack.setSpacing(20);
        List<Users> userList = ServerCommunication.getBySubstring(search);

        for (Users q : userList) {
            stack.getChildren().add( new UserFormatController(q));
        }
    }
}
