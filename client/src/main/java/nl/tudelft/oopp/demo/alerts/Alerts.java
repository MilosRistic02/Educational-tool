package nl.tudelft.oopp.demo.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

public class Alerts {

    /**
     * Generic method to create a new error-alert.
     * @param title - title of the new alert.
     * @param content - content text of the new alert.
     */
    public static void alertError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Generic method to create a new information-alert.
     * @param title String with the title of the new alert
     * @param content String with the content of the new alert
     */
    public static void alertInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Generic method to create a new text-input dialog.
     * @param defaultValue - default text for the text field.
     * @param title - title of the new text-input dialog.
     * @param content - content of the new text-input dialog.
     * @return the input of the user.
     */
    public static Optional<String> textInputDialog(
            String defaultValue, String title, String content) {
        TextInputDialog textInputDialog =
                new TextInputDialog(defaultValue);
        textInputDialog.setGraphic(null);
        textInputDialog.setHeaderText(null);
        textInputDialog.setTitle(title);
        textInputDialog.setContentText(content);
        return textInputDialog.showAndWait();
    }

    /**
     * Generic method to create a new poll.
     * @param question Question of this poll
     * @param size Size of this poll
     * @return An Optional character with the answer the user gave
     */
    public static Optional<Character> createPoll(String question, int size) {
        List<Character> choices = new ArrayList<>();
        for (int i = 65; i < 65 + size; i++) {
            choices.add((char) i);
        }

        ChoiceDialog<Character> poll = new ChoiceDialog<>('A', choices);
        poll.setTitle("Poll");
        poll.setHeaderText("");
        poll.setContentText(question);
        return poll.showAndWait();
    }
}
