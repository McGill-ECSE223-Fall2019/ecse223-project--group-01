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

    public static boolean isPositionValid = true;

    //static String saveLocation = ".\\src\\main\\resources\\";
    static String saveLocation = "./";

    /**
     * Empty constructor for PositionController
     */
    public PositionController(){}

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
                output = new PrintWriter(new FileOutputStream(new File(saveLocation + filename)));
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

            char columnLetter = (char) (column + 96);
            String whitePosition = Character.toString(columnLetter) + Integer.toString(row);
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

            columnLetter = (char) (column + 96);
            String blackPosition = Character.toString(columnLetter) + Integer.toString(row);
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

            char columnLetter = (char) (column + 96);
            String blackPosition = String.valueOf(columnLetter) + Integer.toString(row);
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

            columnLetter = (char) (column + 96);
            String whitePosition = String.valueOf(columnLetter) + Integer.toString(row);
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
    public static boolean loadGame(String filename, String whiteUser, String blackUser) throws java.lang.UnsupportedOperationException, IOException {
        File saveFile = new File("./" + filename);
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        //Make game running
        StartNewGameController.initializeGame();

        StartNewGameController.whitePlayerChoosesAUsername(whiteUser);
        StartNewGameController.blackPlayerChooseAUsername(blackUser);

        PlayerPosition whitePlayerPosition = null;
        PlayerPosition blackPlayerPosition = null;
        PlayerPosition redPlayerPosition = null;
        PlayerPosition greenPlayerPosition = null;
        List<GamePosition> positions = ModelQuery.getCurrentGame().getPositions();


        //Initialize GamePosition

        if(saveFile.exists()){
            try {
                FileReader fileReader = new FileReader(saveLocation + filename);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                Player playerTurn = null;
                int currentTurn = 0;
                List<int[]> whiteWalls = new ArrayList();
                List<int[]> blackWalls = new ArrayList();

                //Read the lines on the file and set them
                while((line = bufferedReader.readLine()) != null){

                    //.Set game stuff in order to load (place walls at locations and pawns at locations)
                    String[] categorySplit = line.split(": "); //[0] White or Black, [1] PositionInfo
                    String playerInfo = categorySplit[0]; // Either white or black
                    String[] positionInfo = categorySplit[1].split(","); //[0] PlayerPosition, [1:] WallPosition

                    if(playerInfo.contains("W")){ //White Player information
                        if(currentTurn == 0){
                            playerTurn = ModelQuery.getWhitePlayer();
                        }
                        //.Set Player Positions
                        int[] playerCoord = posToInt(positionInfo[0]);

                        //Validate position here
                        if(!validatePositionInRange(playerCoord[1],playerCoord[0])){
                            isPositionValid = false;
                            return false;
                        }

                        Tile pos = new Tile(playerCoord[1],playerCoord[0],loadGameBoard()); //using Position --> integer
                        whitePlayerPosition = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),pos);


                        //.Set Player Walls
                        for(int i = 1; i < positionInfo.length; i++){
                            int[] wallCoord = posToInt(positionInfo[i]);

                            whiteWalls.add(wallCoord);
                        }

                    }
                    else if(playerInfo.contains("B")){ //Black Player information
                        if(currentTurn == 0){
                            playerTurn = ModelQuery.getBlackPlayer();
                        }
                        //.Set Player Positions
                        int[] playerCoord = posToInt(positionInfo[0]);

                        //validate position here
                        if(!validatePositionInRange(playerCoord[1],playerCoord[0])){
                            isPositionValid = false;
                            return false;
                        }

                        Tile pos = new Tile(playerCoord[1],playerCoord[0],loadGameBoard()); //using Position --> integer
                        blackPlayerPosition = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),pos);


                        //Set Player Walls
                        for(int i = 1; i < positionInfo.length; i++){
                            int[] wallCoord = posToInt(positionInfo[i]);
                            blackWalls.add(wallCoord);

                        }
                    }
                    else { //Faulty savePosition file
                        return false;
                    }

                    currentTurn++;
                }

                GamePosition gameposition = new GamePosition(positions.size()+1, whitePlayerPosition, blackPlayerPosition, redPlayerPosition, greenPlayerPosition, quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame());
                quoridor.getCurrentGame().setCurrentPosition(gameposition);
                quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(whitePlayerPosition);
                quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(blackPlayerPosition);
                if(!ValidatePositionController.validateOverlappingPawns()){
                    return false;
                }

                if(Wall.getWithId(1)==null){
                    for(int i =1; i <= 10; i++){
                        ModelQuery.getCurrentGame().getWhitePlayer().addWall(i);
                    }
                    for(int j = 11; j <= 20; j++) {
                        ModelQuery.getCurrentGame().getBlackPlayer().addWall(j);
                    }
                }


                //AddWalls for players
                for(int j = 1; j <= 10; j++){
                    Wall wall = Wall.getWithId(j);
                    gameposition.addWhiteWallsInStock(wall);
                }
                for(int j = 1; j <= 10; j++){
                    Wall wall = Wall.getWithId(j + 10);
                    gameposition.addBlackWallsInStock(wall);
                }

                //set player wall
                for(int i = 0; i < whiteWalls.size(); i++){
                    int moveNum = 1, roundNum = 1;

                    Direction wallDir = convertToDir(whiteWalls.get(i)[2]);
                    if(!ValidatePositionController.validateWallPosition(whiteWalls.get(i)[1], whiteWalls.get(i)[0], wallDir)){
                        isPositionValid = false;
                        return false;
                    }

                    Player whitePlayer = ModelQuery.getWhitePlayer();
                    Tile wallTile = new Tile(whiteWalls.get(i)[1], whiteWalls.get(i)[0], ModelQuery.getBoard());
                    Game currentGame = ModelQuery.getCurrentGame();

                    Wall dropWall = ModelQuery.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().get(i);

                    WallMove wallmove = new WallMove(moveNum, roundNum,whitePlayer, wallTile, currentGame, wallDir, dropWall);

                   loadWall(wallmove,ModelQuery.getWhitePlayer());
                }

                for(int i = 0; i < blackWalls.size(); i++){
                    int moveNum = 1;
                    int roundNum = 1;

                    Direction wallDir = convertToDir(blackWalls.get(i)[2]);

                    if(!ValidatePositionController.validateWallPosition(blackWalls.get(i)[1], blackWalls.get(i)[0], wallDir)){
                        isPositionValid = false;
                        return false;
                    }

                    Player blackPlayer = ModelQuery.getBlackPlayer();
                    Tile wallTile = new Tile(blackWalls.get(i)[1], blackWalls.get(i)[0], ModelQuery.getBoard());
                    Game currentGame = ModelQuery.getCurrentGame();

                    Wall dropWall = ModelQuery.getCurrentGame().getCurrentPosition().getBlackWallsInStock().get(i);

                    WallMove wallmove = new WallMove(moveNum, roundNum,blackPlayer, wallTile, currentGame, wallDir, dropWall);

                    loadWall(wallmove,ModelQuery.getBlackPlayer());
                }


                if(playerTurn == null){ //incase while loop was not executed
                    return false;
                }

                else{ //switch the current turn to the player
                    if(ModelQuery.getPlayerToMove() != playerTurn)
                    SwitchPlayerController.switchActivePlayer();
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

    /**
     * This helper method is used to convert Direction into
     * a String that is appropriate for its save format
     * @param direction     the direction of the wall
     * @return              the save format of the direction
     */
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

    /**
     * This helper method is used to convert an integer into a Direction
     * @param direction     the direction in integer form
     * @return              the Direction of the wall
     */
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
    private static int[] posToInt(String playerPosition){
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
        positionInt[1] = Character.getNumericValue(char_arr[1]);

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

    /**
     * This helper method is used to convert wall information into a string that
     * will be used in saving position
     * @param index         the index of the list of walls
     * @param listOfWalls   the list of walls
     * @return              String that will have the appropriate save format
     */
    private static String writeWallInfo(int index, List<Wall> listOfWalls){
        Wall wall = listOfWalls.get(index);
        Tile wallTile = wall.getMove().getTargetTile(); //gets the coordinate of the wall
        String orientation = convertWallDir(wall.getMove().getWallDirection()); //gets the orientation of the wall
        int wallRow = wallTile.getRow();
        int wallCol = wallTile.getColumn();

        //Wall save: ColumnRowDirection
        char columnLetter = (char) (wallCol + 96);
        String wallPosition = "," + String.valueOf(columnLetter) + Integer.toString(wallRow) + orientation;
        return wallPosition;
    }

    private static boolean loadWall(WallMove move, Player player){
        ModelQuery.getCurrentGame().addMove(move);
        ModelQuery.getCurrentGame().setWallMoveCandidate(null);

        if(player.equals(ModelQuery.getWhitePlayer())) {
            ModelQuery.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(move.getWallPlaced());
            ModelQuery.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(move.getWallPlaced());
        }else{
            ModelQuery.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(move.getWallPlaced());
            ModelQuery.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(move.getWallPlaced());
        }

        return true;
    }

    /**
     * Simple helper method that is used to check if the Pawn position is on the board
     * @param row       the row of the pawn position
     * @param col       the column of the pawn position
     * @return          true if it is within the board range
     *                  false if it is outside of the board
     */
    private static boolean validatePositionInRange(int row, int col){
        if (row < 1 || row > 9 || col < 1 || col > 9) {
            return false;
        }
        return true;
    }

    private static Board loadGameBoard() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Board board;
        if (quoridor.getBoard() == null) {
            board = new Board(quoridor);
            for (int i = 1; i <= 9; i++) { // rows
                for (int j = 1; j <= 9; j++) { // columns
                    board.addTile(i, j);
                }
            }
        } else {
            board = quoridor.getBoard();
        }
        return board;
    }
}
