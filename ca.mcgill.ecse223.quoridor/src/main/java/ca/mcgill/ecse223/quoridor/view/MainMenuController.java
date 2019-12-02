package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.MusicController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Window;

public class MainMenuController extends ViewController{

    public Button Button12;
    public void handleNewGame(ActionEvent actionEvent) {
        MusicController.playChooseYourChar();
        changePage("/fxml/SelectPlayerName.fxml");
    }

    public void handleLoadGame(ActionEvent actionEvent) {
        MusicController.playChooseYourChar();
        changePage("/fxml/LoadGameMenu.fxml");
    }

    public void handleQuitGame(ActionEvent actionEvent) {
        AlertHelper.newPopUpWindow(Alert.AlertType.CONFIRMATION, "Alert", "Are you sure you want to quit the game?");
        MusicController.stopAllMusic();
        // popUpWindow("/fxml/QuitTheGame.fxml");
    }
}
