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

        if(moves.size()<9){
            return false;
        }

        Move p11 = ModelQuery.getCurrentGame().getMove(moves.size()-1);
        Move p12 = ModelQuery.getCurrentGame().getMove(moves.size()-2);
        Move p21 = ModelQuery.getCurrentGame().getMove(moves.size()-3);
        Move p22 = ModelQuery.getCurrentGame().getMove(moves.size()-4);

        int dup_p11 = 0;
        int dup_p12 = 0;
        int dup_p21 = 0;
        int dup_p22 = 0;

        for(Move move : moves){
            if(move instanceof WallMove){
                return false;
            }
            if(p11.getTargetTile().equals(move.getTargetTile())){
                dup_p11++;
            }
            if(p12.getTargetTile().equals(move.getTargetTile())){
                dup_p12++;
            }
            if(p21.getTargetTile().equals(move.getTargetTile())){
                dup_p21++;
            }
            if(p22.getTargetTile().equals(move.getTargetTile())){
                dup_p22++;
            }
        }
        if(dup_p11==3 && dup_p12 == 2 && dup_p21==2 && dup_p22==2){
            ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.Draw);
            ModelQuery.getCurrentPosition().setPlayerToMove(null);
            return true;
        }else{
            return false;
        }
    }
}
