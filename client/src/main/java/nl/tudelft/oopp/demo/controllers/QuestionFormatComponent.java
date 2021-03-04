package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class QuestionFormatComponent extends Pane{

    @FXML
    public Text score;

    @FXML
    public Text question;

    @FXML
    public Text author;


    public QuestionFormatComponent(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/questionFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        }
        catch (IOException exception){

        }
    }

}
