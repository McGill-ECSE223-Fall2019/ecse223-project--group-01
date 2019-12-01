package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.*;

public class EndSceneController extends ViewController{

    @FXML
    public Text winnerPlayer;
    public AnchorPane anchorpane;

    public void initialize() {
        String winnerName = ModelQuery.getCurrentGame().getWinningPlayer().getUser().getName();
        winnerPlayer.setText(winnerName + " Wins!");
        winnerPlayer.setTextAlignment(TextAlignment.CENTER);
        winnerPlayer.setX(anchorpane.getWidth()/2);
        winnerPlayer.setY(3 * anchorpane.getHeight()/4);

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
