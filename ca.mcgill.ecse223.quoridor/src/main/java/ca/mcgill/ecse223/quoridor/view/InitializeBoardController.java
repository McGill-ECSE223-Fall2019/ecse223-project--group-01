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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.List;


public class InitializeBoardController extends ViewController{

    enum PlayerState {WALL, PAWN, IDLE, WHITEWON, BLACKWON, REDWON, GREENWON};
    public PlayerState state = PlayerState.IDLE;
    public void handleResignGame(ActionEvent actionEvent) {
        ResignGameController.resign();
        refresh();
    }



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

    public Button grabWallBtn;
    public Button rotateWallBtn;
    public Button backBtn;

    public Timeline timeline;

    public static boolean playerIsWhite = false;
    public static boolean isWallDrop = false;
    public static boolean isPawnMoved = false;
    public String initialTime;
    boolean pawnMoved = false;
    public  PlayerPosition playerPosition ;
    public static boolean validMoved = false;
    public Rectangle rect1;
    public Rectangle rect2;
    public Rectangle rect3;
    public Rectangle rect4;
    public Rectangle rect5;
    public Rectangle rect6;
    public Rectangle rect7;
    public Rectangle rect8;
    public Rectangle rect9;
    public Rectangle rect10;
    public Rectangle rect11;
    public Rectangle rect12;
    public Rectangle rect13;
    public Rectangle rect14;
    public Rectangle rect15;
    public Rectangle rect16;
    public Rectangle rect17;
    public Rectangle rect18;
    public Rectangle rect19;
    public Rectangle rect20;
    public Rectangle rect21;
    public Rectangle rect22;
    public Rectangle rect23;
    public Rectangle rect24;
    public Rectangle rect25;
    public Rectangle rect26;
    public Rectangle rect27;
    public Rectangle rect28;;
    public Rectangle rect29;
    public Rectangle rect30;
    public Rectangle rect31;
    public Rectangle rect32;
    public Rectangle rect33;
    public Rectangle rect34;
    public Rectangle rect35;
    public Rectangle rect36;
    public Rectangle rect37;
    public Rectangle rect38;
    public Rectangle rect39;
    public Rectangle rect40;
    public Rectangle rect41;
    public Rectangle rect42;
    public Rectangle rect43;
    public Rectangle rect44;
    public Rectangle rect45;
    public Rectangle rect46;
    public Rectangle rect47;
    public Rectangle rect48;
    public Rectangle rect49;
    public Rectangle rect50;
    public Rectangle rect51;
    public Rectangle rect52;
    public Rectangle rect53;
    public Rectangle rect54;
    public Rectangle rect55;
    public Rectangle rect56;
    public Rectangle rect57;
    public Rectangle rect58;
    public Rectangle rect59;
    public Rectangle rect60;
    public Rectangle rect61;
    public Rectangle rect62;
    public Rectangle rect63;
    public Rectangle rect64;
    public Rectangle rect65;
    public Rectangle rect66;
    public Rectangle rect67;
    public Rectangle rect68;
    public Rectangle rect69;
    public Rectangle rect70;
    public Rectangle rect71;
    public Rectangle rect72;
    public Rectangle rect73;
    public Rectangle rect74;
    public Rectangle rect75;
    public Rectangle rect76;
    public Rectangle rect77;
    public Rectangle rect78;
    public Rectangle rect79;
    public Rectangle rect80;
    public Rectangle rect81;


    public Circle testCircle;

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
    	}

        state = PlayerState.IDLE;

        switchTimer();
    }

    public void handle(MouseEvent mouseEvent) {
        Rectangle rec = (Rectangle)mouseEvent.getSource();
        String z = rec.getId();
        Double x = rec.getLayoutX();
        Double y = rec.getLayoutY();
        playerPosition = ModelQuery.getPlayerPositionOfPlayerToMove();
        validMoved=true;

        System.out.println("ID : "+z+" X: "+x+" Y: "+y);





        if (state==PlayerState.PAWN) {
            String curDirection = checkPawnPostition(x, y, playerPosition);



            if (curDirection.equals("up")) {

                 //   AlertHelper.error(Alert.AlertType.ERROR, "Alert", "INVALID MOVE");
                validMoved = PawnController.movePawn("up");

            }
            else if (curDirection.equals("down")) {
                validMoved =  PawnController.movePawn("down");
            }
            else if (curDirection.equals("left")) {
                validMoved = PawnController.movePawn("left");
            }
            else if (curDirection.equals("right")) {
                validMoved =  PawnController.movePawn("right");
            }
            else if (curDirection.equals("upright")) {
                validMoved = PawnController.movePawn("upright");
            }
            else if (curDirection.equals("upleft")) {
                validMoved = PawnController.movePawn("upleft");
            }
            else if (curDirection.equals("downleft")) {
                validMoved = PawnController.movePawn("downleft");
            }
            else if (curDirection.equals("downright")) {
                validMoved = PawnController.movePawn("downright");
            }
            else if(curDirection.equals("error")){
                System.out.println("error");
                validMoved =false ;

            }
        }

        refresh();

    }

    public void checkStatus () {
        // check if one of the player wins
        if (ModelQuery.getCurrentGame().getGameStatus().equals(Game.GameStatus.WhiteWon)) {
            ModelQuery.getCurrentGame().setWinningPlayer(ModelQuery.getWhitePlayer());
            state = PlayerState.WHITEWON;
        }
        else if (ModelQuery.getCurrentGame().getGameStatus().equals(Game.GameStatus.BlackWon)) {
            ModelQuery.getCurrentGame().setWinningPlayer(ModelQuery.getBlackPlayer());
            state = PlayerState.BLACKWON;
        }
        else if (ModelQuery.getCurrentGame().getGameStatus().equals(Game.GameStatus.RedWon)) {
            ModelQuery.getCurrentGame().setWinningPlayer(ModelQuery.getRedPlayer());
            state = PlayerState.REDWON;
        }
        else if (ModelQuery.getCurrentGame().getGameStatus().equals(Game.GameStatus.GreenWon)) {
            ModelQuery.getCurrentGame().setWinningPlayer(ModelQuery.getGreenPlayer());
            state = PlayerState.GREENWON;
        } else {
            ModelQuery.getCurrentGame().setWinningPlayer(null);
        }
        refresh();
    }

    public String checkPawnPostition(Double x, Double y, PlayerPosition playerPosition){
        Tile tile = null;
        String direction= null;

        Player currentPlayer = ModelQuery.getPlayerToMove();
        tile = playerPosition.getTile();
        String playerName = playerPosition.getPlayer().getUser().getName();

        Pair<Integer,Integer> coord = convertPawnToCanvas(tile.getRow(),tile.getColumn());

        Double pawnX = (double)coord.getValue();
        Double pawnY = (double)coord.getKey();

            /*For handling pawn move*/
            System.out.println("PawnX : "+pawnX+ " PawnY: "+pawnY);

                if ((x-pawnX == -17 && y-pawnY == -60) || (pawnX-x==17 && pawnY - y == 103)) {  //jump or move pawn
                    direction = "up";
                }
                else if ((pawnX-x == 17 && pawnY-y == -26) || (pawnX-x == 17 && pawnY-y == -69)) {
                    direction = "down";
                }
                else if((pawnX-x == 60 && pawnY-y == 17) || (pawnX-x == 103 && pawnY-y == 17)){
                    direction = "left";
                }
                else  if((pawnX-x== -26 && pawnY-y == 17) || pawnX-x == -69 && pawnY-y == 17){
                    direction = "right";
                 }
                else if (pawnX-x== 60 && pawnY-y == 60) {
                   direction = "upleft";
                }
                else if (pawnX-x == -26 && pawnY-y == 60) {
                   direction = "upright";
                }
                else if (pawnX-x == 60 && pawnY-y == -26) {
                    direction = "downleft";
                }
                else if (pawnX-x == -26 && pawnY-y == -26) {
                    direction = "downright";
                }
                else {
                    direction = "error";
                    }
                    System.out.println("Direction = "+direction);

        return  direction;

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

                if (isWallDrop || isPawnMoved) {
                    GameStatusController.checkWin();
                    checkStatus();
                	isWallDrop = false;
                	isPawnMoved = false;
                	StartNewGameController.resetTimeToSet();
                	refresh();
                }
                else if (StartNewGameController.timeOver()) {
                    GameStatusController.checkWin();
                    checkStatus();
                    refresh();
                }else {
                    //grey out the next player name & count down time for the current player
                    if (currentPlayer.equals(ModelQuery.getWhitePlayer())) {
                        timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
                    } else if (currentPlayer.equals(ModelQuery.getBlackPlayer())) {
                        timerForBlackPlayer.setText(StartNewGameController.toTimeStr());
                    } else if (currentPlayer.equals(ModelQuery.getRedPlayer())) {
                        timerForRedPlayer.setText(StartNewGameController.toTimeStr());
                    } else if (currentPlayer.equals(ModelQuery.getGreenPlayer())) {
                        timerForGreenPlayer.setText(StartNewGameController.toTimeStr());
                    }

                }
            }
        };

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.25), onFinished));
        timeline.playFromStart();
        refresh();
    }


    public void handleBackToMenu(ActionEvent actionEvent) {
        timeline.stop();
        MusicController.playMainMenu();
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
        Player currentPlayer = ModelQuery.getPlayerToMove();
        Player red = ModelQuery.getRedPlayer();
        Player green = ModelQuery.getGreenPlayer();

        // remove all walls and pawns
        board.getChildren().clear();

        board.getChildren().add(rect1);
        board.getChildren().add(rect2);
        board.getChildren().add(rect3);
        board.getChildren().add(rect4);
        board.getChildren().add(rect5);
        board.getChildren().add(rect6);
        board.getChildren().add(rect7);
        board.getChildren().add(rect8);
        board.getChildren().add(rect9);
        board.getChildren().add(rect10);
        board.getChildren().add(rect11);
        board.getChildren().add(rect12);
        board.getChildren().add(rect13);
        board.getChildren().add(rect14);
        board.getChildren().add(rect15);
        board.getChildren().add(rect16);
        board.getChildren().add(rect17);
        board.getChildren().add(rect18);
        board.getChildren().add(rect19);
        board.getChildren().add(rect20);
        board.getChildren().add(rect21);
        board.getChildren().add(rect22);
        board.getChildren().add(rect23);
        board.getChildren().add(rect24);
        board.getChildren().add(rect25);
        board.getChildren().add(rect26);
        board.getChildren().add(rect27);
        board.getChildren().add(rect28);
        board.getChildren().add(rect29);
        board.getChildren().add(rect30);
        board.getChildren().add(rect31);
        board.getChildren().add(rect32);
        board.getChildren().add(rect33);
        board.getChildren().add(rect34);
        board.getChildren().add(rect35);
        board.getChildren().add(rect36);
        board.getChildren().add(rect37);
        board.getChildren().add(rect38);
        board.getChildren().add(rect39);
        board.getChildren().add(rect40);
        board.getChildren().add(rect41);
        board.getChildren().add(rect42);
        board.getChildren().add(rect43);
        board.getChildren().add(rect44);
        board.getChildren().add(rect45);
        board.getChildren().add(rect46);
        board.getChildren().add(rect47);
        board.getChildren().add(rect48);
        board.getChildren().add(rect49);
        board.getChildren().add(rect50);
        board.getChildren().add(rect51);
        board.getChildren().add(rect52);
        board.getChildren().add(rect53);
        board.getChildren().add(rect54);
        board.getChildren().add(rect55);
        board.getChildren().add(rect56);
        board.getChildren().add(rect57);
        board.getChildren().add(rect58);
        board.getChildren().add(rect59);
        board.getChildren().add(rect60);
        board.getChildren().add(rect61);
        board.getChildren().add(rect62);
        board.getChildren().add(rect63);
        board.getChildren().add(rect64);
        board.getChildren().add(rect65);
        board.getChildren().add(rect66);
        board.getChildren().add(rect67);
        board.getChildren().add(rect68);
        board.getChildren().add(rect69);
        board.getChildren().add(rect70);
        board.getChildren().add(rect71);
        board.getChildren().add(rect72);
        board.getChildren().add(rect73);
        board.getChildren().add(rect74);
        board.getChildren().add(rect75);
        board.getChildren().add(rect76);
        board.getChildren().add(rect77);
        board.getChildren().add(rect78);
        board.getChildren().add(rect79);
        board.getChildren().add(rect80);
        board.getChildren().add(rect81);

        // update player turn
        if (position.getPlayerToMove().equals(white)) {
            whitePlayerName.setFill(Color.BLACK);
            blackPlayerName.setFill(Color.LIGHTGRAY);
            redPlayerName.setFill(Color.LIGHTGRAY);
            greenPlayerName.setFill(Color.LIGHTGRAY);

            whitePlayerName1.setFill(Color.BLACK);
            blackPlayerName1.setFill(Color.LIGHTGRAY);
            redPlayerName1.setFill(Color.LIGHTGRAY);
            greenPlayerName1.setFill(Color.LIGHTGRAY);

            whiteNumOfWalls.setFill(Color.BLACK);
            blackNumOfWalls.setFill(Color.LIGHTGRAY);
            redNumOfWalls.setFill(Color.LIGHTGRAY);
            greenNumOfWalls.setFill(Color.LIGHTGRAY);
        } else if (position.getPlayerToMove().equals(black)){
            whitePlayerName.setFill(Color.LIGHTGRAY);
            blackPlayerName.setFill(Color.BLACK);
            redPlayerName.setFill(Color.LIGHTGRAY);
            greenPlayerName.setFill(Color.LIGHTGRAY);

            whitePlayerName1.setFill(Color.LIGHTGRAY);
            blackPlayerName1.setFill(Color.BLACK);
            redPlayerName1.setFill(Color.LIGHTGRAY);
            greenPlayerName1.setFill(Color.LIGHTGRAY);

            whiteNumOfWalls.setFill(Color.LIGHTGRAY);
            blackNumOfWalls.setFill(Color.BLACK);
            redNumOfWalls.setFill(Color.LIGHTGRAY);
            greenNumOfWalls.setFill(Color.LIGHTGRAY);
        } else if (position.getPlayerToMove().equals(red)){
            whitePlayerName.setFill(Color.LIGHTGRAY);
            blackPlayerName.setFill(Color.LIGHTGRAY);
            redPlayerName.setFill(Color.BLACK);
            greenPlayerName.setFill(Color.LIGHTGRAY);

            whitePlayerName1.setFill(Color.LIGHTGRAY);
            blackPlayerName1.setFill(Color.LIGHTGRAY);
            redPlayerName1.setFill(Color.BLACK);
            greenPlayerName1.setFill(Color.LIGHTGRAY);

            whiteNumOfWalls.setFill(Color.LIGHTGRAY);
            blackNumOfWalls.setFill(Color.LIGHTGRAY);
            redNumOfWalls.setFill(Color.BLACK);
            greenNumOfWalls.setFill(Color.LIGHTGRAY);
        } else {
            whitePlayerName.setFill(Color.LIGHTGRAY);
            blackPlayerName.setFill(Color.LIGHTGRAY);
            redPlayerName.setFill(Color.LIGHTGRAY);
            greenPlayerName.setFill(Color.BLACK);

            whitePlayerName1.setFill(Color.LIGHTGRAY);
            blackPlayerName1.setFill(Color.LIGHTGRAY);
            redPlayerName1.setFill(Color.LIGHTGRAY);
            greenPlayerName1.setFill(Color.BLACK);

            whiteNumOfWalls.setFill(Color.LIGHTGRAY);
            blackNumOfWalls.setFill(Color.LIGHTGRAY);
            redNumOfWalls.setFill(Color.LIGHTGRAY);
            greenNumOfWalls.setFill(Color.BLACK);
        }

        // update walls in stock
        whiteNumOfWalls.setText(String.valueOf(position.getWhiteWallsInStock().size()));
        blackNumOfWalls.setText(String.valueOf(position.getBlackWallsInStock().size()));
        redNumOfWalls.setText(String.valueOf(position.getRedWallsInStock().size()));
        greenNumOfWalls.setText(String.valueOf(position.getGreenWallsInStock().size()));

        // update pawn positions
        placePawn(position.getWhitePosition(),"w");
        placePawn(position.getBlackPosition(),"b");
        if(ModelQuery.isFourPlayer()) {
        	placePawn(position.getRedPosition(),"r");
        	placePawn(position.getGreenPosition(),"g");
        }

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

        //update game status and stop timer
        if (state == PlayerState.WHITEWON) {
            timeline.stop();
            changePage("/fxml/EndScene.fxml");
        }
        else if (state == PlayerState.BLACKWON) {
            timeline.stop();
            changePage("/fxml/EndScene.fxml");
        }
        else if (state == PlayerState.REDWON) {
            timeline.stop();
            changePage("/fxml/EndScene.fxml");
        }
        else if (state == PlayerState.GREENWON) {
            timeline.stop();
            changePage("/fxml/EndScene.fxml");
        }
    }

    private void placePawn(PlayerPosition position, String color){
        Tile tile = position.getTile();

        Pair<Integer,Integer> coord = convertPawnToCanvas(tile.getRow(),tile.getColumn());

        Circle pawn = new Circle();
        if(color.equals("w")){
            pawn.setFill(Color.WHITE);
            pawn.setStroke(Color.BLACK);

        }
        else if(color.equals("b")){
            pawn.setFill(Color.BLACK);
        }
        else if(color.equals("r")) {
        	pawn.setFill(Color.RED);
        	pawn.setStroke(Color.BLACK);
        }
        else {
        	pawn.setFill(Color.GREEN);
        	pawn.setStroke(Color.BLACK);
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
        if(code.equals(KeyCode.DIGIT1)) {
            if (state == PlayerState.WALL) {
                WallController.cancelWallMove();
                state = PlayerState.IDLE;
            }
            //
            else if (WallController.grabWall()) {
                state = PlayerState.WALL;
            } else {
                AlertHelper.newPopUpWindow(Alert.AlertType.ERROR,"Alert","NO MORE WALLS IN STOCK");
                System.out.println("No more walls");
            }
        }
        if (code.equals(KeyCode.DIGIT2)) {
            state = PlayerState.PAWN;
            WallController.cancelWallMove();
        }


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
                    isWallDrop=true;
                }
                else{
                    AlertHelper.newPopUpWindow(Alert.AlertType.ERROR, "Alert", "INVALID WALL PLACEMENT");
                }
            }
            else if(code.equals(KeyCode.R)){
                WallController.rotateWall();
            }
            refresh();

        }

        if(code.equals(KeyCode.H)){
            showHint();
        }

        if (state==PlayerState.PAWN){
            /*For handling pawn move*/


            if (code.equals(KeyCode.I)) {
                    pawnMoved = PawnController.movePawn("up");
                }
                else if (code.equals(KeyCode.K)) {
                	pawnMoved = PawnController.movePawn("down");
                }
                else if (code.equals(KeyCode.J)) {
                	pawnMoved = PawnController.movePawn("left");
                }
                else if (code.equals(KeyCode.L)) {
                	pawnMoved = PawnController.movePawn("right");
                }
                else if (code.equals(KeyCode.U)) {
                	pawnMoved = PawnController.movePawn("upleft");
                }
                else if (code.equals(KeyCode.O)) {
                	pawnMoved = PawnController.movePawn("upright");
                }
                else if (code.equals(KeyCode.N)) {
                	pawnMoved = PawnController.movePawn("downleft");
                }
                else if (code.equals(KeyCode.COMMA)) {
                	pawnMoved = PawnController.movePawn("downright");
                }
            refresh();
        }
    }

    private void showHint(){
        if(!ModelQuery.isFourPlayer()){
            String bestTile = WallController.findOptimalPath();
            String message = "HINT: Your shortest path is through " + bestTile;
            AlertHelper.newPopUpWindow(Alert.AlertType.INFORMATION,"Hint",message);
        }else{
            AlertHelper.newPopUpWindow(Alert.AlertType.INFORMATION,"Hint","Hints are not supported for 4 player mode");
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
    
    public void handleSaveGame(ActionEvent actionEvent) {
        String filename;
        TextInputDialog textInput = new TextInputDialog();

        textInput.setTitle("Saving game");
        textInput.getDialogPane().setContentText("Name of save file");

        TextField input = textInput.getEditor();
        textInput.showAndWait();

        if(input.getText() != null && input.getText().length() != 0) {
            filename = input.getText();

            if (!SaveLoadGameController.fileSave(filename +".mov")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                if (!SaveLoadGameController.isSaveMoveValid)
                    errorAlert.setContentText("The current game save is invalid");
                else
                    errorAlert.setContentText("There was an error in saving your game");
                errorAlert.setHeaderText("Error in loading Game");
                errorAlert.showAndWait();
            }
            else{
                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setContentText("Game is successfully saved in: " +filename +".mov");
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
