package ca.mcgill.ecse223.quoridor.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Window;

public class MainMenuController extends ViewController{

    public Button Button12;
    public void handleNewGame(ActionEvent actionEvent) {

        changePage("/fxml/SelectPlayerName.fxml");
    }

    public void handleLoadGame(ActionEvent actionEvent) {
        changePage("/fxml/LoadGameMenu.fxml");
    }

    public void handleQuitGame(ActionEvent actionEvent) {

        Window page = Button12.getScene().getWindow();
        AlertHelper.newPopUpWindow(Alert.AlertType.CONFIRMATION, page, "Alert", "Are you sure you want to quit the game?");
        // popUpWindow("/fxml/QuitTheGame.fxml");
    }
}
