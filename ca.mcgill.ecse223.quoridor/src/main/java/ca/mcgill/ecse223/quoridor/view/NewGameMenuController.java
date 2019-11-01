package ca.mcgill.ecse223.quoridor.view;
import javafx.event.ActionEvent;

public class NewGameMenuController extends ViewController{


    public void backToMainMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }
    public void handleInitializeBoard(ActionEvent actionEvent){
        changePage("/fxml/InitializeBoard.fxml");
    }
}
