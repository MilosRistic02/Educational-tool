package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.pages.ArchiveController;
import nl.tudelft.oopp.demo.controllers.pages.LobbyController;
import nl.tudelft.oopp.demo.controllers.pages.QuestionController;
import nl.tudelft.oopp.demo.controllers.pages.QuestionLecturerController;
import nl.tudelft.oopp.demo.controllers.pages.UsersListController;
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
        load("/FXML/login.fxml");
    }

    /** This functions sets the main screen to the login screen.
     *
     * @param users         the current logged user so it is known who votes or asks a question
     * @throws IOException  can throw an exception
     */
    public static void showQuestion(Users users, LectureRoom lectureRoom) throws IOException {
        QuestionController questionController = load("/FXML/question.fxml").getController();
        questionController.init(users, lectureRoom);
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
        QuestionLecturerController questionLecturerController
                = load("/FXML/questionLecturer.fxml").getController();
        questionLecturerController.init(users, lectureRoom);
    }


    /**
     * If the current logged user is a lecturer, redirect it to the
     * lecturer view.
     * @param users - current logged user.
     * @throws IOException - can throw an error.
     */
    public static void showLecturer(Users users) throws IOException {
        LobbyController lobbyController = load("/FXML/lobbyLecturer.fxml").getController();
        lobbyController.setUsers(users);
    }

    /**
     * If the current logged user is a student, redirect it to the
     * student view.
     * @param users - current logged user.
     * @throws IOException - can throw an error.
     */
    public static void showStudent(Users users) throws IOException {
        LobbyController lobbyController = load("/FXML/lobbyStudent.fxml").getController();
        lobbyController.setUsers(users);

        if (!users.getRole().equals("student")) {
            lobbyController.showBackButton();
        }
    }

    /**
     * Redirects the lecturer to the page where he can create a lecture.
     * @param users User who is currently logged in
     * @throws IOException Exception thrown when something goes with IO
     */
    public static void showLobbyCreateRoom(Users users) throws IOException {
        LobbyController lobbyController = load("/FXML/lobbyCreateRoom.fxml").getController();
        lobbyController.setUsers(users);
    }

    /**
     * Redirects the lecturer to the archive page.
     * @param users User who is currently logged in
     * @throws IOException if the fxml page cannot be loaded
     */
    public static void showArchiveList(Users users) throws IOException {
        ArchiveController archiveController = load("/FXML/archiveList.fxml").getController();
        archiveController.setUsers(users);
        archiveController.showPins();
        archiveController.showButtons(false);
    }

    /**
     * Redirects the lecturer to the archived view of a lectureRoom.
     * @param lecturePin - Pin of the lecture that is currently open
     * @throws IOException if the fxml page cannot be loaded
     */
    public static void showArchive(String lecturePin, Users users) throws IOException {
        ArchiveController archiveController = load("/FXML/archiveList.fxml").getController();
        archiveController.setUsers(users);
        archiveController.showArchive(lecturePin);
        archiveController.showButtons(true);
    }

    /**
     * Shows the list of users who are not banned with an option to switch to banned users.
     * @param user A user
     * @throws IOException Input output exception
     */
    public static void showStudentsBanPage(Users user) throws IOException {
        UsersListController usersListController = load("/FXML/usersList.fxml").getController();
        usersListController.setUsers(user);

        usersListController.searchForUser();
    }

    /**
     * Basic loader for the display class.
     * @param path Path to the file we want to load
     * @return Pair with a FXMLLoader and a Parent root
     * @throws IOException Thrown when something goes wrong with IO
     */
    public static FXMLLoader load(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Display.class.getResource(path);
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
