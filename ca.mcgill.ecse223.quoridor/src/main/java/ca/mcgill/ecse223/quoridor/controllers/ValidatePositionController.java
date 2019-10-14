/**
 * @author = Mark Zhu
*/

package ca.mcgill.ecse223.quoridor.controllers;

import java.util.List;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Tile;

public class ValidatePositionController {
	
	/**
	 * This method checks if the pawn is on a valid tile
	 * @param row of the pawn's tile
	 * @param col of the pawn's tile
	 * @return whether or not the pawn location is allowed
	 */
	public static boolean validatePawnPosition(int row, int col){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This method checks if the wall location is valid
	 * @param row of the reference tile
	 * @param col of the reference tile
	 * @param dir of the wall
	 * @return whether or not the wall location is allowed
	 */
	public static boolean validateWallPosition(int row, int col, Direction dir) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This method iterates over the list of reference tiles to see if the wall location does not overlap
	 * @param tiles is the reference tiles
	 * @return whether or not the wall overlaps with another
	 */
	public static boolean validateOverlappingWalls(List<Tile> tiles) {
		throw new UnsupportedOperationException();
	}
	
	
}