package ca.mcgill.ecse223.quoridor.view;
import javafx.event.ActionEvent;
import javafx.application.Platform;

public class QuitTheGameController extends ViewController{

        public void handleBackToTheMenu(ActionEvent actionEvent) {
            changePage("/fxml/Menu.fxml");
        }

    public void handleCloseWindow(ActionEvent actionEvent) {
                closePage(Main.getCurrentStage());
                Platform.exit();

    }
}
