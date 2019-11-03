package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.BoardController;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.PositionController;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import ca.mcgill.ecse223.quoridor.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NewGameMenuController extends ViewController {

    @FXML
    public TextField whitePlayerName;
    public TextField blackPlayerName;
    public TextField minutes;
    public TextField seconds;
    public Button confirm;
    public ChoiceBox<String> existingBlackChoices;
    public ChoiceBox<String> existingWhiteChoices;
    public ChoiceBox<String> existingSavedPosition;
    List<String> saveFiles;
    public String saveLocation = ".\\";

    public void initialize() {

        StartNewGameController.initializeGame();
        List<User> existingUsers = StartNewGameController.existedUsers();

        existingBlackChoices.setOnAction(e -> blackPlayerName.setText(""));

        existingWhiteChoices.setOnAction(e -> whitePlayerName.setText(""));

        whitePlayerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                existingWhiteChoices.setValue(null);
            }
        });

        blackPlayerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                existingBlackChoices.setValue(null);
            }
        });

        for (User user : existingUsers) {
            existingBlackChoices.getItems().add(user.getName());
            existingWhiteChoices.getItems().add(user.getName());
        }

    }

    public void backToMainMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }

    public void handleInitializeBoard(ActionEvent actionEvent) {
        // confirm button
        Window page = confirm.getScene().getWindow();
        boolean readyTostart = true;
        String error = "";
        String whiteName;
        String blackName;


        // confirm that all fields have been set
        // validate white player name

        if (whitePlayerName.getText().equals("") && existingWhiteChoices.getValue() == null) {
            error += "White name not set \n";
            readyTostart = false;
        } else if (!whitePlayerName.getText().equals("") && StartNewGameController.usernameExists(whitePlayerName.getText())) {
            error += "White username already exists \n";
            readyTostart = false;
        }

        //validate black player name
        if (blackPlayerName.getText().equals("") && existingBlackChoices.getValue() == null) {
            error += "Black player name not set \n";
            readyTostart = false;
        } else if (!blackPlayerName.getText().equals("") && StartNewGameController.usernameExists(blackPlayerName.getText())) {
            error += "Black username already exists \n";
            readyTostart = false;
        }

        // validate thinking time
        if (seconds.getText().equals("") || minutes.getText().equals("")) {
            error += "Thinking time not set";
            readyTostart = false;
        } else if (!isInteger(seconds.getText()) || !isInteger(minutes.getText())) {
            error += "Thinking time is not a whole number";
            readyTostart = false;
        } else if (Integer.parseInt(seconds.getText()) > 60 || Integer.parseInt(seconds.getText()) < 0 || Integer.parseInt(minutes.getText()) < 0) {
            error += "Invalid numbers given for Thinking time";
            readyTostart = false;
        }

        // All good begin initialization process
        if (readyTostart) {
            if(blackPlayerName.getText().equals("")){
                blackName = existingBlackChoices.getValue();
            }
            else{
                blackName = blackPlayerName.getText();
            }
            if(whitePlayerName.getText().equals("")){
                whiteName = existingWhiteChoices.getValue();
            }
            else{
                whiteName = whitePlayerName.getText();
            }
            StartNewGameController.initializeGame();
            StartNewGameController.blackPlayerChooseAUsername(blackName);
            StartNewGameController.whitePlayerChoosesAUsername(whiteName);
            StartNewGameController.setTotalThinkingTime(Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
            BoardController.initializeBoard();
            changePage("/fxml/InitializeBoard.fxml");
        }
        // Display erros
        else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, page, "Error", error);
        }
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void handleLoadPosition(ActionEvent actionEvent) {
        // confirm button
        Window page = confirm.getScene().getWindow();
        boolean readyTostart = true;
        String error = "";
        String whiteName;
        String blackName;
        String filename;

        //.Open a list of saved games


        // confirm that all fields have been set
        // validate white player name

        if (whitePlayerName.getText().equals("") && existingWhiteChoices.getValue() == null) {
            error += "White name not set \n";
            readyTostart = false;
        } else if (!whitePlayerName.getText().equals("") && StartNewGameController.usernameExists(whitePlayerName.getText())) {
            error += "White username already exists \n";
            readyTostart = false;
        }

        //validate black player name
        if (blackPlayerName.getText().equals("") && existingBlackChoices.getValue() == null) {
            error += "Black player name not set \n";
            readyTostart = false;
        } else if (!blackPlayerName.getText().equals("") && StartNewGameController.usernameExists(blackPlayerName.getText())) {
            error += "Black username already exists \n";
            readyTostart = false;
        }

        // validate thinking time
        if (seconds.getText().equals("") || minutes.getText().equals("")) {
            error += "Thinking time not set";
            readyTostart = false;
        } else if (!isInteger(seconds.getText()) || !isInteger(minutes.getText())) {
            error += "Thinking time is not a whole number";
            readyTostart = false;
        } else if (Integer.parseInt(seconds.getText()) > 60 || Integer.parseInt(seconds.getText()) < 0 || Integer.parseInt(minutes.getText()) < 0) {
            error += "Invalid numbers given for Thinking time";
            readyTostart = false;
        }

        // All good begin initialization process
        if (readyTostart) {
            if(blackPlayerName.getText().equals("")){
                blackName = existingBlackChoices.getValue();
            }
            else{
                blackName = blackPlayerName.getText();
            }
            if(whitePlayerName.getText().equals("")){
                whiteName = existingWhiteChoices.getValue();
            }
            else{
                whiteName = whitePlayerName.getText();
            }

            //add all savefiles into a list
            File directory = new File(saveLocation);
            File[] saveFiles = directory.listFiles((d,name) -> name.endsWith(".dat"));

            //make those savefiles appear in the ChoiceBox
            if(saveFiles.length > 0){
                for(File names: saveFiles){
                    existingSavedPosition.getItems().add(names.getName());
                }
            }



            StartNewGameController.setTotalThinkingTime(Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
            try {

                if(!PositionController.loadGame("save_temp.dat", ModelQuery.getWhitePlayer().getUser().getName(),ModelQuery.getBlackPlayer().getUser().getName())){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Unable to load position");
                    errorAlert.setContentText("The saved positions were unable to be loaded");
                    errorAlert.showAndWait();
                }
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error in loading Position");
                errorAlert.setContentText("There was an error in loading your position");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
            changePage("/fxml/InitializeBoard.fxml");
        }
        // Display errors
        else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, page, "Error", error);
        }
    }
}


//TODO: load name list

