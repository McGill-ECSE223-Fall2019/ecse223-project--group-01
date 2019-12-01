package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.*;

import java.util.List;

public class GameStatusController {
    public static void checkGameStatus(){
        checkDraw();
        checkWin();
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

    public static void checkWin() {
        Player player = ModelQuery.getPlayerToMove();

        Tile whiteTile = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        Tile blackTile = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();

        Player whitePlayer = ModelQuery.getWhitePlayer();
        Player blackPlayer = ModelQuery.getBlackPlayer();

        if (!ModelQuery.isFourPlayer()) {
            if (ModelQuery.getWhitePlayer().getRemainingTime().getTime() <= 0) {

                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
            }else if (ModelQuery.getBlackPlayer().getRemainingTime().getTime() <=0) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);

            }
            else if (player.getNextPlayer().equals(whitePlayer) && ModelQuery.getWhitePlayer().getRemainingTime().getTime() > 0) {
                if (whiteTile.getRow() == whitePlayer.getDestination().getTargetNumber()) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
                }
            }else if (player.getNextPlayer().equals(blackPlayer) && ModelQuery.getBlackPlayer().getRemainingTime().getTime() > 0) {
                if (blackTile.getRow() == blackPlayer.getDestination().getTargetNumber()) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
                }
            }

        }
        else {
            Player redPlayer = ModelQuery.getRedPlayer();
            Player greenPlayer = ModelQuery.getGreenPlayer();

            Tile redTile = ModelQuery.getCurrentGame().getCurrentPosition().getRedPosition().getTile();
            Tile greenTile = ModelQuery.getCurrentGame().getCurrentPosition().getGreenPosition().getTile();

            if (ModelQuery.getWhitePlayer().getRemainingTime().getTime() <= 0) {
                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
            }
            else if (ModelQuery.getBlackPlayer().getRemainingTime().getTime() <= 0) {
                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
            }
            else if (ModelQuery.getGreenPlayer().getRemainingTime().getTime() <= 0) {
                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.RedWon);
            }
            else if (ModelQuery.getRedPlayer().getRemainingTime().getTime() <= 0) {
                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.GreenWon);
            }

            else if (player.getNextPlayer().equals(whitePlayer) && ModelQuery.getWhitePlayer().getRemainingTime().getTime() > 0) {
                if (whiteTile.getRow() == whitePlayer.getDestination().getTargetNumber()) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
                }
            }else if (player.getNextPlayer().equals(blackPlayer) && ModelQuery.getBlackPlayer().getRemainingTime().getTime() > 0) {
                if (blackTile.getRow() == blackPlayer.getDestination().getTargetNumber()) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
                }
            }
            else if (player.getNextPlayer().equals(redPlayer) && ModelQuery.getRedPlayer().getRemainingTime().getTime() > 0) {
                if (redTile.getRow() == redPlayer.getDestination().getTargetNumber()) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.RedWon);
                }
            }
            else if (player.getNextPlayer().equals(greenPlayer) && ModelQuery.getGreenPlayer().getRemainingTime().getTime() > 0) {
                if (greenTile.getRow() == greenPlayer.getDestination().getTargetNumber()) {
                    ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.GreenWon);
                }
            }
        }
    }
}
