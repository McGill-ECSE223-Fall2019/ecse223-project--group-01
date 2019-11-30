package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class ResignGameController {

    /**
     * @author Jason Lau
     *
     * This method set the game state to either WhiteWon or BlackWon depends on the player who resigns
     * @throws UnsupportedOperationException
     */
    public static void resign() throws UnsupportedOperationException{

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Player player = ModelQuery.getPlayerToMove();

       if (player.getNextPlayer().equals( "white")){
           quoridor.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
       }
       else  if (player.getNextPlayer().equals( "black")){
            quoridor.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
        }

    }
}
