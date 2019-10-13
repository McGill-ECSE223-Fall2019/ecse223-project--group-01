package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.*;

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
        throw new java.lang.UnsupportedOperationException();
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
}
