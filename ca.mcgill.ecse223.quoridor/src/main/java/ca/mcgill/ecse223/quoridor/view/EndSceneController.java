package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.*;

public class EndSceneController extends ViewController{

    @FXML
    public Text winnerPlayer;

    public void initialize() {
        String winnerName = ModelQuery.getCurrentGame().getWinningPlayer().getUser().getName();
        winnerPlayer.setText(winnerName);
        winnerPlayer.setTextAlignment(TextAlignment.CENTER);

    }

    @FXML
    public void handleSaveGame (ActionEvent actionEvent) {

    }

    public void handlePlayAgain (ActionEvent actionEvent) {

    }

    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }

}
