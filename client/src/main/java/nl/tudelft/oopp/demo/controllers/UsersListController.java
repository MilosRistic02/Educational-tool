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
import java.util.ArrayList;
import java.util.List;

public class UsersListController {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox stack;

    @FXML
    private TextField enterUserField;

    @FXML
    private Label userListEmptyText;

    @FXML
    private Button switchViewButton;

    private Users user;

    private boolean view;

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

        if(search == null || search.length() == 0){
            if(!view){
                makeList(ServerCommunication.getAllNotBannedStudents());
            }else{
                makeList(ServerCommunication.getAllBannedStudents());
            }
        }else{
            makeList(ServerCommunication.getBySubstring(search, view));
        }

    }

    public void switchView() throws JsonProcessingException {
        view = !view;
        enterUserField.setText("");
        if(!view){
            makeList(ServerCommunication.getAllNotBannedStudents());
        }else{
            makeList(ServerCommunication.getAllBannedStudents());
        }

        switchViewButton.setText(view ? "Unbanned Users" : "Banned Users");
    }

    public void makeList(List<Users> userList){
        stack.getChildren().clear();
        stack.setSpacing(20);

        if(userList.size()>0) {
            userListEmptyText.setVisible(false);
            for (Users u : userList) {
                stack.getChildren().add(new UserFormatComponent(u));
            }
        }else{
            userListEmptyText.setVisible(true);
        }


    }
}
