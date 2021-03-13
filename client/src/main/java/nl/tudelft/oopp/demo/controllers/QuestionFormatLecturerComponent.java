package nl.tudelft.oopp.demo.controllers;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.demo.alerts.Alerts;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Users;

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/questionFormatLecturer.fxml"));
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

    /**
     * When clicking the "answer" button of a particular question,
     * a dialog allows the lecturer/moderator to type the desired answer to
     * the question. The current question is updated in the database with the
     * provided answer.
     */
    @FXML
    public void makeAnswer() {
        // display the last answer.
        String oldAnswer = "your answer";
        if (currentQuestion.isAnswered()) {
            oldAnswer = currentQuestion.getAnswer();
        }
        // Dialog to input the answer.
        Optional<String> result = Alerts.textInputDialog(oldAnswer,
                "Answer",
                "Enter an answer for this question: ");

        // Update only if there is a change and the answer is non blank.
        if (!result.isEmpty() && !result.get().equals(oldAnswer)) {
            if(result.get().length() > 512) {
                Alerts.alertInfo("Answer too long",
                        "Answer too long, can only be 512 characters");
            } else {
                currentQuestion.setAnswer(result.get());
                currentQuestion.setAnswered(true);
                ServerCommunication.updateAnswerQuestion(currentQuestion);
            }
        }
    }

    /**
     * When clicking the "pencil" button of a particular question, a
     * dialog allows the lecturer/moderator to edit the content of the
     * question. The changes made to the question are updated in the
     * database.
     */
    @FXML
    public void editQuestion() {
        String oldQuestion = currentQuestion.getQuestion();
        // Dialog to input the answer.
        Optional<String> result = Alerts.textInputDialog(oldQuestion,
                "Edit question",
                "Enter updated question:");

        // Update only if there is a change and the question is non blank.
        if (!result.isEmpty() && !result.get().equals(oldQuestion)) {
            if (result.get().length() > 255) {
                Alerts.alertInfo("Question too long",
                        "Question is too long, can only be 255 characters");
            } else {
                currentQuestion.setQuestion(result.get());
                ServerCommunication.updateContentQuestion(currentQuestion);
            }
        }
    }

    @FXML
    public void delete() {
        ServerCommunication.deleteQuestion(Integer.toString((int) currentQuestion.getId()));
    }
}
