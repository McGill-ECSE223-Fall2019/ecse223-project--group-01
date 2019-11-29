package ca.mcgill.ecse223.quoridor.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.PositionController;
import ca.mcgill.ecse223.quoridor.controllers.SaveLoadGameController;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class ReplayModeController extends ViewController{
	
	private int stepNumber = 0;
	List<GamePosition> listPositions;
	
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
	
    
	public void initialize() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(Main.getCurrentStage());
        if (file == null) {
        	changePage("/fxml/Menu.fxml");
        } else {
    		whitePlayerName.setText(file.getName());
			try {
				SaveLoadGameController.fileLoad(file.getName(), "xx", "yy");
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
		}   
        
        //listPositions = ModelQuery.getCurrentGame().getPositions();
		//refresh();
	}

	
	@FXML
	public void handleForwardStep(ActionEvent actionEvent) {
		stepNumber++;
		refresh();
	}
	
	@FXML
	public void handleBackStep(ActionEvent actionEvent) {
		//stepNumber--;
		refresh();
	}
	
	@FXML
	public void handleStartSkip(ActionEvent actionEvent) {
		stepNumber=0;
		refresh();
	}
	
	@FXML
	public void handleEndSkip(ActionEvent actionEvent) {
		stepNumber=listPositions.size()-1;
		refresh();
	}
	
    public void refresh() { 	
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
        changePage("/fxml/Menu.fxml");
    }
}
