package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for holding controller methods that will be used
 * for the save position feature and the load position feature for Quoridor gameplay.
 * @author Kevin
 */
public class PositionController {

    static String saveLocation = ".\\src\\main\\resources\\";

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
    public static boolean loadGame(String filename) throws java.lang.UnsupportedOperationException, IOException {
        File saveFile = new File(saveLocation + filename);
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        if(saveFile.exists()){
            try {
                FileReader fileReader = new FileReader(saveLocation + filename);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                String playerTurn = null;
                int currentTurn = 0;
                //Read the lines on the file and set them
                while((line = bufferedReader.readLine()) != null){

                    //.Set game stuff in order to load (place walls at locations and pawns at locations)
                    String[] categorySplit = line.split(":"); //[0] White or Black, [1] PositionInfo
                    String playerInfo = categorySplit[0]; // Either white or black
                    String[] positionInfo = categorySplit[1].split(" "); //[0] PlayerPosition, [1:] WallPosition

                    if(playerInfo.contains("W:")){ //White Player information
                        if(currentTurn == 0){
                            playerTurn = "White";
                        }
                        //.Set Player Positions
                        int[] playerCoord = posToInt(positionInfo[0]);
                        Tile pos = new Tile(playerCoord[1],playerCoord[0],quoridor.getBoard()); //using Position --> integer
                        PlayerPosition playerPosition = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),pos);
                        quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(playerPosition);

                        //.Set Player Walls
                        for(int i = 1; i < (categorySplit.length - 1); i++){
                            //TODO: drop all the walls on the board
                        }

                    }
                    else if(playerInfo.contains("B:")){ //Black Player information
                        if(currentTurn == 0){
                            playerTurn = "Black";
                        }
                        //.Set Player Positions
                        int[] playerCoord = posToInt(positionInfo[0]);
                        Tile pos = new Tile(playerCoord[1],playerCoord[0],quoridor.getBoard()); //using Position --> integer
                        PlayerPosition playerPosition = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),pos);
                        quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(playerPosition);

                        //Set Player Walls
                        for(int i = 1; i < (categorySplit.length - 1); i++){
                            //TODO: drop all the walls on the board
                        }
                    }
                    else { //Faulty savePosition file
                        return false;
                    }

                    currentTurn++;
                }

                if(playerTurn.isEmpty()){ //incase while loop was not executed
                    return false;
                }

                else{ //switch the current turn to the player
                    SwitchPlayerController.SwitchActivePlayer(playerTurn);
                }

                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false; //save file was unsuccessfully loaded
            }
            return true; //Save file was successfully loaded
        }
        else{
            return false; //save file was unsuccessfully loaded
        }
    }
    //return a list of moves?


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


    /**
     * Converts position information obtained from the file into integers
     * @param playerPosition    the position obtained from the file
     * @return int[]            [0] COLUMN
     *                          [1] ROW
     */
    public static int[] posToInt(String playerPosition){
        //.Note [0]: always a letter, [1]: always a number
        char[] char_arr = playerPosition.toCharArray();

        //.Note [0]: Column, [1]: Row
        int[] positionInt = new int[char_arr.length];

        //.Converting Column letter into integer
        int temp = (int)char_arr[0];
        int temp_int = 96; //will be used as difference
        int ASCII_a = 97, ASCII_i = 105; //ASCII index for 'a' and 'i'
        if(temp <= ASCII_i & temp >= ASCII_a)
            positionInt[0] = (temp-temp_int); //will return the alphabetical index of the letters

        //.Converting Row string into integer
        positionInt[1] = (int)char_arr[1];

        return positionInt;
    }
}

//TODO: Validating positions
//TODO: Dropping walls and decrementing wallStock