package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.util.List;


public class BoardController { 
    /**
     *This controller method is responsible for initializing the board when quoridor is initialized
     * Returns true if it is successfully initialized
     *
     *
     * @return  outcome of operation
     * @throws UnsupportedOperationException
     *
     * @author: Jason Lau
     */
    public static boolean initializeBoard() throws UnsupportedOperationException{

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Board board;
        if(quoridor.getBoard() == null){
            board = new Board(quoridor);
            for (int i = 1; i <= 9; i++) { // rows
                for (int j = 1; j <= 9; j++) { // columns
                    board.addTile(i, j);
                }
            }
        }
        else{
            board = quoridor.getBoard();
        }

        PlayerPosition whitePlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), ModelQuery.getTile(9,5));
        PlayerPosition blackPlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), ModelQuery.getTile(1,5));

        List<GamePosition> positions = ModelQuery.getCurrentGame().getPositions();


        GamePosition gameposition = new GamePosition(positions.size()+1, whitePlayerPos, blackPlayerPos, quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame());
        
        quoridor.getCurrentGame().setCurrentPosition(gameposition);

        ModelQuery.getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getCurrentGame().getWhitePlayer());
        ModelQuery.getCurrentGame().getCurrentPosition().setWhitePosition(whitePlayerPos);
        ModelQuery.getCurrentGame().getCurrentPosition().setBlackPosition(blackPlayerPos);

        PawnController.initPawnSM(quoridor.getCurrentGame().getWhitePlayer(), whitePlayerPos);
        PawnController.initPawnSM(quoridor.getCurrentGame().getBlackPlayer(), blackPlayerPos);
        
        //if Four Player, do all this extra stuff
        if(ModelQuery.isFourPlayer()) { 
        	PlayerPosition redPlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getRedPlayer(), ModelQuery.getTile(5,1));
        	PlayerPosition greenPlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getGreenPlayer(), ModelQuery.getTile(5,9));
        	gameposition.setRedPosition(redPlayerPos);
        	gameposition.setGreenPosition(greenPlayerPos);
        	ModelQuery.getCurrentGame().getCurrentPosition().setRedPosition(redPlayerPos);
        	ModelQuery.getCurrentGame().getCurrentPosition().setGreenPosition(greenPlayerPos);
            PawnController.initPawnSM(quoridor.getCurrentGame().getRedPlayer(), redPlayerPos);
            PawnController.initPawnSM(quoridor.getCurrentGame().getGreenPlayer(), greenPlayerPos);
        }

        for(int i =1; i <= 10; i++){
            ModelQuery.getCurrentGame().getWhitePlayer().addWall(i);
            ModelQuery.getCurrentGame().getBlackPlayer().addWall(10+i);
            if(ModelQuery.isFourPlayer()) {
            	ModelQuery.getCurrentGame().getRedPlayer().addWall(20+i);
            	ModelQuery.getCurrentGame().getGreenPlayer().addWall(30+i);
            }
        }
     
        ModelQuery.getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getWhitePlayer());

        for(Wall wall: ModelQuery.getWhitePlayer().getWalls()){
            ModelQuery.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(wall);
        }

        for(Wall wall: ModelQuery.getBlackPlayer().getWalls()){
            ModelQuery.getCurrentGame().getCurrentPosition().addBlackWallsInStock(wall);
        }
        
        if(ModelQuery.isFourPlayer()) {
	        for(Wall wall: ModelQuery.getRedPlayer().getWalls()){
	            ModelQuery.getCurrentGame().getCurrentPosition().addRedWallsInStock(wall);
	        }
	        
	        for(Wall wall: ModelQuery.getGreenPlayer().getWalls()){
	            ModelQuery.getCurrentGame().getCurrentPosition().addGreenWallsInStock(wall);
	        }
        }
        
        
	    return true;

    }
}
