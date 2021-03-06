/**
 * @author = Mark Zhu
*/

package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;

import java.util.List;

public class ValidatePositionController {
	
	/**
	 * This method checks if the pawn is on a valid tile
	 * @param row of the pawn's tile
	 * @param col of the pawn's tile
	 * @return whether or not the pawn location is allowed
	 */
	public static boolean validatePawnPosition(int row, int col){
		if (row < 1 || row > 9 || col < 1 || col > 9) {
			return false;
		}
		return validateOverlappingPawns();
	}
	
	/**
	 * This method checks if the pawns are overlapping by checking if they're on the same tile
	 * @return whether or not the pawn locations are allowed (true if valid, false if invalid)
	 */
	public static boolean validateOverlappingPawns() {
		boolean valid = true;
		
		PlayerPosition whitePosition = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition();
		PlayerPosition blackPosition = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition();
		if(whitePosition.getTile().equals(blackPosition.getTile())) {
			valid = false;
		}
		
		if(ModelQuery.isFourPlayer()) {
			PlayerPosition redPosition = ModelQuery.getCurrentGame().getCurrentPosition().getRedPosition();
			PlayerPosition greenPosition = ModelQuery.getCurrentGame().getCurrentPosition().getGreenPosition();
			if(whitePosition.getTile().equals(redPosition.getTile()) || whitePosition.getTile().equals(greenPosition.getTile()) ||
					blackPosition.getTile().equals(redPosition.getTile()) || blackPosition.getTile().equals(greenPosition.getTile()) ||
					redPosition.getTile().equals(greenPosition.getTile())) {
				valid = false;
			}
		}
		
		return valid;
	}
	
	/**
	 * This method checks if the wall location is valid
	 * @param row of the reference tile
	 * @param col of the reference tile
	 * @param dir of the wall
	 * @return whether or not the wall location is allowed (true if valid, false if invalid)
	 */
	public static boolean validateWallPosition(int row, int col, Direction dir) {
		
		if (row < 1 || row > 8 || col < 1 || col > 8) {
			return false;
		}
		
		return !validateOverlappingWalls(row, col, dir, ModelQuery.getAllWallsOnBoard());
	}
	
	/**
	 * This (mostly helper) method takes in the values of a single wall and compares it against a list of existing walls to see if there is overlap
	 * @return whether or not the wall overlaps with another (false if no overlap -> true means invalid)
	 */
	public static boolean validateOverlappingWalls(int row, int col, Direction dir, List<Wall> walls) throws UnsupportedOperationException{
		for(Wall previousWall: walls) {
			Tile previousTile = previousWall.getMove().getTargetTile();
			if (previousTile.getRow() == row && previousTile.getColumn() == col){
				return true;
			}
			else if (previousWall.getMove().getWallDirection() == Direction.Vertical && dir == Direction.Vertical &&
					(previousTile.getColumn() == col && Math.abs(previousTile.getRow() - row) <= 1)) {
				return true;
			}
			else if (previousWall.getMove().getWallDirection() == Direction.Horizontal && dir == Direction.Horizontal &&
					(previousTile.getRow() == row && Math.abs(previousTile.getColumn() - col) <= 1)){
				return true;
			}
		}
		return false;
	}
	
	
}