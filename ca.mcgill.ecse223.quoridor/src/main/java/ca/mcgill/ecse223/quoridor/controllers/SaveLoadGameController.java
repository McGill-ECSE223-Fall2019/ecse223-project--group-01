package ca.mcgill.ecse223.quoridor.controllers;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse223.quoridor.controllers.PositionController.*;

/**
 * This class is responsible for holding controller methods that will be used
 * for the save game feature and the load game feature for Quoridor gameplay.
 *
 * This class will also be borrowing the helper methods that were defined
 * in the PositionController class
 * @author Kevin
 */
@SuppressWarnings("ALL")
public class SaveLoadGameController {

    public static boolean isSaveMoveValid = true;

    //This variable is the save location of the .mov files
    static String saveLocation = "./";

    /**
     * Attempts to create or overwrite a saved game.
     * The save game will contain the move number, player 1's move and player 2's move
     *
     * @param filename      the name of the savefile
     * @return true         the game saved correctly
     *         false        the game saved incorrectly.
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean fileSave(String filename){
        File file = new File(saveLocation + filename);
        PrintWriter output;

        //Check to see if the file exists and is not a directory
        if(file.exists() && !file.isDirectory()){
            try {
                output = new PrintWriter(new FileOutputStream(new File(saveLocation + filename)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        //If the file does not exist
        else {
            try {
                output = new PrintWriter(saveLocation + filename);
            } catch (FileNotFoundException e) { //Error check for writer
                e.printStackTrace();
                return false;
            }
        }

        //.Get the list of move history
        List<Move> listOfMoves = QuoridorApplication.getQuoridor().getCurrentGame().getMoves();

        /* NOT SURE IF THIS PART OF THE CODE WORKS*/
        //.*Not going into for loop, because there is nothing in listOfMoves
        for(int i = 0; i < listOfMoves.size(); i++){
            Move move = listOfMoves.get(i);
            int moveNumber = move.getRoundNumber();
            move.getNextMove();
            //.****Not finished yet
            output.append("\n");

        }
        output.close();
        //.Write the move number

        return true;
    }

    /**
     * Attempts to load a specified saved game.
     * @param filename  the name of the savefile
     * @return true     the game loads correctly
     *         false    the game loads inccorectly
     * @throws java.lang.UnsupportedOperationException
     */
    public static boolean fileLoad(String filename, String whiteUser, String blackUser){
        File saveFile = new File(saveLocation + filename);
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        boolean isMoveValid = false;

        //Making the game run
        PrepareGame(whiteUser, blackUser);
        PlayerPosition whitePlayerPosition = null;
        PlayerPosition blackPlayerPosition = null;
        List<GamePosition> positions = ModelQuery.getCurrentGame().getPositions();
        int movesTotal = 0;
        //Reading the savefile
        if(saveFile.exists()){
            try {
                FileReader fileReader = new FileReader(saveLocation + filename);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                //This is where all the information will be stored: [0] Move number [1:] Move Coordinates
                List<int[]> whiteWalls = new ArrayList();
                List<int[]> blackWalls = new ArrayList();
                List<int[]> whiteMoves = new ArrayList();
                List<int[]> blackMoves = new ArrayList();

                //Read the lines on the file and set them
                while((line = bufferedReader.readLine()) != null) {
                    /* Reading the data from the save file and turning them into something that can be understood by our game*/

                    //Split the information gained from the file
                    String[] categorySplit = line.split(". "); //[0] Move Number, [1] White Move + Black Move
                    String moveNumber = categorySplit[0];
                    String[] moveInfo = categorySplit[1].split(" "); //[0] White Move, [1] Black Move
                    //Summary: Information extracted == Move number, White Move, Black Move

                    //White Player Move Information
                    int[] whiteMoveCoord = posToInt(moveInfo[0]);

                    //Validate position here
                    if (!validatePositionInRange(whiteMoveCoord[1], whiteMoveCoord[0])) {
                        isSaveMoveValid = false;
                        return false;
                    }
                    //It is a pawn move
                    if (whiteMoveCoord.length == 2) {
                        int[] info = {Integer.parseInt(moveNumber), whiteMoveCoord[0], whiteMoveCoord[1]};
                        whiteMoves.add(info);
                    }
                    //It is a wall move
                    else if (whiteMoveCoord.length == 3) {
                        int[] info = {Integer.parseInt(moveNumber), whiteMoveCoord[0], whiteMoveCoord[1], whiteMoveCoord[2]};
                        whiteWalls.add(info);
                    } else {
                        //something is wrong
                        isSaveMoveValid = false;
                        return false;
                    }

                    //Black Player Move Info
                    int[] blackMoveCoord = posToInt(moveInfo[1]); //Turning it into game understandable data

                    //Validate position here
                    if (!validatePositionInRange(blackMoveCoord[1], blackMoveCoord[0])) {
                        isSaveMoveValid = false;
                        return false;
                    }

                    //It is a pawn move
                    if (blackMoveCoord.length == 2) {
                        int[] info = {Integer.parseInt(moveNumber), blackMoveCoord[0], blackMoveCoord[1]};
                        blackMoves.add(info);
                    }
                    //It is a wall move
                    else if (blackMoveCoord.length == 3) {
                        int[] info = {Integer.parseInt(moveNumber), blackMoveCoord[0], blackMoveCoord[1], blackMoveCoord[2]};
                        blackWalls.add(info);
                    } else {
                        //something went wrong
                        isSaveMoveValid = false;
                        return false;
                    }
                    movesTotal ++;
                }

                //.Setting initialize pawn position
                //White pawn will start at E9
                Tile whiteDefaultPos = new Tile(9,5,ModelQuery.getBoard());
                whitePlayerPosition = new PlayerPosition(ModelQuery.getWhitePlayer(), whiteDefaultPos);

                //Black pawn will start at E1
                Tile blackDefaultPos = new Tile(1,5,ModelQuery.getBoard());
                blackPlayerPosition = new PlayerPosition(ModelQuery.getBlackPlayer(), blackDefaultPos);

                GamePosition gamePosition = new GamePosition(positions.size() + 1,whitePlayerPosition,blackPlayerPosition,ModelQuery.getWhitePlayer(),ModelQuery.getCurrentGame());
                ModelQuery.getCurrentGame().setCurrentPosition(gamePosition);

                quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(whitePlayerPosition);
                quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(blackPlayerPosition);

                PawnController.initPawnSM(quoridor.getCurrentGame().getBlackPlayer(), blackPlayerPosition);
                PawnController.initPawnSM(quoridor.getCurrentGame().getWhitePlayer(), whitePlayerPosition);

                if(!ValidatePositionController.validateOverlappingPawns()){
                    isSaveMoveValid = false;
                    return false;
                }

                //.Add the walls for each player
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
                    gamePosition.addWhiteWallsInStock(wall);
                }
                for(int j = 1; j <= 10; j++){
                    Wall wall = Wall.getWithId(j + 10);
                    gamePosition.addBlackWallsInStock(wall);
                }


                QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getWhitePlayer());

                //How to know if the pawn is jumping or moving one tile?

                //.Using a for loop, executing the moves (creating a function that will simulate the players making their moves for one line)
                //Check the lists of each player and see if
                int wm = 0, ww = 0, bm = 0, bw = 0;
                for(int i = 0; i < movesTotal; i++) {
                    //i+1 is the moveNumber
                    if(ModelQuery.getPlayerToMove() == ModelQuery.getWhitePlayer()) {
                        if (!whiteMoves.isEmpty() && whiteMoves.get(wm)[0] == i + 1) {
                            String side;
                            //Moved the pawn before
                            if(wm > 0){
                                side = GetPawnMoveDirection(whiteMoves.get(wm-1), whiteMoves.get(wm));
                                if(side.equals("")) {
                                    isSaveMoveValid = false;
                                    return false;
                                }
                                PawnController.movePawn(side);
                            }
                            //Still at default position
                            else{
                                int[] defaultPos = {0,9,5};
                                side = GetPawnMoveDirection(defaultPos,whiteMoves.get(wm));
                                PawnController.movePawn(side);
                            }
                            wm++;
                        } else if (!whiteWalls.isEmpty() && whiteWalls.get(ww)[0] == i + 1) {
                            if(!ExecuteWallMove(whiteWalls, ww, i + 1, ModelQuery.getWhitePlayer())){
                                isSaveMoveValid = false;
                                return false;
                            }
                            ww++;
                        } else {
                            return false;
                        }
                        SwitchPlayerController.switchActivePlayer();
                    }
                    else{
                        //White player must always move first, so if PlayerToMove isn't white player, then somethign went wrong
                        return false;
                    }

                    //After doing the if statement above, it should run the if statement below since it would be black player's turn
                    if(ModelQuery.getPlayerToMove() == ModelQuery.getBlackPlayer()) {
                        if (!blackMoves.isEmpty() && blackMoves.get(bm)[0] == i + 1) {
                            String side;
                            //Moved the pawn before
                            if(bm > 0){
                                side = GetPawnMoveDirection(blackMoves.get(wm-1), blackMoves.get(wm));
                                if(side.equals("")){
                                    isSaveMoveValid = false;
                                    return false;
                                }
                                PawnController.movePawn(side);
                            }
                            //Still at default position
                            else{
                                int[] defaultPos = {0,1,5};
                                side = GetPawnMoveDirection(defaultPos,blackMoves.get(bm));
                                PawnController.movePawn(side);
                            }
                            bm++;
                        } else if (!blackWalls.isEmpty() && blackWalls.get(bw)[0] == i + 1) {
                            if(!ExecuteWallMove(blackWalls, bw, i + 1, ModelQuery.getBlackPlayer())){
                                isSaveMoveValid = false;
                                return false;
                            }
                            bw++;
                        } else {
                            return false;
                        }
                        SwitchPlayerController.switchActivePlayer();
                    }
                    else{
                        //If statement is not executed, therefore somethign went wrong
                        return false;
                    }
                }
            } catch (FileNotFoundException e) {
                //File not found error
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                //File is empty error
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    //.Helper methods

    /**
     * This helper method will set up the important parts of the game
     * @param whiteUser     The White player
     * @param blackUser     The Black player
     */
    private static void PrepareGame(String whiteUser, String blackUser){
        StartNewGameController.initializeGame();
        StartNewGameController.whitePlayerChoosesAUsername(whiteUser);
        StartNewGameController.blackPlayerChooseAUsername(blackUser);
    }

    /**
     * This helper method will move the wall at a given position
     * @param walls
     * @param index
     * @param moveNumber
     * @param player
     * @return
     */
    private static boolean ExecuteWallMove(List<int[]> walls, int index, int moveNumber, Player player){
        Direction wallDir = convertToDir(walls.get(index)[3]);
        if(!ValidatePositionController.validateWallPosition(walls.get(index)[2], walls.get(index)[1], wallDir)){
            isPositionValid = false;
            return false;
        }

        Player whitePlayer = ModelQuery.getWhitePlayer();
        Player blackPlayer = ModelQuery.getBlackPlayer();
        Tile wallTile = new Tile(walls.get(index)[2], walls.get(index)[1], ModelQuery.getBoard());
        Game currentGame = ModelQuery.getCurrentGame();

        WallMove wallmove;
        int roundNum = 0;
        if(player.equals(ModelQuery.getWhitePlayer())){
            Wall dropWall = ModelQuery.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().get(index);
            roundNum = moveNumber * 2 - 1;
            wallmove = new WallMove(moveNumber, roundNum, whitePlayer, wallTile, currentGame, wallDir, dropWall);
            loadWall(wallmove,ModelQuery.getWhitePlayer());
        }
        else if(player.equals(ModelQuery.getBlackPlayer())){
            Wall dropWall = ModelQuery.getCurrentGame().getCurrentPosition().getBlackWallsInStock().get(index);
            roundNum = moveNumber * 2;
            wallmove = new WallMove(moveNumber, roundNum, blackPlayer, wallTile, currentGame, wallDir, dropWall);
            loadWall(wallmove,ModelQuery.getBlackPlayer());
        }
        else{
            return false;
        }
        return true;
    }

    /**
     * This helper method will read the new and old move sets and determine
     * the direction in movement performed by the pawn
     * @param oldMove   Array of the move coordinates
     * @param newMove   Array of the move coordinates
     * @return          String that contains the direction
     *                  Empty string if an error occured
     */
    private static String GetPawnMoveDirection(int[] oldMove, int[] newMove){
        //[0] MoveNumber, [1] Row, [2] Column
        if(newMove[1] == oldMove[1] && newMove[2] < oldMove[2]){
            return "up";
        }
        else if(newMove[1] > oldMove[1] && newMove[2] < oldMove[2]){
            return "upright";
        }
        else if(newMove[1] < oldMove[1] && newMove[2] < oldMove[2]){
            return "upleft";
        }
        else if(newMove[1] > oldMove[1] && newMove[2] == newMove[2]){
            return "right";
        }
        else if(newMove[1] < oldMove[1] && newMove[2] == newMove[2]){
            return "left";
        }
        else if(newMove[1] > oldMove[1] && newMove[2] > oldMove[2]){
            return "downright";
        }
        else if(newMove[1] < oldMove[1] && newMove[2] < oldMove[2]){
            return "downleft";
        }
        else if(newMove[1] == oldMove[1] && newMove[2] > oldMove[2]){
            return "down";
        }
        else{
            return "";
        }
    }
}

//TODO: implement a warning when the move doesn't make sense, for example pawn jumped 4 tiles