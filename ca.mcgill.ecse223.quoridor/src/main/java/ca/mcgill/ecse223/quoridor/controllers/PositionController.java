package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

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

    static String saveLocation = ".\\src\\main\\resources\\";

    /**
     * Attempts to create or overwrite a savefile,
     * that will contain the positions of the current game, into a filesystem.
     * @param filename      the name of the savefile
     * @param currentPlayer the current player when initializing the save feature
     * @return true         the game saved correctly
     *         false        the game saved incorrectly.
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean saveGame(String filename, Player currentPlayer) throws java.lang.UnsupportedOperationException {
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
    public static boolean loadGame(String filename) throws java.lang.UnsupportedOperationException, IOException {
        File saveFile = new File(saveLocation + filename);
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        //Make game running
        StartNewGameController.initializeGame();

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

                        //Validate position here
                        ValidatePositionController.validatePawnPosition(playerCoord[1],playerCoord[0]);

                        Tile pos = new Tile(playerCoord[1],playerCoord[0],quoridor.getBoard()); //using Position --> integer
                        PlayerPosition playerPosition = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),pos);
                        quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(playerPosition);

                        //.Set Player Walls
                        for(int i = 1; i < (categorySplit.length - 1); i++){
                            int[] wallCoord = posToInt(categorySplit[i]);

                            int moveNum = 1;
                            int roundNum = 1;
                            Player whitePlayer = ModelQuery.getWhitePlayer();
                            Tile wallTile = new Tile(wallCoord[1], wallCoord[0], ModelQuery.getBoard());
                            Game currentGame = ModelQuery.getCurrentGame();
                            Direction wallDir = convertToDir(wallCoord[2]);
                            Wall dropWall = ModelQuery.getWhitePlayer().getWall(i);

                            WallMove wallmove = new WallMove(moveNum, roundNum,whitePlayer, wallTile, currentGame, wallDir, dropWall);
                            WallController.dropWall(wallmove,ModelQuery.getWhitePlayer());
                        }

                    }
                    else if(playerInfo.contains("B:")){ //Black Player information
                        if(currentTurn == 0){
                            playerTurn = "Black";
                        }
                        //.Set Player Positions
                        int[] playerCoord = posToInt(positionInfo[0]);

                        //validate position here
                        ValidatePositionController.validatePawnPosition(playerCoord[1],playerCoord[0]);

                        Tile pos = new Tile(playerCoord[1],playerCoord[0],quoridor.getBoard()); //using Position --> integer
                        PlayerPosition playerPosition = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),pos);
                        quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(playerPosition);

                        //Set Player Walls
                        for(int i = 1; i < (categorySplit.length - 1); i++){
                            //TODO: drop all the walls on the board
                            int[] wallCoord = posToInt(categorySplit[i]);

                            int moveNum = 1;
                            int roundNum = 1;
                            Player blackPlayer = ModelQuery.getBlackPlayer();
                            Tile wallTile = new Tile(wallCoord[1], wallCoord[0], ModelQuery.getBoard());
                            Game currentGame = ModelQuery.getCurrentGame();
                            Direction wallDir = convertToDir(wallCoord[2]);
                            Wall dropWall = ModelQuery.getBlackPlayer().getWall(i);

                            WallMove wallmove = new WallMove(moveNum, roundNum,blackPlayer, wallTile, currentGame, wallDir, dropWall);
                            WallController.dropWall(wallmove,ModelQuery.getBlackPlayer());
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

    public static Direction convertToDir(int direction){
        switch(direction){
            case 0:
                return Direction.Horizontal;
            case 1:
                return Direction.Vertical;
            default:
                return null;
        }
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

        //.Note [0]: Column, [1]: Row, [2]: Wall direction
        int[] positionInt = new int[char_arr.length];

        //.Converting Column letter into integer
        int temp = (int)char_arr[0];
        int temp_int = 96; //will be used as difference
        int ASCII_a = 97, ASCII_i = 105; //ASCII index for 'a' and 'i'
        if(temp <= ASCII_i & temp >= ASCII_a)
            positionInt[0] = (temp-temp_int); //will return the alphabetical index of the letters

        //.Converting Row string into integer
        positionInt[1] = (int)char_arr[1];

        //.Converting string into Direction
        if(char_arr.length > 2){
            if(char_arr[2] == 'h'){ //if wall is vertical
                positionInt[2] = 0;
            }
            else{ //if wall is horizontal
                positionInt[2] = 1;
            }
        }

        return positionInt;
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
