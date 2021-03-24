package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class PollFormatComponent extends Pane {
    @FXML
    public Text question;

    @FXML
    public Text creationDate;

    @FXML
    public BarChart pollChart;

    /** constructor for Poll Format component.
     *  This method loads an instance of archive format and sets the controller to be this file.
     *
     */
    public PollFormatComponent() {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("/pollFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }
    }
}
