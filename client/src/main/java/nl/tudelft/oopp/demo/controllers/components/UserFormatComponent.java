package nl.tudelft.oopp.demo.controllers.components;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Users;


public class UserFormatComponent  extends VBox {

    @FXML
    private Text username;

    @FXML
    private Button banButton;

    Users user;

    Users loggedUser;

    /** constructor for UserFormatComponent Format component.
     *  This method loads an instance of question format and sets the controller to be this file.
     *
     */

    public UserFormatComponent(Users user, Users loggedUser) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("/FXML/userFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }

        this.user = user;
        this.loggedUser = loggedUser;
        username.setText(user.getUsername());
        banButton.setText(user.isBanned() ? "Unban" : "Ban");
    }

    /**
     * Bans or unbans a user, triggered when ban/unban button is clicked.
     */
    @FXML
    public void ban() {
        if (user.isBanned()) {
            ServerCommunication.unbanUser(user.getUsername(), loggedUser.getUsername());
            banButton.setText("Ban");
            user.setBanned(false);
        } else {
            ServerCommunication.banUser(user.getUsername(), loggedUser.getUsername());
            banButton.setText("Unban");
            user.setBanned(true);
        }
    }
}
