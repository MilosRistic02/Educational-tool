package nl.tudelft.oopp.demo.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.demo.entities.Users;

import java.io.IOException;

public class UserFormatController  extends VBox {

    @FXML
    private Label username;

    @FXML
    private Label email;

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
    }


}
