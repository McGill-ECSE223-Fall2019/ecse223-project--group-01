package ca.mcgill.ecse223.quoridor.view;
import ca.mcgill.ecse223.quoridor.controllers.*;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.model.Player;
import javafx.animation.KeyFrame;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Pair;


import java.util.List;
import java.util.Optional;

import static java.awt.event.KeyEvent.*;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class InitializeBoardController extends ViewController {



    private Pair<Integer, Integer> convertPawnToCanvas(int row, int col){
        int x = (row-1)*43 + 17;
        int y = (col-1)*43 + 17;
        return new Pair<>(x,y);
    }

    private Pair<Integer, Integer> convertWallToCanvas(int row, int col){
        int x  = row*43-10;
        int y  = row*43-10;
        return new Pair<>(x,y);
    }


    @FXML
    private AnchorPane board;
    Rectangle wall;
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
        String nextPlayer = ModelQuery.getPlayerToMove().getNextPlayer().getUser().getName();

        //grey out the next player name
        if (nextPlayer.equals(blackPlayerName.getText())) {
            blackPlayerName.setFill(Color.LIGHTGRAY);
            playerIsWhite = true;
        } else {
            whitePlayerName.setFill(Color.LIGHTGRAY);
        }

        //display player name on the thinking time section
        whitePlayerName1.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName1.setText(ModelQuery.getBlackPlayer().getUser().getName());

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
    }


    public void handleBackToMenu(ActionEvent actionEvent) {
            timeline.stop();
            changePage("/fxml/Menu.fxml");

    }

    public void createNewWall(ActionEvent actionEvent) {
        try {
            if(WallController.grabWall()){
                refresh();
            }
            else{
                System.out.println("no more walls");
            }
        } catch (Exception e){
            System.out.println("Error");
        }
    }

    public void refresh(){
        // remove all walls and pawns


        // update wall positions
        ModelQuery.getCurrentGame();
        List<Wall> walls = ModelQuery.getAllWallsOnBoard();

        for(Wall wall: walls){
            placeWall(wall, false);
        }

        Wall wall =  ModelQuery.getWallMoveCandidate().getWallPlaced();
        placeWall(wall,true);

    }

    public void placeWall(Wall wall, boolean isWall){

            Tile tile  = wall.getMove().getTargetTile();
            Direction dir = wall.getMove().getWallDirection();
            Pair<Integer,Integer> coord = convertWallToCanvas(tile.getRow(),tile.getColumn());


            Rectangle rectangle = new Rectangle(coord.getKey(), coord.getValue(), 9, 77);

            if(isWall){
                rectangle.setFill(Color.GREY);
            }
            else{
                rectangle.setFill(Color.web("#5aff1e"));
            }
            rectangle.setArcWidth(5);
            rectangle.setArcHeight(5);
            rectangle.setStroke(Color.web("#000000"));
            rectangle.setStrokeWidth(1.5);
            rectangle.setStrokeType(StrokeType.INSIDE);
        if(dir.toString() == "Horizontal"){
            rectangle.setRotate(0);
        }
        else{
            rectangle.setRotate(90);
        }
            board.getChildren().add(rectangle);

    }

      public void handleKeyPressed(KeyEvent keyEvent) {


//
//        //Moves the wall up
//        if(keyEvent.equals(VK_W)){
//            wall.setY();
//        }
//        //Moves the wall left
//        else if(keyEvent.equals(VK_A)){
//            wall.setX();
//        }
//        //Moves the wall down
//        else if(keyEvent.equals(VK_S)){
//            wall.setY();
//        }
//        //Moves the wall right
//        else if(keyEvent.equals(VK_D)){
//            wall.setX();
//        }
//        //Confirm wall placement and drops the wall
//        else if(keyEvent.equals(VK_SPACE)){
//
//        }

          // TODO SET NUMBER OF WALLS -1 FOR THE CURRENT PLAYER
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
