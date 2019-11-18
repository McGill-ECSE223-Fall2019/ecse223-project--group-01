package ca.mcgill.ecse223.quoridor.view;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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

    public static void newPopUpWindow(AlertType alertType,Window page, String title, String message) {
        Stage window = new Stage();
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.initOwner(page);
        alert.show();

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> window.close());
        Button okButton = new Button("Ok");
        okButton.setOnAction(e ->
                System.exit(1));
    }
}
