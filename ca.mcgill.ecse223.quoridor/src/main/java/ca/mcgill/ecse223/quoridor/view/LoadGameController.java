package ca.mcgill.ecse223.quoridor.view;
import ca.mcgill.ecse223.quoridor.controllers.MusicController;
import ca.mcgill.ecse223.quoridor.controllers.PositionController;
import ca.mcgill.ecse223.quoridor.controllers.SaveLoadGameController;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import ca.mcgill.ecse223.quoridor.model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class LoadGameController extends ViewController{
    @FXML
    public Button confirm;
    public TextField whitePlayerName;
    public TextField blackPlayerName;
    public TextField redPlayerName;
    public TextField greenPlayerName;
    public TextField minutes;
    public TextField seconds;
    public ChoiceBox<String> existingWhiteChoices;
    public ChoiceBox<String> existingBlackChoices;
    public ChoiceBox<String> existingRedChoices;
    public ChoiceBox<String> existingGreenChoices;
    public ChoiceDialog<String> existingSavedPosition;
    public ToggleGroup numberPlayers;
    public RadioButton twoPlayer;
    public RadioButton fourPlayer;
    List<String> saveFiles;
    public String saveLocation = ".\\";
    //Default names
    public static String loadWhiteName;
    public static String loadBlackName;

    @SuppressWarnings("Duplicates")
    public void initialize() {
        StartNewGameController.initializeGame();
        List<User> existingUsers = StartNewGameController.existedUsers();

        existingWhiteChoices.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                loadWhiteName = (existingWhiteChoices.getItems().get((Integer) number2));
            }
        });
        existingBlackChoices.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                loadBlackName = (existingWhiteChoices.getItems().get((Integer) number2));
            }
        });

        for (User user : existingUsers) {
            existingWhiteChoices.getItems().add(user.getName());
            existingBlackChoices.getItems().add(user.getName());
        }


    }

    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @SuppressWarnings("Duplicates")
    public void handleContinueGame(ActionEvent actionEvent) {
        // confirm button
       // Window page = confirm.getScene().getWindow();
        boolean readyTostart = true;
        String error = "";
        String whiteName;
        String blackName;
        String filename;

        //.Open a list of saved games


        // confirm that all fields have been set
        // validate white player name

        // All good begin initialization process
        if (readyTostart) {

            //add all savefiles into a list
            File directory = new File("./");
            File[] saveFiles = directory.listFiles((d,name) -> name.endsWith(".mov"));
            List<String> listOfSaves = new ArrayList<>();
            for(File file: saveFiles){
                listOfSaves.add(file.getName());
            }

            //make those savefiles appear in the ChoiceBox
            existingSavedPosition = new ChoiceDialog<String>("",listOfSaves);
            existingSavedPosition.setTitle("List of saved games");
            existingSavedPosition.setHeaderText("Choose a saved game file to load");


            //get repsonse value
            Optional<String> result = existingSavedPosition.showAndWait();
            if(result.isPresent()){
                filename = result.get();
            }
            else{
                Alert loadWarning = new Alert(Alert.AlertType.WARNING);
                loadWarning.setHeaderText("Missing selection");
                loadWarning.setContentText("Missing selection of save game file");
                loadWarning.showAndWait();
                filename = null;
            }

            /* ------------------------- HARD CODED THE THINKING TIME FOR NOW ------------------------- */
            StartNewGameController.setTotalThinkingTime(1, 30);
            try {
                String wname;
                String bname;
                if(loadWhiteName != null){
                    wname = loadWhiteName;
                }
                else{
                    wname = "White";
                }
                if(loadBlackName != null){
                    bname = loadBlackName;
                }
                else{
                    bname = "Black";
                }
                if(!SaveLoadGameController.fileLoad(filename, wname,bname)){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Unable to load game");
                    errorAlert.setContentText("The saved game were unable to be loaded");
                    errorAlert.showAndWait();
                }
                else{
                    /* Playing the Battle music */
                    MusicController.playEpicMusic();
                    changePage("/fxml/InitializeBoard.fxml");
                }
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error in loading game");
                errorAlert.setContentText("There was an error in loading your game");
                errorAlert.showAndWait();
                e.printStackTrace();
            } catch (NullPointerException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error in loading game");
                errorAlert.setContentText("There are no save files");
                errorAlert.showAndWait();

            }
        }
        // Display errors
        //else {
        //    AlertHelper.showAlert(Alert.AlertType.ERROR, page, "Error", error);
        //}
    }

    public void handleReplayMode(ActionEvent actionEvent) {
        // confirm button
        // Window page = confirm.getScene().getWindow();
         boolean readyTostart = true;
         String error = "";
         String whiteName;
         String blackName;
         String filename;

         //.Open a list of saved games


         // confirm that all fields have been set
         // validate white player name

         // All good begin initialization process
         if (readyTostart) {

             //add all savefiles into a list
             File directory = new File("./");
             File[] saveFiles = directory.listFiles((d,name) -> name.endsWith(".mov"));
             List<String> listOfSaves = new ArrayList<>();
             for(File file: saveFiles){
                 listOfSaves.add(file.getName());
             }

             //make those savefiles appear in the ChoiceBox
             existingSavedPosition = new ChoiceDialog<String>("",listOfSaves);
             existingSavedPosition.setTitle("List of saved games");
             existingSavedPosition.setHeaderText("Choose a saved game file to load");


             //get repsonse value
             Optional<String> result = existingSavedPosition.showAndWait();
             if(result.isPresent()){
                 filename = result.get();
             }
             else{
                 Alert loadWarning = new Alert(Alert.AlertType.WARNING);
                 loadWarning.setHeaderText("Missing selection");
                 loadWarning.setContentText("Missing selection of save game file");
                 loadWarning.showAndWait();
                 filename = null;
             }

             /* ------------------------- HARD CODED THE THINKING TIME FOR NOW ------------------------- */
             StartNewGameController.setTotalThinkingTime(1, 30);
             try {
                 String wname;
                 String bname;
                 if(loadWhiteName != null){
                     wname = loadWhiteName;
                 }
                 else{
                     wname = "White";
                 }
                 if(loadBlackName != null){
                     bname = loadBlackName;
                 }
                 else{
                     bname = "Black";
                 }
                 if(!SaveLoadGameController.fileLoad(filename, wname,bname)){
                     Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                     errorAlert.setHeaderText("Unable to load game");
                     errorAlert.setContentText("The saved game were unable to be loaded");
                     errorAlert.showAndWait();
                 }
                 else{
                     /* Playing the Battle music */
                     //MusicController.playEpicMusic();
                     changePage("/fxml/ReplayMode.fxml");
                 }
             } catch (IOException e) {
                 Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                 errorAlert.setHeaderText("Error in loading game");
                 errorAlert.setContentText("There was an error in loading your game");
                 errorAlert.showAndWait();
                 e.printStackTrace();
             } catch (NullPointerException e) {
                 Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                 errorAlert.setHeaderText("Error in loading game");
                 errorAlert.setContentText("There are no save files");
                 errorAlert.showAndWait();

             }
         }   	
    }
}
