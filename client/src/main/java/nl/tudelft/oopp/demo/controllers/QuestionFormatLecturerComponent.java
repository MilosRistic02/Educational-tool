package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;

import java.io.IOException;
import java.util.Optional;

public class QuestionFormatLecturerComponent extends VBox {

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
    public Button editQuestion;

    @FXML
    public Button makeAnswer;

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
    public QuestionFormatLecturerComponent() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/questionFormatLecturer.fxml"));
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

    @FXML
    public void makeAnswer() {
        // display the last answer.
        String oldAnswer = "your answer";
        if (currentQuestion.isAnswered()) {
            oldAnswer = currentQuestion.getAnswer();
        }

        // Dialog to input the answer.
        TextInputDialog textInputDialog = new TextInputDialog(oldAnswer);
        textInputDialog.setGraphic(null);
        textInputDialog.setHeaderText(null);
        textInputDialog.setTitle("Answer");
        textInputDialog.setContentText("Enter an answer for the question:");

        Optional<String> result = textInputDialog.showAndWait();
        String input = textInputDialog.getEditor().getText();
        // Update only if there is a change and the answer is non blank.
        if (result.isPresent() && !input.isEmpty() && !input.equals(oldAnswer)) {
            currentQuestion.setAnswer(result.get());
            currentQuestion.setAnswered(true);
            ServerCommunication.updateAnswerQuestion(currentQuestion);
        }
    }

    @FXML
    public void delete() {
        ServerCommunication.deleteQuestion(Integer.toString((int) currentQuestion.getId()));
    }
}
