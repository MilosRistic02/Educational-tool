package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;

import java.io.IOException;

public class QuestionFormatComponent extends Pane{

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

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void setLoggedUser(Users loggedUser) {
        this.loggedUser = loggedUser;
    }

    @FXML
    public void upvote() {
        ScoringLog scoringLog = null;
        dislike.setSelected(false);

        if (like.isSelected())
            scoringLog = new ScoringLog(currentQuestion, loggedUser, 1);
        else
            scoringLog = new ScoringLog(currentQuestion, loggedUser, 0);

        ServerCommunication.voteQuestion(scoringLog);
    }

    @FXML
    public void downvote() {
        ScoringLog scoringLog = null;
        like.setSelected(false);

        if (dislike.isSelected())
            scoringLog = new ScoringLog(currentQuestion, loggedUser, -1);
        else
            scoringLog = new ScoringLog(currentQuestion, loggedUser, 0);

        ServerCommunication.voteQuestion(scoringLog);
    }
}
