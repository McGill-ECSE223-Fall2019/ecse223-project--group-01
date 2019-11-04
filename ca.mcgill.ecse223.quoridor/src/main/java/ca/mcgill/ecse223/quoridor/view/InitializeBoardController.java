package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.PositionController;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import ca.mcgill.ecse223.quoridor.controllers.WallController;
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
    public Text whitePlayerName1;
    public Text blackPlayerName1;
    public Label timerForWhitePlayer;
    public Label timerForBlackPlayer;
    public Text whiteNumOfWalls;
    public Text blackNumOfWalls;
    public Timeline timeline;
    public static boolean playerIsWhite = false;


    public void initialize() {

        //display player name
        whitePlayerName.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName.setText(ModelQuery.getBlackPlayer().getUser().getName());

        //start the clock once the game is initiated
        StartNewGameController.startTheClock();
        timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
        timerForBlackPlayer.setText(StartNewGameController.toTimeStr());

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
                if (currentPlayer.getRemainingTime().getTime() <= 0) {
                    /*
                     * TODO: Reset total thinking time for the current player
                     * TODO: switch Player
                     * Player nextPlayer = currentPlayer.getNextPlayer();
                     * SwitchPlayerController.SwitchActivePlayer(nextPlayer); //should pass in string
                     * TODO: count down timer for the next player
                     */
                    // currentPlayer.setNextPlayer(currentPlayer.getNextPlayer());

                } else {
                    if (playerIsWhite) {
                        timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
                    } else {
                        timerForBlackPlayer.setText(StartNewGameController.toTimeStr());
                    }
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
                WallController.shiftWall("up");
            }
            //Moves the wall left
            else if(code.equals(KeyCode.A)){
                WallController.shiftWall("left");
            }
            //Moves the wall down
            else if(code.equals(KeyCode.S)){
                WallController.shiftWall("down");
            }
            //Moves the wall right
            else if(code.equals(KeyCode.D)){
                WallController.shiftWall("right");
            }
            //Confirm wall placement and drops the wall
            else if(code.equals(KeyCode.E)){
                if(WallController.dropWall()){
                    wallInHand=false;
                }
            }
            else if(code.equals(KeyCode.R)){
                WallController.rotateWall();
            }
            refresh();
        }
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

    public void handleClearBoard(ActionEvent actionEvent) {
        board.getChildren().clear();
    }

    public void handleSavePosition(ActionEvent actionEvent) {
        String filename;
        TextInputDialog textInput = new TextInputDialog();

        textInput.setTitle("Saving game position");
        textInput.getDialogPane().setContentText("Name of save file");

        TextField input = textInput.getEditor();

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
