package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Wall;

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
		ModelQuery.getCurrentGame().addPosition(deepCopyPosition());
	}
	
	/*@author: Mark Zhu
	 * 
	 * This method makes a deep copy of the current game position, for storing purposes (for replay)
	 * @return A deep copy of the current game position
	 */
	public static GamePosition deepCopyPosition() {
		GamePosition current = ModelQuery.getCurrentPosition();
		
		int id = ModelQuery.getCurrentGame().getPositions().size()+1;
		int wallCounter = 0;
		PlayerPosition whitePlayerPos =  new PlayerPosition(ModelQuery.getCurrentGame().getWhitePlayer(), current.getWhitePosition().getTile());
		PlayerPosition blackPlayerPos =  new PlayerPosition(ModelQuery.getCurrentGame().getBlackPlayer(), current.getBlackPosition().getTile());
		PlayerPosition redPlayerPos;
		PlayerPosition greenPlayerPos;
		Player nextPlayer = current.getPlayerToMove().getNextPlayer();
		
		GamePosition clone = new GamePosition(id, whitePlayerPos, blackPlayerPos, nextPlayer, ModelQuery.getCurrentGame());
		
		for(Wall whiteWall: ModelQuery.getWhiteWallsOnBoard()) {
			Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getWhitePlayer());
			wallClone.setMove(whiteWall.getMove());
			wallClone.setOwner(ModelQuery.getWhitePlayer());
			clone.addWhiteWallsOnBoard(wallClone);
		}
		
		for(Wall blackWall: ModelQuery.getBlackWallsOnBoard()) {
			Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getBlackPlayer());
			wallClone.setMove(blackWall.getMove());
			wallClone.setOwner(ModelQuery.getBlackPlayer());
			clone.addBlackWallsOnBoard(wallClone);
		}
		
		for(Wall whiteWall: ModelQuery.getCurrentPosition().getWhiteWallsInStock()) {
			Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getWhitePlayer());
			wallClone.setMove(whiteWall.getMove());
			wallClone.setOwner(ModelQuery.getWhitePlayer());
			clone.addWhiteWallsInStock(wallClone);
		}
		
		for(Wall blackWall: ModelQuery.getCurrentPosition().getBlackWallsOnBoard()) {
			Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getBlackPlayer());
			wallClone.setMove(blackWall.getMove());
			wallClone.setOwner(ModelQuery.getBlackPlayer());
			clone.addBlackWallsInStock(wallClone);
		}
		
		if(ModelQuery.isFourPlayer()) {
			redPlayerPos = new PlayerPosition(ModelQuery.getCurrentGame().getRedPlayer(), current.getRedPosition().getTile());
			greenPlayerPos = new PlayerPosition(ModelQuery.getCurrentGame().getGreenPlayer(), current.getGreenPosition().getTile());
			clone.setRedPosition(redPlayerPos);
			clone.setGreenPosition(greenPlayerPos);
			
			for(Wall redWall: ModelQuery.getRedWallsOnBoard()) {
				Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getRedPlayer());
				wallClone.setMove(redWall.getMove());
				wallClone.setOwner(ModelQuery.getRedPlayer());
				clone.addRedWallsOnBoard(wallClone);
			}
			
			for(Wall greenWall: ModelQuery.getGreenWallsOnBoard()) {
				Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getGreenPlayer());
				wallClone.setMove(greenWall.getMove());
				wallClone.setOwner(ModelQuery.getGreenPlayer());
				clone.addGreenWallsOnBoard(wallClone);
			}
			
			for(Wall redWall: ModelQuery.getCurrentPosition().getRedWallsInStock()) {
				Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getRedPlayer());
				wallClone.setMove(redWall.getMove());
				wallClone.setOwner(ModelQuery.getRedPlayer());
				clone.addRedWallsInStock(wallClone);
			}
			
			for(Wall greenWall: ModelQuery.getCurrentPosition().getGreenWallsOnBoard()) {
				Wall wallClone = new Wall(id*40+wallCounter++, ModelQuery.getGreenPlayer());
				wallClone.setMove(greenWall.getMove());
				wallClone.setOwner(ModelQuery.getGreenPlayer());
				clone.addGreenWallsInStock(wallClone);
			}
		}
		
		return clone;
	}
}
