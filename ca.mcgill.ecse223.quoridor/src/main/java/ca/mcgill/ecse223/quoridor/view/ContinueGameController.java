package ca.mcgill.ecse223.quoridor.view;

import javafx.event.ActionEvent;

public class ContinueGameController extends ViewController {
    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }
}
