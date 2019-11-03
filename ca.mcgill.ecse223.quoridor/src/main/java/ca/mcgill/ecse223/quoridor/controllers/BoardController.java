package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

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
    public static boolean initializeBoard()throws UnsupportedOperationException{

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


        PlayerPosition whitePlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), quoridor.getBoard().getTile(36));
        PlayerPosition blackPlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), quoridor.getBoard().getTile(44));


        List<GamePosition> positions = ModelQuery.getCurrentGame().getPositions();


        GamePosition gameposition = new GamePosition(positions.size()+1, whitePlayerPos, blackPlayerPos, quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame());

        quoridor.getCurrentGame().setCurrentPosition(gameposition);

        ModelQuery.getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getCurrentGame().getWhitePlayer());
        ModelQuery.getCurrentGame().getCurrentPosition().setWhitePosition(whitePlayerPos);
        ModelQuery.getCurrentGame().getCurrentPosition().setBlackPosition(blackPlayerPos);

        for(int i =1; i <= 10; i++){
            ModelQuery.getCurrentGame().getWhitePlayer().addWall(i);
        }
        for(int j = 11; j <= 20; j++) {
            ModelQuery.getCurrentGame().getBlackPlayer().addWall(j);
        }
        ModelQuery.getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getWhitePlayer());

        //TODO White's clock should be counting down(

        //TODO It should be shown that it's white's turn




        return true;

    }
}
