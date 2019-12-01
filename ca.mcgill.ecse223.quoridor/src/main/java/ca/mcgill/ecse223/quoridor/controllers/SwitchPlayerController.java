package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public class SwitchPlayerController {
	
	/**@author: Mark Zhu
	 * 
	 * This method switches the active and passive players
	 * @param currentPlayerColor The color of the player that starts as active
	 * @return A string representing the color of the player that ends as active
	 */
	
	private static int id = 100;
	
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
		
		//record current gamestate
		ModelQuery.getCurrentGame().addPosition(deepCopyPosition());
		
		Player currentPlayer = ModelQuery.getPlayerToMove();
		//switch active player
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());

		//Wall d = ModelQuery.getWhiteWallsOnBoard().get(0);
		//System.out.println(d.getMove());
	}
	
	/*@author: Mark Zhu
	 * 
	 * This method makes a deep copy of the current game position, for storing purposes (for replay)
	 * @return A deep copy of the current game position
	 */
	public static GamePosition deepCopyPosition() {
		
		//throw new RuntimeException("c "+ModelQuery.getCurrentPosition().getWhiteWallsInStock().size());
		GamePosition current = ModelQuery.getCurrentPosition();
		
		int wallCounter = 0;
		PlayerPosition whitePlayerPos =  new PlayerPosition(ModelQuery.getCurrentGame().getWhitePlayer(), current.getWhitePosition().getTile());
		PlayerPosition blackPlayerPos =  new PlayerPosition(ModelQuery.getCurrentGame().getBlackPlayer(), current.getBlackPosition().getTile());
		PlayerPosition redPlayerPos;
		PlayerPosition greenPlayerPos;
		Player nextPlayer = current.getPlayerToMove().getNextPlayer();
		
		GamePosition clone = new GamePosition(id, whitePlayerPos, blackPlayerPos, nextPlayer, ModelQuery.getCurrentGame());
		
		//just to make it easier to call them
		Player wP = ModelQuery.getWhitePlayer();
		Player bP = ModelQuery.getBlackPlayer();
		Player rP = ModelQuery.getRedPlayer();
		Player gP = ModelQuery.getGreenPlayer();
		
		Player whiteClone = new Player(wP.getRemainingTime(),wP.getUser(),1,Direction.Vertical);
		Player blackClone = new Player(bP.getRemainingTime(),bP.getUser(),1,Direction.Vertical);
		Player redClone;
		Player greenClone;
		
		
		int whiteWallBoard = 0;
		int blackWallBoard = 0;
		
		for(Wall whiteWall: ModelQuery.getWhiteWallsOnBoard()) {
			Wall wallClone = new Wall(id*40+wallCounter++, whiteClone);
			WallMove mov = whiteWall.getMove();
			WallMove moveClone = new WallMove(mov.getMoveNumber(),mov.getRoundNumber(),mov.getPlayer(),mov.getTargetTile(),mov.getGame(),mov.getWallDirection(),wallClone);
			wallClone.setOwner(whiteClone);
			clone.addWhiteWallsOnBoard(wallClone);
			
			whiteWallBoard++;
		}
		
		for(Wall blackWall: ModelQuery.getBlackWallsOnBoard()) {
			Wall wallClone = new Wall(id*40+wallCounter++, blackClone);
			WallMove mov = blackWall.getMove();
			WallMove moveClone = new WallMove(mov.getMoveNumber(),mov.getRoundNumber(),mov.getPlayer(),mov.getTargetTile(),mov.getGame(),mov.getWallDirection(),wallClone);
			wallClone.setOwner(blackClone);
			clone.addBlackWallsOnBoard(wallClone);
			
			blackWallBoard++;
		}
		
		while(whiteWallBoard < 10) {
			Wall wallClone = new Wall(id*40+wallCounter++, whiteClone);
			clone.addWhiteWallsInStock(wallClone);
			
			whiteWallBoard++;
		}
		
		while(blackWallBoard < 10) {
			Wall wallClone = new Wall(id*40+wallCounter++, blackClone);
			clone.addBlackWallsInStock(wallClone);
			
			blackWallBoard++;
		}
		/*for(Wall whiteWall: ModelQuery.getCurrentPosition().getWhiteWallsInStock()) {
			Wall wallClone = new Wall(id*40+wallCounter++, whiteClone);
			wallClone.setMove(whiteWall.getMove());
			wallClone.setOwner(whiteClone);
			clone.addWhiteWallsInStock(wallClone);
		}
		
		for(Wall blackWall: ModelQuery.getCurrentPosition().getBlackWallsInStock()) {
			Wall wallClone = new Wall(id*40+wallCounter++, blackClone);
			//wallClone.setMove(blackWall.getMove());
			wallClone.setOwner(ModelQuery.getBlackPlayer());
			clone.addBlackWallsInStock(wallClone);
		}*/
		
		if(ModelQuery.isFourPlayer()) {
			redPlayerPos = new PlayerPosition(ModelQuery.getCurrentGame().getRedPlayer(), current.getRedPosition().getTile());
			greenPlayerPos = new PlayerPosition(ModelQuery.getCurrentGame().getGreenPlayer(), current.getGreenPosition().getTile());
			redClone = new Player(rP.getRemainingTime(),rP.getUser(),1,Direction.Vertical);
			greenClone = new Player(gP.getRemainingTime(),gP.getUser(),1,Direction.Vertical);
			clone.setRedPosition(redPlayerPos);
			clone.setGreenPosition(greenPlayerPos);
			
			for(Wall redWall: ModelQuery.getRedWallsOnBoard()) {
				Wall wallClone = new Wall(id*40+wallCounter++, redClone);
				WallMove mov = redWall.getMove();
				WallMove moveClone = new WallMove(mov.getMoveNumber(),mov.getRoundNumber(),mov.getPlayer(),mov.getTargetTile(),mov.getGame(),mov.getWallDirection(),wallClone);
				wallClone.setOwner(redClone);
				clone.addRedWallsOnBoard(wallClone);
			}
			
			for(Wall greenWall: ModelQuery.getGreenWallsOnBoard()) {
				Wall wallClone = new Wall(id*40+wallCounter++, greenClone);
				WallMove mov = greenWall.getMove();
				WallMove moveClone = new WallMove(mov.getMoveNumber(),mov.getRoundNumber(),mov.getPlayer(),mov.getTargetTile(),mov.getGame(),mov.getWallDirection(),wallClone);
				wallClone.setOwner(greenClone);
				clone.addGreenWallsOnBoard(wallClone);
			}
			
			for(Wall redWall: ModelQuery.getCurrentPosition().getRedWallsInStock()) {
				Wall wallClone = new Wall(id*40+wallCounter++, redClone);
				wallClone.setOwner(redClone);
				clone.addRedWallsInStock(wallClone);
			}
			
			for(Wall greenWall: ModelQuery.getCurrentPosition().getGreenWallsOnBoard()) {
				Wall wallClone = new Wall(id*40+wallCounter++, greenClone);
				wallClone.setOwner(greenClone);
				clone.addGreenWallsInStock(wallClone);
			}
		}
		
		id++;
		return clone;
	}
}
