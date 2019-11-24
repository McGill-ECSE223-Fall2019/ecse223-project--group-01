package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.BoardController;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.PositionController;
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


public class NewGameMenuController extends ViewController {

    @FXML
    public TextField whitePlayerName;
    public TextField blackPlayerName;
    public TextField redPlayerName;
    public TextField greenPlayerName;
    public TextField minutes;
    public TextField seconds;
    public Button confirm;
    public ChoiceBox<String> existingWhiteChoices; 
    public ChoiceBox<String> existingBlackChoices;
    public ChoiceBox<String> existingRedChoices;
    public ChoiceBox<String> existingGreenChoices;
    public ChoiceDialog<String> existingSavedPosition;
    List<String> saveFiles;
    public String saveLocation = ".\\";
    
    public RadioButton twoPlayer;
    public RadioButton fourPlayer;
    public ToggleGroup numberPlayers;

    public static String minS;
    public static String secS;

    public void initialize() {
    	
        StartNewGameController.initializeGame(); 
        List<User> existingUsers = StartNewGameController.existedUsers();
        
        existingWhiteChoices.setOnAction(e -> whitePlayerName.setText(""));
        existingBlackChoices.setOnAction(e -> blackPlayerName.setText(""));
        existingRedChoices.setOnAction(e -> blackPlayerName.setText(""));
        existingGreenChoices.setOnAction(e -> whitePlayerName.setText(""));
        
        //redPlayerName.setText(numPlayers.getSelectedToggle().toString());
        
        

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
        
        redPlayerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                existingRedChoices.setValue(null);
            }
        });

        greenPlayerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                existingGreenChoices.setValue(null);
            }
        });


        for (User user : existingUsers) {
        	existingWhiteChoices.getItems().add(user.getName());
            existingBlackChoices.getItems().add(user.getName());
            existingRedChoices.getItems().add(user.getName());
            existingGreenChoices.getItems().add(user.getName());
        }
        
        
		redPlayerName.setEditable(false);
		greenPlayerName.setEditable(false);
		existingRedChoices.setDisable(false);
		existingGreenChoices.setDisable(false);
		redPlayerName.setStyle("-fx-background-color: grey;");
		greenPlayerName.setStyle("-fx-background-color: grey;");
        numberPlayers.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
        { 
            public void changed(ObservableValue<? extends Toggle> ob,  
                                                    Toggle o, Toggle n) 
            { 
            	if(numberPlayers.getSelectedToggle().equals(twoPlayer)) {
            		redPlayerName.setEditable(false);
            		greenPlayerName.setEditable(false);
            		existingRedChoices.setDisable(true);
            		existingGreenChoices.setDisable(true);
            		redPlayerName.setStyle("-fx-background-color: grey;");
            		greenPlayerName.setStyle("-fx-background-color: grey;");
            	} else {
            		redPlayerName.setEditable(true);
            		greenPlayerName.setEditable(true);
            		existingRedChoices.setDisable(false);
            		existingGreenChoices.setDisable(false);
            		redPlayerName.setStyle("-fx-background-color: white;");
            		greenPlayerName.setStyle("-fx-background-color: white;");
            	}
            } 
        }); 

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
        String redName = "";
        String greenName = "";


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

        if (numberPlayers.getSelectedToggle().equals(fourPlayer)) {
            if (redPlayerName.getText().equals("") && existingRedChoices.getValue() == null) {
                error += "Red name not set \n";
                readyTostart = false;
            } else if (!redPlayerName.getText().equals("") && StartNewGameController.usernameExists(redPlayerName.getText())) {
                error += "Red username already exists \n";
                readyTostart = false;
            }

            //validate black player name
            if (greenPlayerName.getText().equals("") && existingGreenChoices.getValue() == null) {
                error += "Green player name not set \n";
                readyTostart = false;
            } else if (!greenPlayerName.getText().equals("") && StartNewGameController.usernameExists(greenPlayerName.getText())) {
                error += "Green username already exists \n";
                readyTostart = false;
            }
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
        } else if (Integer.parseInt(seconds.getText()) == 0 && Integer.parseInt(minutes.getText()) == 0) {
            error += "Thinking Time is invalid";
            readyTostart = false;
        }

        // All good begin initialization process
        if (readyTostart) {
            //setup names
            if(whitePlayerName.getText().equals("")){
                whiteName = existingWhiteChoices.getValue();
            }
            else{
                whiteName = whitePlayerName.getText();
            }
            if(blackPlayerName.getText().equals("")){
                blackName = existingBlackChoices.getValue();
            }
            else{
                blackName = blackPlayerName.getText();
            }
            
            if(numberPlayers.getSelectedToggle().equals(fourPlayer)) {
                if(redPlayerName.getText().equals("")){
                    redName = existingRedChoices.getValue();
                }
                else{
                    redName = redPlayerName.getText();
                }
                if(blackPlayerName.getText().equals("")){
                    greenName = existingGreenChoices.getValue();
                }
                else{
                    greenName = greenPlayerName.getText();
                }
            }


            // Validate both player's name
            if(numberPlayers.getSelectedToggle().equals(twoPlayer)) {
            	if(whiteName.equals(blackName)){
            		error+= "Players may not have the same name!";
                	readyTostart = false;
            	}
            } else if(numberPlayers.getSelectedToggle().equals(fourPlayer)) {
            	if(whiteName.equals(blackName)|| whiteName.equals(redName) || whiteName.equals(greenName) || 
            			blackName.equals(redName) || blackName.equals(greenName) || redName.equals(greenName)) {
            		error+= "Players may not have the same name!";
            		readyTostart = false;
            	}
            }

            StartNewGameController.initializeGame();
            if(numberPlayers.getSelectedToggle().equals(fourPlayer)) {
            	ModelQuery.getCurrentGame().setIsFourPlayer(true);
            }
            
            //setup user-player connections
            StartNewGameController.whitePlayerChoosesAUsername(whiteName); 
            StartNewGameController.blackPlayerChooseAUsername(blackName);
            if(numberPlayers.getSelectedToggle().equals(fourPlayer)) { 
            	StartNewGameController.redPlayerChooseAUsername(redName);
            	StartNewGameController.greenPlayerChooseAUsername(greenName);
            }
            
            minS = minutes.getText();
            secS = seconds.getText();
            StartNewGameController.setTotalThinkingTime(Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
            BoardController.initializeBoard();
            changePage("/fxml/InitializeBoard.fxml");
        }
        // Display errors
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
            File directory = new File("./");
            File[] saveFiles = directory.listFiles((d,name) -> name.endsWith(".dat"));
            List<String> listOfSaves = new ArrayList<>();
            for(File file: saveFiles){
                listOfSaves.add(file.getName());
            }

            //make those savefiles appear in the ChoiceBox
            existingSavedPosition = new ChoiceDialog<String>("",listOfSaves);
            existingSavedPosition.setTitle("List of saved positions");
            existingSavedPosition.setHeaderText("Choose a saved position file to load");


            //get repsonse value
            Optional<String> result = existingSavedPosition.showAndWait();
            if(result.isPresent()){
                filename = result.get();
            }
            else{
                Alert loadWarning = new Alert(Alert.AlertType.WARNING);
                loadWarning.setHeaderText("Missing selection");
                loadWarning.setContentText("Missing selection of save position file");
                loadWarning.showAndWait();
                filename = null;
            }

            StartNewGameController.setTotalThinkingTime(Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
            try {
                if(!PositionController.loadGame(filename, whitePlayerName.getText(),blackPlayerName.getText())){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Unable to load position");
                    errorAlert.setContentText("The saved positions were unable to be loaded");
                    errorAlert.showAndWait();
                }
                else
                    changePage("/fxml/InitializeBoard.fxml");
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error in loading Position");
                errorAlert.setContentText("There was an error in loading your position");
                errorAlert.showAndWait();
                e.printStackTrace();
            } catch (NullPointerException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error in loading Position");
                errorAlert.setContentText("There are no save files");
                errorAlert.showAndWait();

            }

        }
        // Display errors
        else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, page, "Error", error);
        }
    }

    public void handleBackToMenu(ActionEvent actionEvent) {
        changePage("/fxml/Menu.fxml");
    }
}


//TODO: load name list

