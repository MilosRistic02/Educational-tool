package nl.tudelft.oopp.demo.alerts;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class Alerts {

    /**
     * Generic method to create a new alert.
     * @param title - title of the new alert.
     * @param content - content text of the new alert.
     */
    public static void alertInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Too many rooms");
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
}
