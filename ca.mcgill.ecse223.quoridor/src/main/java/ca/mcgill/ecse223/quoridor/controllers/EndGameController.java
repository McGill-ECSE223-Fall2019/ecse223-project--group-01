package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

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

}
