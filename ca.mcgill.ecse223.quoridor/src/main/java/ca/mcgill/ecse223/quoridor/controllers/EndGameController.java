package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

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
//        long blackRemainingTime = Long.parseLong(ModelQuery.getBlackPlayer().getRemainingTime().toString());
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

    //TODO: disable the button... unbind the stuff

}
