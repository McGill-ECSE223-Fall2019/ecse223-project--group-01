/**
 * @author = Mark Zhu
*/

package ca.mcgill.ecse223.quoridor.controllers;

import java.util.List;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Tile;

public class ValidatePositionController {
	
	/**
	 * This method checks if the pawn position is allowed (e.g. off the map)
	 * @param row of the pawn
	 * @param col of the pawn
	 * @return whether or not the pawn position is allowed
	 */
	public static boolean validatePawnPosition(int row, int col){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This method checks if the wall is allowed to be where it is
	 * @param row of the reference tile to which the wall is defined
	 * @param col of the reference tile to which the wall is defined
	 * @param dir of the wall
	 * @return whether or not the wall position is valid
	 */
	public static boolean validateWallPosition(int row, int col, Direction dir) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This method iterates over the list of walls to ensure no overlapping
	 * (technically iterates over the list of wall reference tiles)
	 * @param tiles List of tiles
	 * @return whether or not there is overlap
	 */
	public static boolean validateOverlappingWalls(List<Tile> tiles) {
		throw new UnsupportedOperationException();
	}
	
	
}