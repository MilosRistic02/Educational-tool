package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;

public class QuestionFormatComponent extends Pane {

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
}
