/**
 * @author = Mark Zhu
*/

package ca.mcgill.ecse223.quoridor.controller;

import java.util.List;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Tile;

public class ValidatePositionController {
	
	/**
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean validatePawnPosition(int row, int col){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @param row
	 * @param col
	 * @param dir
	 * @return
	 */
	public static boolean validateWallPosition(int row, int col, Direction dir) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @param tiles
	 * @return
	 */
	public static boolean validateOverlappingWalls(List<Tile> tiles) {
		throw new UnsupportedOperationException();
	}
	
	
}