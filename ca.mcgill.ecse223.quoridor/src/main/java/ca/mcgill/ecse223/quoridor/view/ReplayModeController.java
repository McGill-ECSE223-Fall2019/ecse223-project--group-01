package ca.mcgill.ecse223.quoridor.view;

import javafx.event.ActionEvent;

public class ReplayModeController extends ViewController{
    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }
}
