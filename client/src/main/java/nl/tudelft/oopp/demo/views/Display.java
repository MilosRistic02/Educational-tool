package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import nl.tudelft.oopp.demo.controllers.*;
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
        Pair<FXMLLoader, Parent> recourse = load("/login.fxml");

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();
    }

    /** This functions sets the main screen to the login screen.
     *
     * @param users         the current logged user so it is known who votes or asks a question
     * @throws IOException  can throw an exception
     */
    public static void showQuestion(Users users, LectureRoom lectureRoom) throws IOException {
        Pair<FXMLLoader, Parent> recourse = load("/question.fxml");

        QuestionController questionController = recourse.getKey().getController();
        questionController.init(users, lectureRoom);

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();
    }

    /**
     * If the current logged user is a lecturer and he/she wants
     * to join a existing room, then show the room with the lecturer
     * view.
     * @param users - current logged user.
     * @param lectureRoom - lecture room the lecturer wants to join.
     * @throws IOException - can throw an error.
     */
    public static void showQuestionLecturer(
            Users users, LectureRoom lectureRoom) throws IOException {
        Pair<FXMLLoader, Parent> recourse = load("/questionLecturer.fxml");

        QuestionLecturerController questionLecturerController = recourse.getKey().getController();
        questionLecturerController.init(users, lectureRoom);

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();
    }


    /**
     * If the current logged user is a lecturer, redirect it to the
     * lecturer view.
     * @param users - current logged user.
     * @throws IOException - can throw an error.
     */
    public static void showLecturer(Users users) throws IOException {
        Pair<FXMLLoader, Parent> recourse = load("/lobbyLecturer.fxml");

        LobbyController lobbyController = recourse.getKey().getController();
        lobbyController.setUsers(users);

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();
    }

    /**
     * If the current logged user is a student, redirect it to the
     * student view.
     * @param users - current logged user.
     * @throws IOException - can throw an error.
     */
    public static void showStudent(Users users) throws IOException {
        Pair<FXMLLoader, Parent> recourse = load("/lobbyStudent.fxml");

        LobbyController lobbyController = recourse.getKey().getController();
        lobbyController.setUsers(users);

        if (users.getRole().equals("lecturer")) {
            lobbyController.showBackButton();
        }

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();
    }

    /**
     * Redirects the lecturer to the page where he can create a lecture.
     * @param users User who is currently logged in
     * @throws IOException Exception thrown when something goes with IO
     */
    public static void showLobbyCreateRoom(Users users) throws IOException {
        Pair<FXMLLoader, Parent> recourse = load("/lobbyCreateRoom.fxml");

        LobbyController lobbyController = recourse.getKey().getController();
        lobbyController.setUsers(users);

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();
    }

    /**
     * Redirects the lecturer to the archive page.
     * @param users User who is currently logged in
     * @throws IOException if the fxml page cannot be loaded
     */
    public static void showArchiveList(Users users) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource("/archiveList.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        ArchiveController archiveController = loader.getController();
        archiveController.setUsers(users);
        archiveController.showPins();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Redirects the lecturer to the archived view of a lectureRoom.
     * @param lecturePin - Pin of the lecture that is currently open
     * @throws IOException if the fxml page cannot be loaded
     */
    public static void showArchive(String lecturePin, Users users) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource("/archiveList.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        ArchiveController archiveController = loader.getController();
        archiveController.setUsers(users);
        archiveController.showArchive(lecturePin);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showStudentsBanPage(Users users) throws IOException {
        Pair<FXMLLoader, Parent> recourse = load("/usersList.fxml");

        UsersListController usersListController = recourse.getKey().getController();
        usersListController.setUsers(users);

        primaryStage.setScene(new Scene(recourse.getValue()));
        primaryStage.show();

        usersListController.searchForUser();
    }

    /**
     * Basic loader for the display class.
     * @param path Path to the file we want to load
     * @return Pair with a FXMLLoader and a Parent root
     * @throws IOException Thrown when something goes wrong with IO
     */
    public static Pair<FXMLLoader, Parent> load(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource(path);
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        return new Pair(loader, root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
