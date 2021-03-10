package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.LobbyController;
import nl.tudelft.oopp.demo.controllers.QuestionController;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Users;

public class Display extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        showLogin();
    }

    /** This functions sets the main screen to the login screen.
     *
     * @throws IOException  can throw an error
     */
    public static void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource("/login.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /** This functions sets the main screen to the login screen.
     *
     * @param users         the current logged user so it is known who votes or asks a question
     * @throws IOException  can throw an exception
     */
    public static void showQuestion(Users users, LectureRoom lectureRoom) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource("/question.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        QuestionController questionController = loader.getController();
        questionController.setUsers(users);
        questionController.setLectureRoom(lectureRoom);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * If the current logged user is a lecturer, redirect it to the
     * lecturer view.
     * @param users - current logged user.
     * @throws IOException - can throw an error.
     */
    public static void showLecturer(Users users) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource("/lobbyLecturer.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        LobbyController lobbyController = loader.getController();
        lobbyController.setUsers(users);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * If the current logged user is a student, redirect it to the
     * student view.
     * @param users - current logged user.
     * @throws IOException - can throw an error.
     */
    public static void showStudent(Users users) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource("/lobbyStudent.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        LobbyController lobbyController = loader.getController();
        lobbyController.setUsers(users);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
