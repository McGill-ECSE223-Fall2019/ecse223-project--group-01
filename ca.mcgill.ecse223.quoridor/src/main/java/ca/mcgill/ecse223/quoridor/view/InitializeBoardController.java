package ca.mcgill.ecse223.quoridor.view;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.WallController;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import ca.mcgill.ecse223.quoridor.controllers.BoardController;
import javafx.util.Pair;


import java.util.List;

import static java.awt.event.KeyEvent.*;


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


    public void handleBackToMenu(ActionEvent actionEvent) {
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
    }
}
