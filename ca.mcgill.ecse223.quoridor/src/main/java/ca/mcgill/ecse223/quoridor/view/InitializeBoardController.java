package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import ca.mcgill.ecse223.quoridor.controllers.WallController;
import ca.mcgill.ecse223.quoridor.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.List;


public class InitializeBoardController extends ViewController {

    public static boolean wallInHand = false;

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

        //display white player's pawn
        Circle whitePawn = new Circle();
//       awn.setCenterX(0);
//      pawn.setCenterY(0);
        whitePawn.setLayoutX(189);
        whitePawn.setLayoutY(17);
        whitePawn.setRadius(8);
        whitePawn.setFill(Color.web("#1e90ff"));
        board.getChildren().add(whitePawn);

        Circle blackPawn = new Circle();
//       awn.setCenterX(0);
//      pawn.setCenterY(0);
        blackPawn.setLayoutX(189);
        blackPawn.setLayoutY(363);
        blackPawn.setRadius(8);
        blackPawn.setFill(Color.web("#5aff1e"));
        board.getChildren().add(blackPawn);



        Circle pawn = new Circle();
//       awn.setCenterX(0);
//      pawn.setCenterY(0);
        pawn.setLayoutX(189);
        pawn.setLayoutX(17);
        pawn.setRadius(8);
        pawn.setFill(Color.web("#1e90ff"));
        board.getChildren().add(pawn);


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
            System.out.println("no more walls");
        }
    }

    public void refresh() {
        GamePosition position = ModelQuery.getCurrentPosition();
        Player white = ModelQuery.getWhitePlayer();
        Player black = ModelQuery.getBlackPlayer();

        // remove all walls and pawns


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

        // update wall positions
        ModelQuery.getCurrentGame();
        List<Wall> walls = ModelQuery.getAllWallsOnBoard();

        for (Wall wall : walls) {
            placeWall(wall, false);
        }

        // update wall move candidate
        Wall wall = ModelQuery.getWallMoveCandidate().getWallPlaced();
        placeWall(wall, true);
    }

    private void placePawn(PlayerPosition pawn){
        Tile tile = pawn.getTile();
    }

    public void placeWall(Wall wall, boolean isWall) {

        Tile tile = wall.getMove().getTargetTile();
        Direction dir = wall.getMove().getWallDirection();
        Pair<Integer, Integer> coord = convertWallToCanvas(tile.getRow(), tile.getColumn());


        Rectangle rectangle = new Rectangle(coord.getKey(), coord.getValue(), 9, 77);

        // setup color
        if (isWall) {
            rectangle.setFill(Color.GREY);
        } else {
            rectangle.setFill(Color.web("#5aff1e"));
        }

        if (dir.toString() == "Horizontal") {
            rectangle.setRotate(0);
        } else {
            rectangle.setRotate(90);
        }

        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        rectangle.setStroke(Color.web("#000000"));
        rectangle.setStrokeWidth(1.5);
        rectangle.setStrokeType(StrokeType.INSIDE);

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
    }

    private Pair<Integer, Integer> convertPawnToCanvas(int row, int col) {
        int x = (row - 1) * 43 + 17;
        int y = (col - 1) * 43 + 17;
        return new Pair<>(x, y);
    }

    private Pair<Integer, Integer> convertWallToCanvas(int row, int col) {
        int x = row * 43 - 10;
        int y = row * 43 - 10;
        return new Pair<>(x, y);
    }

}
