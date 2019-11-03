package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class MainMenuController extends ViewController{

    public void handleNewGame(ActionEvent actionEvent) {

        changePage("/fxml/SelectPlayerName.fxml");
    }

    public void handleLoadGame(ActionEvent actionEvent) {
        changePage("/fxml/LoadGameMenu.fxml");
    }

    public void handleQuitGame(ActionEvent actionEvent) {
        popUpWindow("/fxml/QuitTheGame.fxml");
    }
}
