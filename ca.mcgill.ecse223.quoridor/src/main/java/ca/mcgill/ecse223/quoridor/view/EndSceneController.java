package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.SaveLoadGameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
        String filename;
        TextInputDialog textInput = new TextInputDialog();

        textInput.setTitle("Saving game");
        textInput.getDialogPane().setContentText("Name of save file");

        TextField input = textInput.getEditor();
        textInput.showAndWait();

        if(input.getText() != null && input.getText().length() != 0) {
            filename = input.getText();

            if (!SaveLoadGameController.fileSave(filename +".mov")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                if (!SaveLoadGameController.isSaveMoveValid)
                    errorAlert.setContentText("The current game save is invalid");
                else
                    errorAlert.setContentText("There was an error in saving your game");
                errorAlert.setHeaderText("Error in loading Game");
                errorAlert.showAndWait();
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setContentText("Game is successfully saved in: " +filename +".mov");
                successAlert.showAndWait();
            }
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Missing file name");
        }

    }

    public void handlePlayAgain (ActionEvent actionEvent) {
        changePage("/fxml/SelectPlayerName.fxml");
    }

    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }

}
