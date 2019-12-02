package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controllers.*;
import ca.mcgill.ecse223.quoridor.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

import java.util.ArrayList;
import java.util.List;


public class ReplayModeController extends ViewController{

	int stepNumber = 0;
	List<GamePosition> listPositions = ModelQuery.getCurrentGame().getPositions();
	
    public enum PlayerState {WALL, PAWN, IDLE};
    public PlayerState state = PlayerState.IDLE;

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
    public static boolean pawnMoved = false;
    public String initialTime;

    public Circle c1;
    public Circle c2;
    public Rectangle r1;
    public Rectangle r2;
    public Text x1;
    public Text x2;


    public void initialize() {
    	//System.out.println("NUMBER OF POSITIONS " + listPositions.size());
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


        //record the time set per turn
        initialTime = StartNewGameController.toTimeStr();

    	timerForWhitePlayer.setText(initialTime);
    	timerForBlackPlayer.setText(initialTime);

    	if(ModelQuery.isFourPlayer()) {
    		timerForRedPlayer.setText(initialTime);
    		timerForGreenPlayer.setText(initialTime);
    	}

        state = PlayerState.IDLE;
        refresh();
        //switchTimer();
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
        //GamePosition position = ModelQuery.getCurrentPosition();
    	GamePosition position = listPositions.get(stepNumber);
        Player white = ModelQuery.getWhitePlayer();
        Player black = ModelQuery.getBlackPlayer();
        Player red = ModelQuery.getRedPlayer();
        Player green = ModelQuery.getGreenPlayer();

        // remove all walls and pawns
        board.getChildren().clear();

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
        List<Wall> walls = new ArrayList<>();
        walls.addAll(position.getWhiteWallsOnBoard());
        walls.addAll(position.getBlackWallsOnBoard());
        
        for (Wall wall : walls) {
            placeWall(wall.getMove(), false);
        }

        // update wall move candidate
        if(ModelQuery.getWallMoveCandidate()!=null){
            WallMove move = ModelQuery.getWallMoveCandidate();
            placeWall(move, true);
        }
        
        //System.out.println("step number " + stepNumber);
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
            /*if (ModelQuery.getPlayerToMove().equals(ModelQuery.getWhitePlayer())) {
            	rectangle.setFill(Color.web("#dde8f2"));
            } else if (ModelQuery.getPlayerToMove().equals(ModelQuery.getBlackPlayer())){
            	rectangle.setFill(Color.BLACK);
            } else if (ModelQuery.getPlayerToMove().equals(ModelQuery.getRedPlayer())) {
            	rectangle.setFill(Color.RED);
            } else if (ModelQuery.getPlayerToMove().equals(ModelQuery.getGreenPlayer())) {
            	rectangle.setFill(Color.LIGHTGREEN);
            }    */
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
	public void handleForwardStep(ActionEvent actionEvent) {
		if(stepNumber<listPositions.size()-1) {
			stepNumber++;
			if(stepNumber==1) stepNumber++;
		}
		refresh();
	}
	
	@FXML
	public void handleBackStep(ActionEvent actionEvent) {
		if(stepNumber>0) {
			stepNumber--;
			if(stepNumber==1) stepNumber--;
		}
		refresh();
	}
	
	@FXML
	public void handleStartSkip(ActionEvent actionEvent) {
		stepNumber=0;
		refresh();
	}
	
	@FXML
	public void handleEndSkip(ActionEvent actionEvent) {
		stepNumber=1;
		refresh();
	}
	
	@FXML
	public void handleResume(ActionEvent actionEvent) {	
		changePage("/fxml/InitializeBoard.fxml");
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
    
    public void handleBackToMenu(ActionEvent actionEvent) {
    	timeline.stop();
    	ModelQuery.getCurrentGame().delete(); //delete the previous game
        changePage("/fxml/Menu.fxml");
    }
}
