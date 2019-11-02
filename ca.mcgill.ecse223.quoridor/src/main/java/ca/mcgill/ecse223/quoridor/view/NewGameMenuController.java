package ca.mcgill.ecse223.quoridor.view;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import javafx.event.EventHandler;
import javafx.scene.control.SplitMenuButton;
import ca.mcgill.ecse223.quoridor.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Window;


import java.awt.event.MouseEvent;
import java.util.List;


public class NewGameMenuController extends ViewController {

    @FXML
    public TextField whitePlayerName;
    public TextField blackPlayerName;
    public SplitMenuButton existedWhitePlayerList = new SplitMenuButton();
    public SplitMenuButton existedBlackPlayerList = new SplitMenuButton();
    public TextField minutes;
    public TextField seconds;
    public Button confirm;
    public static boolean alertFlagWhite = false;
    public static boolean alertFlagBlack = false;
    public static String message;


    public void initialize() {
        StartNewGameController.initializeGame();
        List<User> existingUsers = StartNewGameController.existedUsers();

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                System.out.println(((MenuItem)e.getSource()).getText() + " selected");
            }
        };

        for (User user : existingUsers) {
            MenuItem menuItem = new MenuItem(user.getName());
            existedWhitePlayerList.getItems().add(menuItem);
            existedBlackPlayerList.getItems().add(menuItem);
            menuItem.setOnAction(event1);
        }
            MenuItem menuItem = new MenuItem("1");
            MenuItem menuItem1 = new MenuItem("2");
            existedWhitePlayerList.getItems().add(menuItem);
            existedWhitePlayerList.getItems().add(menuItem1);

            menuItem.setOnAction(event1);
            menuItem1.setOnAction(event1);

    }

    public void backToMainMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }

    public void handleInitializeBoard(ActionEvent actionEvent) {
        // confirm button
        Window page = confirm.getScene().getWindow();

try {
    if (whitePlayerName.getText() != null) {
        if (StartNewGameController.usernameExists(whitePlayerName.getText())) {
            message = "Player 1 name is already existed!";
            alertFlagWhite = true;
        } else {
            StartNewGameController.whitePlayerChoosesAUsername(whitePlayerName.getText());
            existedWhitePlayerList.setDisable(true);
        }
    } else {
        whitePlayerName.setDisable(true);
        if (existedWhitePlayerList != null) {
            StartNewGameController.whitePlayerChoosesAUsername(existedWhitePlayerList.getText());
        } else {
            message = "Please enter name for player 1!";
            existedWhitePlayerList.setDisable(true);
            alertFlagWhite = true;
        }
    }

    if (blackPlayerName.getText() != null) {
        if (StartNewGameController.usernameExists(blackPlayerName.getText())) {
            message = "Player 2 name is already existed!";
            alertFlagBlack = true;
        } else {
            StartNewGameController.blackPlayerChooseAUsername(blackPlayerName.getText());
            existedBlackPlayerList.setDisable(true);
        }
    } else {
        blackPlayerName.setDisable(true);
        if (existedWhitePlayerList != null) {
            StartNewGameController.blackPlayerChooseAUsername(existedBlackPlayerList.getText());
        } else {
            message = "Please enter name for player 2!";
            alertFlagBlack = true;
        }
    }

    StartNewGameController.setThinkingTime(Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
        //TODO: Throw IllegalInputException

        if (alertFlagWhite && alertFlagBlack) {
            message = "Please create name for both players or select name from the player list!";
        }

//        if ((whitePlayerName.getText() == null && existedWhitePlayerList.getText() == null) ||
//                (blackPlayerName.getText() == null && existedBlackPlayerList.getText() == null)) {

//            whitePlayerName.setDisable(true);
//            existedWhitePlayerList.setDisable(true);
//            blackPlayerName.setDisable(true);
//            existedBlackPlayerList.setDisable(true);
//            AlertHelper.showAlert(Alert.AlertType.ERROR, page, "alert", "Please enter username");
//        }

        changePage("/fxml/InitializeBoard.fxml");
}catch (NullPointerException e) {
    //System.out.println(e.getMessage());
    //TODO: message doesnt update depend on the cases :(
    AlertHelper.showAlert(Alert.AlertType.ERROR, page, "alert", "something wrong");

}



    }
}

    //TODO: load name list

