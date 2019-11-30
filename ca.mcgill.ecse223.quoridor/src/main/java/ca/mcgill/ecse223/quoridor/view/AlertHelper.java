package ca.mcgill.ecse223.quoridor.view;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class AlertHelper {

    public static void showAlert(AlertType alertType, Window page, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.initOwner(page);
        alert.show();
    }

    public static void newPopUpWindow(AlertType alertType, String title, String message) {

        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if(alertType.equals(AlertType.CONFIRMATION)) {
            if (result.get() == ButtonType.OK) {
                Platform.exit();
            }
        }


    }

//    public static void error(AlertType alertType, String title, String message) {
//
//        Alert alert = new Alert(alertType);
//        alert.setHeaderText(null);
//        alert.setTitle(title);
//        alert.setContentText(message);
//
//    }
    }