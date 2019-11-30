package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class ResignGameController {

    public static void setWinner(Player player) throws UnsupportedOperationException{

        Quoridor quoridor = QuoridorApplication.getQuoridor();

//        quoridor.getCurrentGame().getWhitePlayer();
       ModelQuery.getCurrentGame().setWinningPlayer(player);

       if (player.getNextPlayer().equals( "white")){
           quoridor.getCurrentGame().setGameStatus(Game.GameStatus.BlackWon);
       }
       else  if (player.getNextPlayer().equals( "black")){
            quoridor.getCurrentGame().setGameStatus(Game.GameStatus.WhiteWon);
        }

    }
}
