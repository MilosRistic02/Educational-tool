package nl.tudelft.oopp.demo.views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.demo.controllers.QuestionController;
import nl.tudelft.oopp.demo.entities.Users;

public class QuoteDisplay extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        showLogin();
    }

    public static void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = QuoteDisplay.class.getResource("/login.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showQuestion(Users users) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = QuoteDisplay.class.getResource("/question.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        QuestionController questionController = loader.getController();
        questionController.setUsers(users);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
