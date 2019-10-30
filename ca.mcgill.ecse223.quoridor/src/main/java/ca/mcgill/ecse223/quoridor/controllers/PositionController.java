package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import java.io.*;
import java.util.List;

/**
 * This class is responsible for holding controller methods that will be used
 * for the save position feature and the load position feature for Quoridor gameplay.
 * @author Kevin
 */
public class PositionController {
    /**
     * Empty constructor for PositionController, will be updated/completed in the future
     */
    public PositionController(){}

    static String saveLocation = ".\\src\\main\\resources\\";

    /**
     * Attempts to create or overwrite a savefile,
     * that will contain the positions of the current game, into a filesystem.
     * @param filename      the name of the savefile
     * @param currentPlayer the current player when initializing the save feature
     * @param position      the position of Pawns/Walls
     * @return true         the game saved correctly
     *         false        the game saved incorrectly.
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean saveGame(String filename, Player currentPlayer, GamePosition position) throws java.lang.UnsupportedOperationException {
        File file = new File(saveLocation + filename);
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        PrintWriter output;
        if (file.exists() && !file.isDirectory()){ //If the save file exists and is not a directory
            try {
                output = new PrintWriter(new FileOutputStream(new File(saveLocation + filename), true));
            } catch (FileNotFoundException e) { //Error check for writer
                e.printStackTrace();
                return false;
            }
        }
        else {
            try {
                output = new PrintWriter(saveLocation + filename);
            } catch (FileNotFoundException e) { //Error check for writer
                e.printStackTrace();
                return false;
            }
        }

        if(currentPlayer.equals(ModelQuery.getWhitePlayer())){
            int column = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
            int row = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();

            String whitePosition = String.valueOf(column + 96) + Integer.toString(row);
            String playerInfo = String.format("W: %s", whitePosition);
            output.append(playerInfo);

            //Walls
            List<Wall> listOfWalls = ModelQuery.getWhiteWallsOnBoard();
            for(int i = 0; i < listOfWalls.size(); i++){
                String wallPosition = writeWallInfo(i, listOfWalls);
                output.append(wallPosition);
            }
            output.append("\n");

            column = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
            row = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();

            String blackPosition = String.valueOf(column + 96) + Integer.toString(row);
            playerInfo = String.format("B: %s", blackPosition);
            output.append(playerInfo);

            listOfWalls = ModelQuery.getBlackWallsOnBoard();
            for(int i = 0; i < listOfWalls.size(); i++){
               String wallPosition = writeWallInfo(i, listOfWalls);
                output.append(wallPosition);
            }
        }

        else if(currentPlayer.equals(ModelQuery.getBlackPlayer())){
            int column = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
            int row = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();

            String blackPosition = String.valueOf(column + 96) + Integer.toString(row);
            String playerInfo = String.format("B: %s,", blackPosition);
            output.append(playerInfo);

            List<Wall> listOfWalls = ModelQuery.getBlackWallsOnBoard();
            for(int i = 0; i < listOfWalls.size(); i++){
                String wallPosition = writeWallInfo(i, listOfWalls);
                output.append(wallPosition);
            }
            output.append("\n");

            column = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
            row = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();

            String whitePosition = String.valueOf(column + 96) + Integer.toString(row);
            playerInfo = String.format("B: %s", whitePosition);
            output.append(playerInfo);

            listOfWalls = ModelQuery.getWhiteWallsOnBoard();
            for(int i = 0; i < listOfWalls.size(); i++){
                String wallPosition = writeWallInfo(i, listOfWalls);
                output.append(wallPosition);
            }
        }

        else{ //Something went wrong
            return false;
        }


        output.append("\n");
        output.close();
        return true;
       }

    /**
     * Attempts to load a specified savefile in a filesystem.
     * @param filename  the name of the savefile
     * @return true     the game loads correctly
     *         false    the game loads inccorectly
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean loadGame(String filename) throws java.lang.UnsupportedOperationException{
        throw new java.lang.UnsupportedOperationException();
    }

    //.helper methods
    private static String convertWallDir(Direction direction){
        switch(direction){
            case Horizontal:
                return "h";
            case Vertical:
                return "v";
            default:
                return null;
        }
    }

    private static String writeWallInfo(int index, List<Wall> listOfWalls){
        Wall wall = listOfWalls.get(index);
        Tile wallTile = wall.getMove().getTargetTile(); //gets the coordinate of the wall
        String orientation = convertWallDir(wall.getMove().getWallDirection()); //gets the orientation of the wall
        int wallRow = wallTile.getRow();
        int wallCol = wallTile.getColumn();

        //Wall save: ColumnRowDirection
        String wallPosition = ", " + String.valueOf(wallCol + 96) + Integer.toString(wallRow) + orientation;
        return wallPosition;
    }

}
