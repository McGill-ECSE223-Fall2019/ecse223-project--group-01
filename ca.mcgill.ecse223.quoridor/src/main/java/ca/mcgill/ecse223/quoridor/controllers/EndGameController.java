package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Tile;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.Date;

public class EndGameController {

    public EndGameController() {}

    public static String checkPawnPosition(String player, int row, int col) {
        String result = "";

        Date whiteTime = new Date();
        Date blackTime = new Date();
        whiteTime = (Date) ModelQuery.getWhitePlayer().getRemainingTime();
        blackTime = (Date) ModelQuery.getBlackPlayer().getRemainingTime();
        long whiteRemainingTime = whiteTime.getTime();
        long blackRemainingTime = blackTime.getTime();

        if (player.equals("white") && whiteRemainingTime > 0) {
            if (row == 9 && col <= 9 && col >= 1) {
                result = "whiteWon";
            } else {
                result = "pending";
            }
        }
        else if (player.equals("black") && blackRemainingTime > 0) {
            if (row == 1 && col <= 9 && col >= 1) {
                result = "blackWon";
            } else {
                result = "pending";
            }
        }
        else if (whiteRemainingTime <= 0) {
            result = "blackWon";
        }
        else if (blackRemainingTime <= 0) {
            result = "whiteWon";

        }
        return result;
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
            else if (ModelQuery.getRedPlayer().getRemainingTime().getTime() <= 0) {
                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
            }
            else if (ModelQuery.getGreenPlayer().getRemainingTime().getTime() <= 0) {
                ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
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
