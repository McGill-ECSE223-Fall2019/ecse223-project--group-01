package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;

import java.util.Date;

public class EndGameController {

    public EndGameController() {}

    /**
     *
     * @return player that wins the game
     */
    public static Player endGame() {
        Player player = null;

        return player;
    }

    /**
     * Updates game status. If black player won, update status to BlackWon
     * If white player won, update status to WhiteWon
     */
    public static void GameNotRun() {
        if (endGame().equals(ModelQuery.getBlackPlayer())) {        //black player wins
            ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
        } else {
            ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
        }

    }

    public static String checkPawnPosition(String player, int row, int col) {
        String result = "";

        Date blackDate = new Date();
        blackDate = (Date) ModelQuery.getBlackPlayer().getRemainingTime();
        Date whiteDate = new Date();
        whiteDate = (Date) ModelQuery.getWhitePlayer().getRemainingTime();
        long blackRemainingTime = blackDate.getTime();
        long whiteRemainingTime = whiteDate.getTime();

        if (player.equals("white") && whiteRemainingTime!= 0) {
            if (row == 9 && col <= 9 && col >= 1) {
                result = "whiteWon";
            } else {
                result = "pending";
            }
        }
        else if (player.equals("black") && blackRemainingTime != 0) {
            if (row == 1 && col <= 9 && col >= 1) {
                result = "blackWon";
            } else {
                result = "pending";
            }
        }
        else if (blackRemainingTime <= 0) {
            result = "whiteWon";
        }
        else if (whiteRemainingTime <= 0) {
            result = "blackWon";
        }

        return result;
    }

    public static String checkGameStatus(Player player) {
        String result = "";

        Date blackDate = new Date();
        Date whiteDate = new Date();
        blackDate = (Date) ModelQuery.getBlackPlayer().getRemainingTime();
        whiteDate = (Date) ModelQuery.getWhitePlayer().getRemainingTime();
        long blackRemainingTime = blackDate.getTime();
        long whiteRemainingTime = whiteDate.getTime();
        Tile whiteTile = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        Tile blackTile = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();

        Player whitePlayer = ModelQuery.getWhitePlayer();
        Player blackPlayer = ModelQuery.getBlackPlayer();

        if (player.equals(whitePlayer) && whiteRemainingTime!= 0) {
            if (whiteTile.getRow() == 1 && whiteTile.getColumn() <= 9 && whiteTile.getColumn() >= 1) {
                result = "whiteWon";
            } else {
                result = "pending";
            }
        }
        else if (player.equals(blackPlayer) && blackRemainingTime != 0) {
            if (blackTile.getRow() == 9 && blackTile.getColumn() <= 9 && blackTile.getColumn() >= 1) {
                result = "blackWon";
            } else {
                result = "pending";
            }
        }
        else if (blackRemainingTime <= 0) {
            result = "whiteWon";
        }
        else if (whiteRemainingTime <= 0) {
            result = "blackWon";
        }

        return result;
    }
    //TODO: disable the button... unbind the stuff

}
