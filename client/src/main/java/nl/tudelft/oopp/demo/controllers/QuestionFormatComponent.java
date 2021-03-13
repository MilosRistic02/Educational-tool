package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;

public class QuestionFormatComponent extends VBox {

    @FXML
    public VBox qanda;

    @FXML
    public Text score;

    @FXML
    public Text question;

    @FXML
    public Text author;

    @FXML
    public Text creationDate;

    @FXML
    public ToggleButton like;

    @FXML
    public ToggleButton dislike;

    @FXML
    public Button delete;

    @FXML
    public Text answer;

    @FXML
    public Text answerHeading;

    @FXML
    public Pane pane;

    @FXML
    public Text isAnswered;


    private Question currentQuestion;
    private Users loggedUser;

    /** constructor for Question Format component.
     *  This method loads an instance of question format and sets the controller to be this file.
     *
     */
    public QuestionFormatComponent() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/questionFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void setLoggedUser(Users loggedUser) {
        this.loggedUser = loggedUser;
    }

    /** The action if the upvote button is toggled.
     *
     */
    @FXML
    public void upvote() {
        ScoringLog scoringLog = null;
        if (like.isSelected()) {
            scoringLog = new ScoringLog(currentQuestion, loggedUser, 1);
        } else {
            scoringLog = new ScoringLog(currentQuestion, loggedUser, 0);
        }
        ServerCommunication.voteQuestion(scoringLog);
    }

    /** The action if the downvote button is toggled.
     *
     */
    @FXML
    public void downvote() {
        ScoringLog scoringLog = null;
        if (dislike.isSelected()) {
            scoringLog = new ScoringLog(currentQuestion, loggedUser, -1);
        } else {
            scoringLog = new ScoringLog(currentQuestion, loggedUser, 0);
        }
        ServerCommunication.voteQuestion(scoringLog);
    }

    @FXML
    public void delete() {
        ServerCommunication.deleteQuestion(Integer.toString((int) currentQuestion.getId()));
    }

    public void setLiked() {
        like.setSelected(true);
        dislike.setSelected(false);
        like.setStyle("-fx-background-color: #f1be3e;");
        dislike.setStyle("-fx-background-color: #FFFFFF;");
    }

    public void setDisliked() {
        dislike.setSelected(true);
        like.setSelected(false);
        dislike.setStyle("-fx-background-color: #c3312f;");
        like.setStyle("-fx-background-color: #FFFFFF;");
    }
}
