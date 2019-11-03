package ca.mcgill.ecse223.quoridor.view;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import ca.mcgill.ecse223.quoridor.model.Player;
import javafx.animation.KeyFrame;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.PointLight;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class InitializeBoardController extends ViewController{

    @FXML
    private AnchorPane board;
    Rectangle wall;
    public Text whitePlayerName;
    public Text blackPlayerName;
    public Text whitePlayerName1;
    public Text blackPlayerName1;
    public Label timerForWhitePlayer;
    public Label timerForBlackPlayer;
    public Timeline timeline;


    public void initialize() {
        whitePlayerName.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName.setText(ModelQuery.getBlackPlayer().getUser().getName());
        blackPlayerName.setFill(Color.LIGHTGRAY);       //white player's turn when the game starts
        whitePlayerName1.setText(ModelQuery.getWhitePlayer().getUser().getName());
        blackPlayerName1.setText(ModelQuery.getBlackPlayer().getUser().getName());
        StartNewGameController.startTheClock();
        timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
        timerForBlackPlayer.setText(StartNewGameController.toTimeStr());


        if (timeline != null) {
            timeline.stop();
        }
        // update timerLabel
        timerForWhitePlayer.setText(StartNewGameController.toTimeStr());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Player currentPlayer = ModelQuery.getPlayerToMove();
                if (currentPlayer.getRemainingTime().getTime() <= 0) {
                    System.out.println("stop");
                    timeline.stop();
                } else {
                    timerForWhitePlayer.setText(StartNewGameController.toTimeStr());

                }
            }
        };
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), onFinished));
        timeline.playFromStart();
    }

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
