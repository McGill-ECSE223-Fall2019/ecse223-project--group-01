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
            String playerInfo = String.format("W: %s,", whitePosition);

            //Walls
            List<Wall> listOfWalls = ModelQuery.getWhitePlayer().getWalls();
            for(int i = 0; i < listOfWalls.size(); i++){
                Wall wall = listOfWalls.get(i);

            }

            //After writing, do BlackPlayer
        }

        else if(currentPlayer.equals(ModelQuery.getBlackPlayer())){
            int column = ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
            int row = ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();

            String blackPosition = String.valueOf(column + 96) + Integer.toString(row);
            String playerInfo = String.format("B: %s,", blackPosition);

            //Walls

            //After writing, do WhitePlayer
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

    /**
     * Validates the positions loaded.
     * @param position  the positions that will be loaded
     * @return true     the positions are valid
     *         false    the positions are invalid
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean validatePosition(GamePosition position) throws java.lang.UnsupportedOperationException{
        throw new java.lang.UnsupportedOperationException();
    }

    public static String convertWallDir(int direction){
        switch(direction){
            case 0:
                return "h";
            case 1:
                return "v";
            default:
                return null;
        }
    }
}
