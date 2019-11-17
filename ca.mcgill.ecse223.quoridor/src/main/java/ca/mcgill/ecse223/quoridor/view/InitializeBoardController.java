package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.*;
import ca.mcgill.ecse223.quoridor.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.List;


public class InitializeBoardController extends ViewController{

    public static boolean wallInHand = false;

    @FXML
    private AnchorPane board;
    public Text whitePlayerName;
    public Text blackPlayerName;
    public Text redPlayerName;
    public Text greenPlayerName;
    
    public Text whitePlayerName1;
    public Text blackPlayerName1;
    public Text redPlayerName1;
    public Text greenPlayerName1;
    
    public Label timerForWhitePlayer;
    public Label timerForBlackPlayer;
    public Label timerForRedPlayer;
    public Label timerForGreenPlayer;
    
    public Text whiteNumOfWalls;
    public Text blackNumOfWalls;
    public Text redNumOfWalls;
    public Text greenNumOfWalls;
    
    public Timeline timeline;
    
    public static boolean playerIsWhite = false;
    public static boolean isWallDrop = false;
    public String initialTime;
    
    public Circle c1;
    public Circle c2;
    public Rectangle r1;
    public Rectangle r2;
    public Text x1;
    public Text x2;


    public void initialize() {

        //display player name
        whitePlayerName.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName.setText(ModelQuery.getBlackPlayer().getUser().getName());

        //display player name on the thinking time section
        whitePlayerName1.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName1.setText(ModelQuery.getBlackPlayer().getUser().getName());

        //if 4player mode
        if(ModelQuery.isFourPlayer()) {
            //display player name
            redPlayerName.setText(ModelQuery.getRedPlayer().getUser().getName());
            greenPlayerName.setText(ModelQuery.getGreenPlayer().getUser().getName());

            //display player name on the thinking time section
            redPlayerName1.setText(ModelQuery.getRedPlayer().getUser().getName());
            greenPlayerName1.setText(ModelQuery.getGreenPlayer().getUser().getName());
        } else { //if 2player mode, set 4player content invisible
        	redPlayerName.setVisible(false);
        	redPlayerName1.setVisible(false);
        	redNumOfWalls.setVisible(false);
        	timerForRedPlayer.setVisible(false);
        	greenPlayerName.setVisible(false);
        	greenPlayerName1.setVisible(false);
        	greenNumOfWalls.setVisible(false);
        	timerForGreenPlayer.setVisible(false);
        	c1.setVisible(false);
        	c2.setVisible(false);
        	r1.setVisible(false);
        	r2.setVisible(false);
        	x1.setVisible(false);
        	x2.setVisible(false);

        }
        
        //start the clock once the game is initiated
        StartNewGameController.startTheClock();

        //record the time set per turn
        initialTime = StartNewGameController.toTimeStr();

    	timerForWhitePlayer.setText(initialTime);
    	timerForBlackPlayer.setText(initialTime);
    	
    	if(ModelQuery.isFourPlayer()) {
    		timerForRedPlayer.setText(initialTime);
    		timerForGreenPlayer.setText(initialTime);
    	} else { //if 2player mode, set 4player content invisible
    		timerForRedPlayer.setVisible(false);
    		timerForGreenPlayer.setVisible(false);
    	}


        switchTimer();
    }

    public void switchTimer() {

        if (timeline != null) {
            timeline.stop();
        }
        // update timerLabel
        //timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Player currentPlayer = ModelQuery.getPlayerToMove();
                if ((StartNewGameController.timeOver()) || (isWallDrop == true) ) {

                	timerForWhitePlayer.setText(initialTime);
                	timerForBlackPlayer.setText(initialTime);

                	SwitchPlayerController.switchActivePlayer();
                	isWallDrop = false;

                	StartNewGameController.resetTimeToSet();
                }


                //grey out the next player name & count down time for the current player
                if (currentPlayer.equals(ModelQuery.getWhitePlayer())) {
                    timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
                } else if (currentPlayer.equals(ModelQuery.getBlackPlayer())){
                    timerForBlackPlayer.setText(StartNewGameController.toTimeStr());
                } else if (currentPlayer.equals(ModelQuery.getRedPlayer())) {
                	timerForRedPlayer.setText(StartNewGameController.toTimeStr());
                } else if (currentPlayer.equals(ModelQuery.getGreenPlayer())) {
                	timerForGreenPlayer.setText(StartNewGameController.toTimeStr());
                }
            }
        };

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), onFinished));
        timeline.playFromStart();
        refresh();
    }


    public void handleBackToMenu(ActionEvent actionEvent) {
        timeline.stop();
        changePage("/fxml/Menu.fxml");

    }

    public void createNewWall(ActionEvent actionEvent) {

        // Check if there is already a wall in hand
        // If so just cancel the wall move
        if (wallInHand) {
            WallController.cancelWallMove();
            wallInHand = false;
            refresh();
        }
        //
        else if (WallController.grabWall()) {
            wallInHand = true;
            refresh();
        } else {
            System.out.println("No more walls");
        }
    }

    public void refresh() {
        GamePosition position = ModelQuery.getCurrentPosition();
        Player white = ModelQuery.getWhitePlayer();
        Player black = ModelQuery.getBlackPlayer();
        Player red = ModelQuery.getRedPlayer();
        Player green = ModelQuery.getGreenPlayer();

        // remove all walls and pawns
        board.getChildren().clear();

        // update player turn
        if (position.getPlayerToMove().equals(black)) {
            whitePlayerName.setFill(Color.BLACK);
            blackPlayerName.setFill(Color.LIGHTGRAY);
        } else {
            whitePlayerName.setFill(Color.LIGHTGRAY);
            blackPlayerName.setFill(Color.BLACK);
        }

        // update walls in stock
        whiteNumOfWalls.setText(String.valueOf(position.getWhiteWallsInStock().size()));
        blackNumOfWalls.setText(String.valueOf(position.getBlackWallsInStock().size()));

        // update pawn positions
        placePawn(position.getWhitePosition(),true);
        placePawn(position.getBlackPosition(),false);

        // update wall positions
        ModelQuery.getCurrentGame();
        List<Wall> walls = ModelQuery.getAllWallsOnBoard();

        for (Wall wall : walls) {
            placeWall(wall.getMove(), false);
        }

        // update wall move candidate
        if(ModelQuery.getWallMoveCandidate()!=null){
            WallMove move = ModelQuery.getWallMoveCandidate();
            placeWall(move, true);
        }
    }

    private void placePawn(PlayerPosition position, boolean isWhite){
        Tile tile = position.getTile();

        Pair<Integer,Integer> coord = convertPawnToCanvas(tile.getRow(),tile.getColumn());

        Circle pawn = new Circle();
        if(isWhite){
            pawn.setFill(Color.web("#1e90ff"));
        }
        else{
            pawn.setFill(Color.web("#5aff1e"));
        }
        pawn.setLayoutX(coord.getKey());
        pawn.setLayoutY(coord.getValue());
        pawn.setRadius(8);
        board.getChildren().add(pawn);
    }

    public void placeWall(WallMove move, boolean isWall) {
        Tile tile = move.getTargetTile();
        Direction dir = move.getWallDirection();
        Pair<Integer, Integer> coord = convertWallToCanvas(tile.getRow(), tile.getColumn());
//        Pair<Integer, Integer> coord = convertWallToCanvas(, 1);

//        Rectangle rectangle = new Rectangle(coord.getKey(), coord.getValue(), 9, 77);

        Rectangle rectangle = new Rectangle(coord.getKey(), coord.getValue(), 9, 77);

        // setup color
        if (isWall) {
            rectangle.setFill(Color.GREY);
        } else {
            rectangle.setFill(Color.web("#5aff1e"));
        }

        if (dir.toString() == "Horizontal") {
            rectangle.setRotate(90);
        } else {
            rectangle.setRotate(0);
        }

        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        rectangle.setStroke(Color.web("#000000"));
        rectangle.setStrokeWidth(1.5);
        rectangle.setStrokeType(StrokeType.INSIDE);

        board.getChildren().add(rectangle);
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if(wallInHand){
            //Moves the wall up
            if(code.equals(KeyCode.W)){
                shiftWall("up");
            }
            //Moves the wall left
            else if(code.equals(KeyCode.A)){
                shiftWall("left");
            }
            //Moves the wall down
            else if(code.equals(KeyCode.S)){
                shiftWall("down");
            }
            //Moves the wall right
            else if(code.equals(KeyCode.D)){
                shiftWall("right");
            }
            //Confirm wall placement and drops the wall
            else if(code.equals(KeyCode.E)){
                if(WallController.dropWall()){
                    wallInHand=false;
                    SwitchPlayerController.switchActivePlayer();
                    isWallDrop=true;
                }
            }
            else if(code.equals(KeyCode.R)){
                WallController.rotateWall();
            }
            refresh();
        }
    }

    public void dropWall(){
        if(WallController.dropWall()){
            wallInHand=false;
        }
    }

    public void shiftWall(String side){
        WallController.shiftWall(side);
    }

    private Pair<Integer, Integer> convertPawnToCanvas(int row, int col) {
        int x = (row - 1) * 43 + 17;
        int y = (col - 1) * 43 + 17;
        return new Pair<>(x, y);
    }

    private Pair<Integer, Integer> convertWallToCanvas(int col, int row) {
        int x = (row) * 43 - 9;
        int y = (col-1) * 43;
        return new Pair<>(x, y);
    }

    public void handleRotate(ActionEvent event){
        if(wallInHand){
            WallController.rotateWall();
            refresh();
        }
    }

    public void handleSavePosition(ActionEvent actionEvent) {
        String filename;
        TextInputDialog textInput = new TextInputDialog();

        textInput.setTitle("Saving game position");
        textInput.getDialogPane().setContentText("Name of save file");

        TextField input = textInput.getEditor();
        textInput.showAndWait();

        if(input.getText() != null && input.getText().length() != 0) {
            filename = input.getText();

            if (!PositionController.saveGame(filename +".dat", ModelQuery.getPlayerToMove())) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                if (!PositionController.isPositionValid)
                    errorAlert.setContentText("The current positions are invalid");
                else
                    errorAlert.setContentText("There was an error in saving your positions");
                errorAlert.setHeaderText("Error in loading Position");
                errorAlert.showAndWait();
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setContentText("Positions is successfully saved in: " +filename +".dat");
                successAlert.showAndWait();
            }
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Missing file name");
        }
    }
}
