package nl.tudelft.oopp.demo.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Users;

import java.io.IOException;

public class UserFormatController  extends VBox {

    @FXML
    private Text username;

    @FXML
    private Text email;

    @FXML
    private Button banButton;

    Users user;

    public UserFormatController(Users user) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("/userFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }

        this.user = user;
        username.setText(user.getUsername());
        banButton.setText(user.isBanned() ? "Unban" : "Ban");

    }

    @FXML
    public void ban() {
        if(user.isBanned()) {
            ServerCommunication.unbanUser(user.getUsername());
        }
        else {
            ServerCommunication.banUser(user.getUsername());
        }
    }


}
