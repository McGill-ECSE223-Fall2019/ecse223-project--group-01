package ca.mcgill.ecse223.quoridor.controllers;
import ca.mcgill.ecse223.quoridor.model.*;

import java.util.List;

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

        if(row<1 || row > 8 || col < 1 || col > 8){
            return false;
        }
        List tiles = ModelQuery.getTiles();
        Tile target = ModelQuery.getTile(row,col);
        move.setTargetTile(target);
        return true;
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
        int row = move.getTargetTile().getRow();
        int col = move.getTargetTile().getColumn();
        switch(side){
            case "left":{
                col-=1;
                break;
            }
            case "right":{
                col+=1;
                break;
            }
            case "up": {
                row-=1;
                break;
            }
            case "down":{
                row+=1;
                break;
            }
        }
        return WallController.moveWall(move,row,col);
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

    /**
     * @author Kate Ward
     * Attempts to rotate wall in hand
     *
     * @return outcome of operation
     * @throws UnsupportedOperationException
     */
    public static boolean rotateWall() {
        throw new UnsupportedOperationException();

    }

    /**
     * @author Kate Ward
     * Attempts to grab wall from stock and hold it in hand above board
     * returns true if successful
     *
     * @return outcome of operation
     * @throws UnsupportedOperationException
     */
    public static boolean grabWall() {
        throw new UnsupportedOperationException();

    }
}
