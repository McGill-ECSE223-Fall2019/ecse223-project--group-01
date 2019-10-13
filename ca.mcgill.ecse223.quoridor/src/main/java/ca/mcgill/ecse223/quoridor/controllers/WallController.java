package ca.mcgill.ecse223.quoridor.controllers;
import ca.mcgill.ecse223.quoridor.model.*;
public class WallController {

    /**
     * @author Tritin Truong
     * Set the tile of WallMove to a specific row and column
     * returns true if operation is successful
     *
     * @param move The subject of the move
     * @param row The target row of the move
     * @param col The target column of the move
     * @return Outcome of operation either
     * @throws UnsupportedOperationException
     */
    public static Boolean moveWall(WallMove move, int row, int col) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }

    /**
     * @author Tritin Truong
     * Shifts the position of a WallMove 1 tile left,right,up or down
     * returns true if the operation is successful
     *
     * @param side Direction of the shift (left,right,down,up)
     * @param move The subject of the shift
     * @return outcome of operation
     * @throws UnsupportedOperationException
     */
    public static Boolean shiftWall(String side, WallMove move) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @author Tritin Truong
     *
     * Attempts to drop the wall at the current position
     * Also completes the moves
     *
     * @param move The subject of the drop
     * @return outcome of operation
     * @throws UnsupportedOperationException
     */
    public static Boolean dropWall(WallMove move) throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }
}
