package ca.mcgill.ecse223.quoridor.controllers;
import ca.mcgill.ecse223.quoridor.WallGraph;
import ca.mcgill.ecse223.quoridor.model.*;
import java.util.*;

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
    public static Boolean dropWall(WallMove move, Player player) throws UnsupportedOperationException{
        List<Wall> placedWalls = ModelQuery.getAllWallsOnBoard();

        // validate no overlap
        for (Wall wall: placedWalls){
            if(compareWalls(move,wall.getMove())){
                return false;
            }
        }

        ModelQuery.getCurrentGame().addMove(move);
        ModelQuery.getCurrentGame().setWallMoveCandidate(null);

        if(player.equals(ModelQuery.getWhitePlayer())) {
            ModelQuery.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(move.getWallPlaced());
            ModelQuery.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(move.getWallPlaced());
            SwitchPlayerController.SwitchActivePlayer("white");
        }else{
            ModelQuery.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(move.getWallPlaced());
            ModelQuery.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(move.getWallPlaced());
            SwitchPlayerController.SwitchActivePlayer("black");
        }

        return true;
    }

    // Return true if the wall do not overlap
    private static Boolean compareWalls(WallMove wall1, WallMove wall2){
        if( wall1.getWallDirection() == Direction.Vertical && wall2.getWallDirection() == Direction.Vertical){
            return Math.abs(wall1.getTargetTile().getRow() - wall2.getTargetTile().getRow()) > 1;
        }
        else if(wall1.getWallDirection() == Direction.Horizontal && wall2.getWallDirection() == Direction.Horizontal){
            return Math.abs(wall1.getTargetTile().getColumn() - wall2.getTargetTile().getColumn()) > 1;
        }
        else if(wall1.getWallDirection() == Direction.Horizontal && wall2.getWallDirection() == Direction.Vertical){
            return wall2.getTargetTile().getColumn() == wall1.getTargetTile().getColumn()-1 && wall2.getTargetTile().getRow() == wall1.getTargetTile().getRow()-1;
        }
        else{
            return wall1.getTargetTile().getColumn() == wall2.getTargetTile().getColumn()+1 && wall1.getTargetTile().getRow() == wall2.getTargetTile().getRow()-1;
        }
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

    private static void initGraph(){
        List<Wall> placedWalls = ModelQuery.getAllWallsOnBoard();

        WallGraph graph = new WallGraph();
        // create graph
        for(int i =0 ; i<81; i++){
            List<Integer> neighbors = getNeighbhors(i);
            for( int neighbor: neighbors) {
                graph.addEdge(i, neighbor);
            }
        }
        // add cuts
        for (Wall wall: placedWalls){
            int[][] cuts = getWallCuts(wall);
            graph.cutEdge(cuts[0][0],cuts[0][1]);
            graph.cutEdge(cuts[1][0],cuts[1][1]);
        }
    }

    private static List<Integer> getNeighbhors(int tile_index){
        int row = Math.floorDiv(tile_index, 9);
        int col = tile_index % 9;

        ArrayList<Integer> neighbhors = new ArrayList();
        if(row>0){
            neighbhors.add(9*(row-1)+col);
        }
        if(row<8){
            neighbhors.add(9*(row+1)+col);
        }
        if(col>0){
            neighbhors.add(9*row + col-1);
        }
        if(col<8){
            neighbhors.add(9*row + col+1);
        }
        return neighbhors;
    }

    private  static int[][] getWallCuts(Wall wall){
        int[][] cuts= new int[2][2];
        WallMove move = wall.getMove();
        int row = move.getTargetTile().getRow() - 1;
        int col = move.getTargetTile().getColumn() - 1;
        Direction dir = wall.getMove().getWallDirection();

        if(dir == Direction.Vertical){
            cuts[0][0] = 9*(row+1)+col;
            cuts[0][1] = 9*(row+1)+col+1;

            cuts[1][0] = 9*(row+2)+col;
            cuts[1][1] = 9*(row+2)+col+1;
        }
        else{
            cuts[0][0] = 9*(row)+col+1;
            cuts[0][1] = 9*(row+1)+col+1;

            cuts[1][0] = 9*(row)+col+2;
            cuts[1][1] = 9*(row+1)+col+2;
        }

        return cuts;
    }
}
