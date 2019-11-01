package ca.mcgill.ecse223.quoridor.view;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static java.awt.event.KeyEvent.*;


public class InitializeBoardController extends ViewController{

    @FXML
    private AnchorPane board;
    Rectangle wall;

    public void handleBackToMenu(ActionEvent actionEvent) {
            changePage("/fxml/Menu.fxml");
    }

    public void createNewWall(ActionEvent actionEvent) {
        try {
            wall = new Rectangle(206, 86, 9, 77);
            wall.setArcWidth(5);
            wall.setArcHeight(5);
            wall.setFill(Color.web("#5aff1e"));
            wall.setStroke(Color.web("#000000"));
            wall.setStrokeWidth(1.5);
            wall.setStrokeType(StrokeType.INSIDE);
            board.getChildren().add(wall);
        } catch (Exception e){
            System.out.println("Error");
        }
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
