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
     * @param filename  the name of the savefile
     * @param position  the position of Pawns/Walls
     * @return true     the game saved correctly
     *         false    the game saved incorrectly.
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean saveGame(String filename, GamePosition position) throws java.lang.UnsupportedOperationException {
        File file = new File(saveLocation + filename);
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

        //.Get the list of moves
        List<Move> listOfMoves = QuoridorApplication.getQuoridor().getCurrentGame().getMoves();

        //.*Not going into for loop, because there is nothing in listOfMoves
        for(int i = 0; i < listOfMoves.size(); i++){
            Move move = listOfMoves.get(i);
            int moveNumber = move.getRoundNumber();
            move.getNextMove();
            //.****Not finished yet
            output.append("\n");

        }
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
}


//Able to create/append to a file

//PlayerPosition, GamePosition, WallPosition?
// format: (round#). (whitePosition/whiteWall) (blackPosition/blackWall)

//Q: Do each move require a .getPosition?
//Q: how to loop through each move and store them using formatting

//Either append after each turn to the associated round#
//Append using formatting after each round
//  Must determine if player put wall or player moved
