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
 * @author Kevin & Kate
 */
@SuppressWarnings("ALL")
public class SaveLoadGameController {

    public static boolean isSaveMoveValid = true;

    //This variable is the save location of the .mov files
    static String saveLocation = "./";

    /**
     * Attempts to create or overwrite a saved game.
     * The save game will contain the move number, player 1's move and player 2's move.
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
        List<Move> listOfMoves = ModelQuery.getMoves();
        /* NOT SURE IF THIS PART OF THE CODE WORKS*/
        //.*Not going into for loop, because there is nothing in listOfMoves
        List<int[]> whiteMoves = new ArrayList();
        List<int[]> blackMoves = new ArrayList();

        for(int i = 0; i < listOfMoves.size(); i++){
            Move move = listOfMoves.get(i);
            int moveNumber = move.getMoveNumber();
            Tile target = move.getTargetTile();
            int[] whiteCoordinates = {target.getRow(), target.getColumn()};
        }


        //After being able to store all the moves and move numbers into a list,
        //Use the methods defined below to turn them into save template and append to file


        int numPlayers;
        if(ModelQuery.isFourPlayer()) {
            numPlayers = 4;
        } else {
            numPlayers = 2;
        }

        int moveCounter = 1;
        int roundCounter = 0;

        for(Move move: listOfMoves) {
            roundCounter++;
            Tile tile = move.getTargetTile();

            char columnLetter = 'e';
            switch(tile.getColumn()) {
                case 1:
                    columnLetter = 'a';
                    break;
                case 2:
                    columnLetter = 'b';
                    break;
                case 3:
                    columnLetter = 'c';
                    break;
                case 4:
                    columnLetter = 'd';
                    break;
                case 5:
                    columnLetter = 'e';
                    break;
                case 6:
                    columnLetter = 'f';
                    break;
                case 7:
                    columnLetter = 'g';
                    break;
                case 8:
                    columnLetter = 'h';
                    break;
                case 9:
                    columnLetter = 'i';
                    break;
            }


            if(move.getPlayer().hasGameAsWhite()) { //if player is white
                output.write(moveCounter++ + ". ");
                output.write(columnLetter+""+tile.getRow());
            } else {
                output.write(columnLetter+""+tile.getRow());
            }

            if(move instanceof WallMove) {
                if (((WallMove) move).getWallDirection().equals(Direction.Horizontal)) {
                    output.write("h ");
                } else {
                    output.write("v ");
                }
            } else {
                output.write(" ");
            }


            if(roundCounter==numPlayers) {
                output.write("\n");
                roundCounter=0;
            }
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
    public static boolean fileLoad(String filename, String whiteUser, String blackUser) throws java.lang.UnsupportedOperationException, IOException{
        NukeData();
        File saveFile = new File(saveLocation + filename);
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        boolean isMoveValid = false;

        //Making the game run
        PrepareGame(whiteUser, blackUser);
        SetNextPlayers();
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
                    String[] categorySplit = line.split("\\. "); //require double brackets because of unwanted regex: [0] Move Number, [1] White Move + Black Move
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

                 BoardController.initializeBoard();
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

                if (gamePosition.getBlackWallsInStock().size() == 0 || gamePosition.getWhiteWallsInStock().size() == 0){
                    //AddWalls for players
                    for(int j = 1; j <= 10; j++){
                        Wall wall = Wall.getWithId(j);
                        gamePosition.addWhiteWallsInStock(wall);
                    }
                    for(int j = 1; j <= 10; j++){
                        Wall wall = Wall.getWithId(j + 10);
                        gamePosition.addBlackWallsInStock(wall);
                    }
                }

                if(!ValidatePositionController.validateOverlappingPawns()){
                    isSaveMoveValid = false;
                    return false;
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
                                int[] defaultPos = {0,5,9};
                                side = GetPawnMoveDirection(defaultPos,whiteMoves.get(wm));
                                PawnController.movePawn(side);
                            }
                            wm++;
                        } else if (!whiteWalls.isEmpty() && whiteWalls.get(ww)[0] == i + 1) {
                            if(!ExecuteWallMove(whiteWalls, ww, i + 1, ModelQuery.getWhitePlayer())){
                                isSaveMoveValid = false;
                                return false;
                            }
                            else{
                                SwitchPlayerController.switchActivePlayer();
                            }
                            ww++;
                        } else {
                            return false;
                        }
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
                                side = GetPawnMoveDirection(blackMoves.get(bm-1), blackMoves.get(bm));
                                if(side.equals("")){
                                    isSaveMoveValid = false;
                                    return false;
                                }
                                PawnController.movePawn(side);
                            }
                            //Still at default position
                            else{
                                int[] defaultPos = {0,5,1};
                                side = GetPawnMoveDirection(defaultPos,blackMoves.get(bm));
                                PawnController.movePawn(side);
                            }
                            bm++;
                        } else if (!blackWalls.isEmpty() && blackWalls.get(bw)[0] == i + 1) {
                            if(!ExecuteWallMove(blackWalls, bw, i + 1, ModelQuery.getBlackPlayer())){
                                isSaveMoveValid = false;
                                return false;
                            }
                            else{
                                SwitchPlayerController.switchActivePlayer();
                            }
                            bw++;
                        } else {
                            return false;
                        }
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
        else{
            //File doesn't exist
            return false;
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

    /**
     * This helper method will convert the coordinates for wall move into board coordinate string [ColumnRowOrientation]
     * @param col   this is the column coordinate
     * @param row   this is the row coordinate
     * @param dir   this is the orientation of the wall
     * @return      String that follows the coordinate template
     */
    private static String convertWallCoord(int col, int row, int dir){
        char columnLetter = (char)(col + 96);
        String orientation = convertWallDir(convertToDir(dir));
        return String.valueOf(columnLetter)+Integer.toString(row)+orientation;
    }

    /**
     * This helper method will convert the coordinates for mvoe pawn into board coordinate string [ColumnRow]
     * @param col   this is the column coordinate
     * @param row   this is the row coordinate
     * @return      String that follows the coordinate template
     */
    private static String convertPawnCoord(int col, int row){
        char columnLetter = (char)(col + 96);
        return String.valueOf(columnLetter)+Integer.toString(row);
    }

    /**
     * This helper method will convert a integer move number into a string
     * @param moveNum   this is the move number
     * @return          String that represents the moven umber
     */
    private static String convertMoveNum(int moveNum){
        return Integer.toString(moveNum);
    }

    /**
     * This helper method will convert all the move information and move number into a saveGame template
     * @param moveNum       the move Number
     * @param whiteMove     White player move, either move pawn or drop wall
     * @param blackMove     Black player move, either move pawn or drop wall
     * @return              String that follows the save game template
     */
    private static String convertLine(String moveNum, String whiteMove, String blackMove){
        return moveNum + ". " + whiteMove + " " + blackMove;
    }

    private static void SetNextPlayers(){
        ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.ReadyToStart);
        ModelQuery.getWhitePlayer().setNextPlayer(ModelQuery.getBlackPlayer());
        ModelQuery.getBlackPlayer().setNextPlayer(ModelQuery.getWhitePlayer());
    }

    public static void NukeData(){
        if(ModelQuery.getCurrentGame()!=null){
            QuoridorApplication.getQuoridor().getCurrentGame().delete();
        }
        if(QuoridorApplication.getQuoridor().getBoard()!=null){
            QuoridorApplication.getQuoridor().getBoard().delete();
        }
        int counter = 0;
        for(int i = 0;i<1000;i++){
            if(Wall.getWithId(counter)!=null){
                Wall.getWithId(counter).delete();
            }
        }
    }
}

//TODO: implement a warning when the move doesn't make sense, for example pawn jumped 4 tiles