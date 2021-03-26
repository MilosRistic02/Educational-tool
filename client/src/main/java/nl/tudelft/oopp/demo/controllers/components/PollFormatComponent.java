package nl.tudelft.oopp.demo.controllers.components;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.entities.Poll;

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
    public PollFormatComponent(Poll poll) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass().getResource("/FXML/pollFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }
        creationDate.setText(String.valueOf(poll.getCreationDate()));
        question.setText(poll.getQuestion());
        BarChart pollChart = this.pollChart;
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
            for (Character c : poll.getRightAnswer()) {
                pollChart.lookup(".data" + (c - 65)
                        + ".chart-bar").setStyle("-fx-bar-fill: green");
            }
        }
    }
}
