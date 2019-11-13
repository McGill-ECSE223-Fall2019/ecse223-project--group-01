package ca.mcgill.ecse223.quoridor.controllers;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.util.ArrayList;
import java.util.List;

public class ModelQuery {

    public static Game getCurrentGame(){
        return QuoridorApplication.getQuoridor().getCurrentGame();
    }

    public static Player getPlayerToMove(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
    }

    public static Player getWhitePlayer(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
    }

    public static Player getBlackPlayer(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
    }
    
    public static Player getRedPlayer() {
    	return QuoridorApplication.getQuoridor().getCurrentGame().getRedPlayer();
    }
    
    public static Player getGreenPlayer() {
    	return QuoridorApplication.getQuoridor().getCurrentGame().getGreenPlayer();
    }
    
    public static int getNumberPlayers() {
    	return QuoridorApplication.getQuoridor().getCurrentGame().getPlayers().size();
    }

    public static Board getBoard(){
        return QuoridorApplication.getQuoridor().getBoard();
    }

    public static List<Move> getMoves(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getMoves();
    }

    public static WallMove getWallMoveCandidate(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
    }

    public static List<Tile> getTiles(){
        return QuoridorApplication.getQuoridor().getBoard().getTiles();
    }

    public static Tile getTile(int row,int col){
        int index = (row - 1) * 9 + col - 1;
        return QuoridorApplication.getQuoridor().getBoard().getTile(index);
    }

    public static List<Wall> getAllWallsOnBoard(){
        List<Wall> placedWalls = new ArrayList<>();
        
    	List<Wall> whiteWalls = ModelQuery.getWhiteWallsOnBoard();
        List<Wall> blackWalls = ModelQuery.getBlackWallsOnBoard();
        List<Wall> redWalls;
        List<Wall> greenWalls;
        if (getNumberPlayers() == 4) {
            redWalls = ModelQuery.getRedWallsOnBoard();
            greenWalls = ModelQuery.getGreenWallsOnBoard();
        	placedWalls.addAll(redWalls);
        	placedWalls.addAll(greenWalls);
        }

        placedWalls.addAll(whiteWalls);
        placedWalls.addAll(blackWalls);

        return placedWalls;
    }

    public static List<Wall> getWhiteWallsOnBoard(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
    }

    public static List<Wall> getBlackWallsOnBoard(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
    }
    
    public static List<Wall> getRedWallsOnBoard(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getRedWallsOnBoard();
    }
    
    public static List<Wall> getGreenWallsOnBoard(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getGreenWallsOnBoard();
    }

    public static GamePosition getCurrentPosition(){
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
    }



}
