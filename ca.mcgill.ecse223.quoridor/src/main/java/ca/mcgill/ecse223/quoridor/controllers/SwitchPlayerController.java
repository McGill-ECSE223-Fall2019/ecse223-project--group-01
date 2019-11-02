package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Player;

public class SwitchPlayerController {
	
	/**@author: Mark Zhu
	 * 
	 * This method switches the active and passive players
	 * @param currentPlayerColor The color of the player that starts as active
	 * @return A string representing the color of the player that ends as active
	 */
	
	public static String SwitchActivePlayer(String currentPlayerColor) {
		Player whitePlayer = ModelQuery.getWhitePlayer();
		Player blackPlayer = ModelQuery.getBlackPlayer();

		//Player currentPlayer = ModelQuery.getPlayerToMove();
		
		if (currentPlayerColor.equals("white")) {
			//ModelQuery.getWhitePlayer().setRemainingTime(new Time(ModelQuery.getWhitePlayer().getRemainingTime().getTime()-timeSpent.toMillis()));
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(blackPlayer);
			return "black";
		} else if (currentPlayerColor.equals("black")) {
			//ModelQuery.getBlackPlayer().setRemainingTime(new Time(ModelQuery.getBlackPlayer().getRemainingTime().getTime()-timeSpent.toMillis()));
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(whitePlayer);
			return "white";
		} else {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
