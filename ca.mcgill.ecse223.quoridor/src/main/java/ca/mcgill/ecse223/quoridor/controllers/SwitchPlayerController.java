package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;

public class SwitchPlayerController {
	
	/**@author: Mark Zhu
	 * 
	 * This method switches the active and passive players
	 * @param currentPlayerColor The color of the player that starts as active
	 * @return A string representing the color of the player that ends as active
	 */
	
	public static void switchActivePlayer() {
		/*
		Player whitePlayer = ModelQuery.getWhitePlayer();
		Player blackPlayer = ModelQuery.getBlackPlayer();
		Player redPlayer = ModelQuery.getRedPlayer();
		Player greenPlayer = ModelQuery.getGreenPlayer();
		
		if (currentPlayer.equals(whitePlayer)) {
			//ModelQuery.getWhitePlayer().setRemainingTime(new Time(ModelQuery.getWhitePlayer().getRemainingTime().getTime()-timeSpent.toMillis()));			
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());
		} else if (currentPlayer.equals(blackPlayer)) {
			//ModelQuery.getBlackPlayer().setRemainingTime(new Time(ModelQuery.getBlackPlayer().getRemainingTime().getTime()-timeSpent.toMillis()));
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());
		} else if (currentPlayer.equals(redPlayer)) {
			//ModelQuery.getBlackPlayer().setRemainingTime(new Time(ModelQuery.getRedPlayer().getRemainingTime().getTime()-timeSpent.toMillis()));
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());
		} else {
			//ModelQuery.getBlackPlayer().setRemainingTime(new Time(ModelQuery.getGreenPlayer().getRemainingTime().getTime()-timeSpent.toMillis()));
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());
		}
		*/
		
		Player currentPlayer = ModelQuery.getPlayerToMove();
		//switch active player
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());
		//record current gamestate
		ModelQuery.getCurrentGame().addPosition(ModelQuery.getCurrentPosition());
	}
	
	/*@author: Mark Zhu
	 * 
	 * This method makes a deep copy of the current game position, for storing purposes (for replay)
	 * @return A deep copy of the current game position
	 */
	public static GamePosition deepCopyPosition() {
		GamePosition current = ModelQuery.getCurrentPosition();
		
		int id = ModelQuery.getCurrentGame().getPositions().size()+1;
		PlayerPosition whitePlayerPos =  new PlayerPosition(ModelQuery.getCurrentGame().getWhitePlayer(), current.getWhitePosition().getTile());
		PlayerPosition blackPlayerPos =  new PlayerPosition(ModelQuery.getCurrentGame().getBlackPlayer(), current.getBlackPosition().getTile());
		Player nextPlayer = current.getPlayerToMove().getNextPlayer();
		
		GamePosition clone = new GamePosition(id, whitePlayerPos, blackPlayerPos, nextPlayer, ModelQuery.getCurrentGame());
		
		
		return clone;
	}
}
