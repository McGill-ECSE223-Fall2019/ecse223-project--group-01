package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.WallMove;

import java.util.List;

public class GameStatusController {
    public static boolean checkGameStatus(){
        // TODO check for draw
        return checkDraw();

        // TODO check for win
    }


    public static boolean checkDraw(){
        List<Move> moves = ModelQuery.getCurrentGame().getMoves();
        Move last_move = ModelQuery.getCurrentGame().getMove(moves.size()-1);


        // TODO stop the clock
        if(last_move instanceof WallMove){
            return false;
        }

        int num_duplicates = 0;
        for(Move move : moves){
            if(last_move.getTargetTile().equals(move.getTargetTile())){
                num_duplicates++;
            }
        }
        if(num_duplicates>=2){
            ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.Draw);
            ModelQuery.getCurrentPosition().setPlayerToMove(null);
            return true;
        }else{
            return false;
        }
    }
}
