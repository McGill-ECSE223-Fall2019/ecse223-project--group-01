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
        List<Wall> whiteWalls = ModelQuery.getWhiteWallsOnBoard();
        List<Wall> blackWalls = ModelQuery.getBlackWallsOnBoard();

        List<Wall> placedWalls = new ArrayList<>();
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



}
