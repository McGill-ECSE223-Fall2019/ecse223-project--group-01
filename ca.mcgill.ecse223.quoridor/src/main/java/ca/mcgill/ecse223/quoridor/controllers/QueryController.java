package ca.mcgill.ecse223.quoridor.controllers;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.util.List;

public class QueryController {

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
}
