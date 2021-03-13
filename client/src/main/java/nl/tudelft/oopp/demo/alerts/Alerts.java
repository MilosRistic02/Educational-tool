package nl.tudelft.oopp.demo.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Alerts {

    public static void alertInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Too many rooms");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Optional<String> textInputDialog(String defaultValue, String title, String content) {
        TextInputDialog textInputDialog = new TextInputDialog(defaultValue);
        textInputDialog.setGraphic(null);
        textInputDialog.setHeaderText(null);
        textInputDialog.setTitle(title);
        textInputDialog.setContentText(content);
        return textInputDialog.showAndWait();
    }
}
