package nl.tudelft.oopp.demo.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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
     * Generic method to create a new information-alert with copyable content.
     * @param title String with the title of the new alert
     * @param content String with the content of the new alert
     * @param width int with de width of the content
     * @param height int with de height of the content
     */
    public static void alertInfoCopyAble(String title, String content, int width, int height) {
        TextArea textArea = new TextArea(content);
        textArea.setMaxSize(width, height);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxSize(width, height);
        gridPane.add(textArea, 0, 0);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
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
     * Generic method to create a new number-input dialog.
     * @param defaultValue - default text for the text field.
     * @param title - title of the new text-input dialog.
     * @param content - content of the new text-input dialog.
     * @param errorText - error to show if input is not a positive number.
     * @return the input of the user.
     */
    public static Optional<Integer> numberInputDialog(
            String defaultValue, String title, String content, String errorText) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(title);
        ButtonType submit = new ButtonType("Set frequency", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().add(submit);
        Text error = new Text();
        error.setText(errorText);
        error.setStyle("-fx-fill: #c3312f");
        error.setVisible(false);
        TextField inputSeconds = new TextField(defaultValue);
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20);
        gridPane.add(error, 0, 0, 2, 1);
        gridPane.add(inputSeconds, 1, 1);
        Label contentLabel = new Label(content);
        gridPane.add(contentLabel, 0, 1);
        dialog.getDialogPane().setContent(gridPane);
        inputSeconds.textProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                if (value < 0) {
                    throw new Exception();
                } else {
                    error.setVisible(false);
                    dialog.getDialogPane().lookupButton(submit).setDisable(false);
                }
            } catch (Exception e) {
                error.setVisible(true);
                dialog.getDialogPane().lookupButton(submit).setDisable(true);
            }
        }));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submit) {
                return Integer.parseInt(inputSeconds.getText());
            } else {
                return null;
            }
        });
        return  dialog.showAndWait();
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
