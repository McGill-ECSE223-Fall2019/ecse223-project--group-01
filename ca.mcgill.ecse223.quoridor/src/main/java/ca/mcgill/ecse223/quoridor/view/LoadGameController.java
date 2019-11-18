package ca.mcgill.ecse223.quoridor.view;
import javafx.event.ActionEvent;


public class LoadGameController extends ViewController{

    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }

    public void handleContinueGame(ActionEvent actionEvent) {
        changePage("/fxml/ContinueGame.fxml");
    }

    public void handleReplayMode(ActionEvent actionEvent) {
        changePage("/fxml/ReplayMode.fxml");
    }
}
