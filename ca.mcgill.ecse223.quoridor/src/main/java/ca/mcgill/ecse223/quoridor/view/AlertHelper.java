package ca.mcgill.ecse223.quoridor.view;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

public class AlertHelper {

    public static void showAlert(AlertType alertType, Window page, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.initOwner(page);
        alert.show();
    }
}
