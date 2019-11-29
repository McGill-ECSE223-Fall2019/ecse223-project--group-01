package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.*;
import ca.mcgill.ecse223.quoridor.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.List;


public class InitializeBoardController extends ViewController{

    enum PlayerState {WALL, PAWN, IDLE};
    PlayerState state = PlayerState.IDLE;

    @FXML
    private AnchorPane board;
    public Text whitePlayerName;
    public Text blackPlayerName;
    public Text whitePlayerName1;
    public Text blackPlayerName1;
    public Label timerForWhitePlayer;
    public Label timerForBlackPlayer;
    public Text whiteNumOfWalls;
    public Text blackNumOfWalls;
    public Timeline timeline;
    public static boolean playerIsWhite = false;
    public static boolean isWallDrop = false;
    public static boolean isPawnMoved = false;
    public String initialTime;
    public static boolean whiteWon = false;
    public static boolean blackWon = false;


    public void initialize() {

        //display player name
        whitePlayerName.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName.setText(ModelQuery.getBlackPlayer().getUser().getName());

        //display player name on the thinking time section
        whitePlayerName1.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName1.setText(ModelQuery.getBlackPlayer().getUser().getName());

        //start the clock once the game is initiated
        StartNewGameController.startTheClock();

        //record the time set per turn
        initialTime = StartNewGameController.toTimeStr();

    	timerForWhitePlayer.setText(initialTime);
    	timerForBlackPlayer.setText(initialTime);

        state = PlayerState.IDLE;
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

                if ((StartNewGameController.timeOver()) || isWallDrop || isPawnMoved) {
                	timerForWhitePlayer.setText(initialTime);
                	timerForBlackPlayer.setText(initialTime);

                	SwitchPlayerController.switchActivePlayer();
                	isWallDrop = false;
                	isPawnMoved = false;

                	StartNewGameController.resetTimeToSet();
                }
                //TODO: MODIFY: time counts to zero/white reaches row = 9 or black reach row = 1
                else if  (PawnController.whiteWonCheck) {
                    whiteWon = true;
                    //TODO: Stop timer

                }
                else if (PawnController.blackWonCheck) {
                    blackWon = true;
                    //TODO: Stop timer

                }
                //TODO: timer should not count again once it reaches zero


                //grey out the next player name & count down time for the current player
                if (currentPlayer.equals(ModelQuery.getWhitePlayer())) {
                    timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
                } else {
                    timerForBlackPlayer.setText(StartNewGameController.toTimeStr());

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
        if (state == PlayerState.WALL) {
            WallController.cancelWallMove();
            state = PlayerState.IDLE;
        }
        //
        else if (WallController.grabWall()) {
            state = PlayerState.WALL;
        } else {
            System.out.println("No more walls");
        }
        refresh();
    }

    public void handleGrabPawn(ActionEvent actionEvent) {
        if(state == PlayerState.PAWN){
            state = PlayerState.IDLE;
        }

        else {
            state = PlayerState.PAWN;
            WallController.cancelWallMove();
        }
        refresh();
    }


    public void refresh() {
        GamePosition position = ModelQuery.getCurrentPosition();
        Player white = ModelQuery.getWhitePlayer();
        Player black = ModelQuery.getBlackPlayer();

        // remove all walls and pawns
        board.getChildren().clear();

        // update player turn
        if (position.getPlayerToMove().equals(white)) {
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

        // check if one of the player wins
        if (whiteWon) {
            ModelQuery.getCurrentGame().setWinningPlayer(ModelQuery.getWhitePlayer());
            changePage("/fxml/EndScene.fxml");
            whiteWon = false; //avoid change page all the time
        }
        else if (blackWon) {
            ModelQuery.getCurrentGame().setWinningPlayer(ModelQuery.getBlackPlayer());
            changePage("/fxml/EndScene.fxml");
            blackWon = false;
        }
    }

    private void placePawn(PlayerPosition position, boolean isWhite){
        Tile tile = position.getTile();

        Pair<Integer,Integer> coord = convertPawnToCanvas(tile.getRow(),tile.getColumn());

        Circle pawn = new Circle();
        if(isWhite){
            pawn.setFill(Color.WHITE);
            pawn.setStroke(Color.BLACK);

        }
        else{
            pawn.setFill(Color.BLACK);

        }
        pawn.setLayoutX(coord.getValue());
        pawn.setLayoutY(coord.getKey());
        pawn.setRadius(8);
        board.getChildren().add(pawn);
    }

    public void placeWall(WallMove move, boolean isWall) {
        Tile tile = move.getTargetTile();
        Direction dir = move.getWallDirection();
        Pair<Integer, Integer> coord = convertWallToCanvas(tile.getRow(), tile.getColumn());
        Rectangle rectangle = new Rectangle(coord.getKey(), coord.getValue(), 9, 77);

        Player white = ModelQuery.getWhitePlayer();
        Player black = ModelQuery.getBlackPlayer();
        // setup color
        if (isWall) {
            rectangle.setFill(Color.GRAY);
        } else {
            rectangle.setFill(Color.DEEPSKYBLUE);
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

        if(state==PlayerState.WALL){
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
                    state = PlayerState.IDLE;
                    SwitchPlayerController.switchActivePlayer();
                    isWallDrop=true;
                }
            }
            else if(code.equals(KeyCode.R)){
                WallController.rotateWall();
            }
            refresh();
        }

        if (state==PlayerState.PAWN){
            /*For handling pawn move*/
                if (code.equals(KeyCode.I)) {
                    PawnController.movePawn("up");
                }
                else if (code.equals(KeyCode.K)) {
                    PawnController.movePawn("down");
                }
                else if (code.equals(KeyCode.J)) {
                    PawnController.movePawn("left");
                }
                else if (code.equals(KeyCode.L)) {
                    PawnController.movePawn("right");
                }
                else if (code.equals(KeyCode.U)) {
                    PawnController.movePawn("upleft");
                }
                else if (code.equals(KeyCode.O)) {
                    PawnController.movePawn("upright");
                }
                else if (code.equals(KeyCode.N)) {
                    PawnController.movePawn("downleft");
                }
                else if (code.equals(KeyCode.COMMA)) {
                    PawnController.movePawn("downright");
                }
            refresh();
        }
    }

    public void dropWall(){
        if(WallController.dropWall()){
            state = PlayerState.IDLE;
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
        if(state==PlayerState.WALL){
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

    public static void showStage(String message){
        Stage newStage = new Stage();
        VBox comp = new VBox();
        TextField nameField = new TextField("Game End!");
        TextField phoneNumber = new TextField(message);
        comp.getChildren().add(nameField);
        comp.getChildren().add(phoneNumber);

        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();
    }
}
