package nl.tudelft.oopp.demo.controllers.components;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    private DateFormat df;
    private Question currentQuestion;
    private Users loggedUser;

    /** constructor for Question Format component.
     *  This method loads an instance of question format and sets the controller to be this file.
     *
     */
    public QuestionFormatComponent(Question currentQuestion, Users loggedUser) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/FXML/questionFormat.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("could not create instance");
            System.out.println(exception);
        }

        this.currentQuestion = currentQuestion;
        this.loggedUser = loggedUser;
        loadQuestion();
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
        ServerCommunication.voteQuestion(scoringLog, loggedUser.getUsername());
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
        ServerCommunication.voteQuestion(scoringLog, loggedUser.getUsername());
    }

    @FXML
    public void delete() {
        ServerCommunication.deleteQuestion(Integer.toString((int) currentQuestion.getId()),
                loggedUser.getUsername());
    }

    /**
     * Show if the current question was previously liked by
     * the current logged user.
     */
    public void setLiked() {
        like.setSelected(true);
        dislike.setSelected(false);
        like.setStyle("-fx-background-color: #f1be3e;");
        dislike.setStyle("-fx-background-color: #FFFFFF;");
    }

    /**
     * Show if the current question was previously disliked by
     * the current logged user.
     */
    public void setDisliked() {
        dislike.setSelected(true);
        like.setSelected(false);
        dislike.setStyle("-fx-background-color: #c3312f;");
        like.setStyle("-fx-background-color: #FFFFFF;");
    }

    /**
     * Make the answer visible and update its content. It also changes the
     * border of the question to green and the status to "Answered".
     */
    public void setAnswered(String colour, String text) {
        answerHeading.setVisible(true);
        answer.setVisible(true);
        answer.setText(currentQuestion.getAnswer());
        qanda.setStyle("-fx-border-color:  " + colour + ";"
                + "-fx-border-width: 4; -fx-border-radius: 18");
        isAnswered.setText(text);
        isAnswered.setFill(Color.valueOf(colour));
    }


    /**
     * Loads the current question with its corresponding information
     * such as question content, score, author and date. It also
     * loads the answer if it is available. The delete button is only
     * enabled and made visible if the current logged user is the
     * author of the current question.
     */
    public void loadQuestion() {
        if (currentQuestion.getAnswered() == 1) {
            setAnswered("#99d28c", "Answered");
        }
        if (currentQuestion.getAnswered() == 2) {
            setAnswered("#f1be3e", "Answered verbally");
        }
        if (currentQuestion.getAnswered() == 3) {
            setAnswered("#00A6D6", "Typing...");
        }

        question.setText(currentQuestion.getQuestion());
        score.setText(Integer.toString(currentQuestion.getScore()));
        author.setText("By " + currentQuestion.getAuthor());

        // Date format to be displayed.
        String pattern = "HH:mm:ss";
        df = new SimpleDateFormat(pattern);
        String date = df.format(currentQuestion.getCreationDate());
        creationDate.setText(date);

        // Enable the delete button, only for the questions made by the current logged user.
        if (currentQuestion.getAuthor().equals(loggedUser.getUsername())) {
            delete.setDisable(false);
            delete.setVisible(true);
        }
    }
}
