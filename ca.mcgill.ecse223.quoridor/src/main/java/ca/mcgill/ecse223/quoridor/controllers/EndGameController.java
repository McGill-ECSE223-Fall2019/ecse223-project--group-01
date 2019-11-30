package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.view.InitializeBoardController;
import jdk.javadoc.internal.tool.Start;

import java.util.Date;

public class EndGameController {

    public EndGameController() {}

    public static String checkPawnPosition(String player, int row, int col) {
        String result = "";

        Player currentPlayer = ModelQuery.getPlayerToMove();
        Player whitePlayer = ModelQuery.getWhitePlayer();
        Player blackPlayer = ModelQuery.getBlackPlayer();

        Tile whiteTile = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        Tile blackTile = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();

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

    public static String checkGameStatus(Player player) {
        String result = "";

        Player currentPlayer = ModelQuery.getPlayerToMove();

        Tile whiteTile = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        Tile blackTile = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();

        Player whitePlayer = ModelQuery.getWhitePlayer();
        Player blackPlayer = ModelQuery.getBlackPlayer();
        if (player.equals(whitePlayer) && StartNewGameController.timeToSet > 0) {

            if (whiteTile.getRow() == 1 && whiteTile.getColumn() <= 9 && whiteTile.getColumn() >= 1) {
                result = "whiteWon";
            } else {
                result = "pending";
            }
        }
        else if (player.equals(blackPlayer) && StartNewGameController.timeToSet > 0) {
            if (blackTile.getRow() == 9 && blackTile.getColumn() <= 9 && blackTile.getColumn() >= 1) {
                result = "blackWon";
            } else {
                result = "pending";
            }
        }
        else if (StartNewGameController.timeToSet <= 0) {
            if (currentPlayer.equals(whitePlayer)) {
                result = "blackWon";
            }
            else if (currentPlayer.equals(blackPlayer)) {
                result = "whiteWon";
            }
        }

        return result;
    }

}
