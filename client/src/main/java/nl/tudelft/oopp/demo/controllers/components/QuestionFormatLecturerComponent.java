package nl.tudelft.oopp.demo.controllers.components;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    @FXML
    public Button verbal;

    @FXML
    public Button banButton;

    private Question currentQuestion;
    private Users loggedUser;

    /** constructor for Question Format component.
     *  This method loads an instance of question format and sets the controller to be this file.
     *
     */
    public QuestionFormatLecturerComponent(Question currentQuestion, Users loggedUser) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/FXML/questionFormatLecturer.fxml"));
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
        if (currentQuestion.getAnswered() >= 1) {
            oldAnswer = currentQuestion.getAnswer();
        }
        // Dialog to input the answer.
        Optional<String> result = Alerts.textInputDialog(oldAnswer,
                "Answer",
                "Enter an answer for this question: ");

        // Update only if there is a change and the answer is non blank.
        if (!result.isEmpty() && !result.get().equals(oldAnswer)) {
            if (result.get().length() > 512) {
                Alerts.alertError("Answer too long",
                        "Answer too long, can only be 512 characters");
            } else {
                currentQuestion.setAnswer(result.get());
                currentQuestion.setAnswered(1);
                ServerCommunication.updateAnswerQuestion(currentQuestion, loggedUser.getUsername());
                answer.setText(result.get());
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
                Alerts.alertError("Question too long",
                        "Question is too long, can only be 255 characters");
            } else {
                currentQuestion.setQuestion(result.get());
                ServerCommunication.updateContentQuestion(
                        currentQuestion, loggedUser.getUsername());
            }
        }
    }

    /**
     * Make the answer visible and update its content. It also changes the
     * border of the question to green and the status to "Answered". Once a
     * question has been answered, the "answer" button changes to "update
     * answer".
     */
    public void setAnswered(String colour, String text) {
        answerHeading.setVisible(true);
        answer.setVisible(true);
        answer.setText(currentQuestion.getAnswer());
        qanda.setStyle("-fx-border-color:  " + colour + ";"
                        + "-fx-border-width: 4; -fx-border-radius: 18");
        isAnswered.setText(text);
        isAnswered.setFill(Color.valueOf(colour));
        makeAnswer.setText("Change Answer");
        makeAnswer.setStyle("-fx-background-color: " + colour);
        verbal.setVisible(false);
        verbal.setDisable(true);
    }

    /**
     * Loads the current question with its corresponding information
     * such as question content, score, author and date. It also
     * loads the answer if it is available.
     */
    public void loadQuestion() {
        if (currentQuestion.getAnswered() == 1) {
            setAnswered("#99d28c", "Answered");
        }
        if (currentQuestion.getAnswered() == 2) {
            setAnswered("#f1be3e", "Answered verbally");
        }

        question.setText(currentQuestion.getQuestion());
        score.setText(Integer.toString(currentQuestion.getScore()));
        author.setText("By " + currentQuestion.getAuthor());

        // Date format to be displayed.
        String pattern = "HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String date = df.format(currentQuestion.getCreationDate());
        creationDate.setText(date);
    }

    @FXML
    public void delete() {
        ServerCommunication.deleteQuestion(Integer.toString((int) currentQuestion.getId()),
                loggedUser.getUsername());
    }

    @FXML
    public void answeredVerbal() {
        currentQuestion.setAnswered(2);
        ServerCommunication.updateAnswerQuestion(currentQuestion, loggedUser.getUsername());
    }

    public void banUser() {
        ServerCommunication.banUser(currentQuestion.getAuthor(), loggedUser.getUsername());
        delete();
    }
}
