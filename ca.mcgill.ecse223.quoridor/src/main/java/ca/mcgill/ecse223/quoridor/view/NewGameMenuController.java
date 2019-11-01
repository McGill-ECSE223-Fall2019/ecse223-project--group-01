package ca.mcgill.ecse223.quoridor.view;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;


public class NewGameMenuController extends ViewController{

    @FXML
    public TextField whitePlayerName;
    public TextField blackPlayerName;
    public TextField minutes;
    public TextField seconds;
    public void handleWhitePlayerName(ActionEvent actionEvent) {
        try {

        } catch (Exception e ) {

        }

        //LOAD NAME LIST IN LOAD GAME CONTROLLER
    }

    public void backToMainMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }
    public void handleInitializeBoard(ActionEvent actionEvent){
        changePage("/fxml/InitializeBoard.fxml");
    }
}
