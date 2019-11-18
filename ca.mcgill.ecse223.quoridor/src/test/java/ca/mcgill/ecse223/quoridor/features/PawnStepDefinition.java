package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.PawnController;

import ca.mcgill.ecse223.quoridor.model.*;
import cucumber.api.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;


public class PawnStepDefinition {

    private boolean isLegalMove;
    private Player currentPlayer;

    /**
     * @author Kate Ward
     */
    @And("The player is located at {int}:{int}")
    public void thePlayerIsLocatedAtRowCol(int row, int col) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Player player = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
        //player is white
        if (player.equals(quoridor.getCurrentGame().getWhitePlayer())) {
            Tile tile = ModelQuery.getTile(row, col);
            quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
        }
        //player is black
        else {
            Tile tile = ModelQuery.getTile(row, col);
            quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
        }
        player.getStatemachine().exit();
        player.getStatemachine().enter();
    }

    /**
     * @author Kate Ward
     */
    @And("The opponent is located at {int}:{int}")
    public void theOpponentIsLocatedAtRowCol(int row, int col) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Player player = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
        //player is not white (opponent is white)
        if (!player.equals(quoridor.getCurrentGame().getWhitePlayer())) {
            Tile tile = new Tile(row, col, quoridor.getBoard());
            quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
        }
        //player is not black (opponent is black)
        else {
            Tile tile = new Tile(row, col, quoridor.getBoard());
            quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
        }
    }

    /**
     * @author Kate Ward
     */
    @And("There are no {string} walls {string} from the player nearby")
    public void thereAreNoDirWallsSideFromThePlayerNearby(String dir, String side) {
        //do nothing
    }

    /**
     * @author Kate Ward
     */
    @When("Player {string} initiates to move {string}")
<<<<<<< HEAD
    public void iInitiateToLoadASavedGameFilename(String playerColor, String side) {
        try{
            PawnController.movePawn(side);
        }catch (UnsupportedOperationException e){
=======
    public void playerInitiatesToMoveSide(String playerColor, String side) {
        currentPlayer = stringToPlayer(playerColor);
        try {
            isLegalMove = PawnController.movePawn(side);
        } catch (UnsupportedOperationException e) {
>>>>>>> 795c32d58f45c73692d2882b447a396c9bef27a4
            throw new PendingException();
        }
    }

    /**
     * @author Kate Ward
     */
    @Then("The move {string} shall be {string}")
    public void theMoveSideShallBeStatus(String side, String status) {
        if (status.equals("success")) {
            assertTrue(isLegalMove);
        } else {
            assertFalse(isLegalMove);
        }
    }

    /**
     * @author Kate Ward
     */
    @And("Player's new position shall be {int}:{int}")
    public void playersNewPositionShallBeRowCol(int row, int col) {
        Tile tile;
        //player is white
        if (currentPlayer.equals(ModelQuery.getWhitePlayer())) {
            tile = ModelQuery.getCurrentPosition().getWhitePosition().getTile();
        }
        //player is black
        else {
            tile = ModelQuery.getCurrentPosition().getBlackPosition().getTile();
        }
        assertEquals(row, tile.getRow());
        assertEquals(col, tile.getColumn());
    }

    //scenario outline: Jump of player blocked by wall

    /**
     * @author Kate Ward
     */
    @And("There is a {string} wall at {int}:{int}")
    public void thereIsAWallAtRowCol(String dir, int row, int col) {
        //place a wall
        Direction direction = stringToDirection(dir);
        Player player = ModelQuery.getWhitePlayer();
        Board board = ModelQuery.getBoard();
        Game game = ModelQuery.getCurrentGame();
        Wall wall = player.getWall(1);
        WallMove move = new WallMove(0, 1, player, board.getTile((row - 1) * 9 + col - 1), game, direction, wall);
        game.setWallMoveCandidate(move);
        ModelQuery.getCurrentPosition().addWhiteWallsOnBoard(wall);
    }


    /**
     * @author Kate Ward
     */
    @And("The opponent is not {string} from the player")
    public void theOpponentIsNotFromThePlayer(String side) {
        // Do nothing
    }

    /**
     * @author Kate Ward
     */
    @And("My opponent is not {string} from the player")
    public void myOpponentIsNotFromThePlayer(String arg0) {
//		 Do nothing
    }

    /**
     * @author Kate Ward
     */
    @And("There are no {string} walls {string} from the player")
    public void thereAreNoWallsFromThePlayer(String arg0, String arg1) {
//		 Do nothing
    }

    private Direction stringToDirection(String direction) {
        switch (direction) {
            case "horizontal": {
                return Direction.Horizontal;
            }
            case "vertical": {
                return Direction.Vertical;
            }
            default: {
                return null;
            }
        }
    }

    private Player stringToPlayer(String playerColor){
        switch (playerColor) {
            case "white": {
                return ModelQuery.getWhitePlayer();
            }
            case "black": {
                return ModelQuery.getBlackPlayer();
            }
            default: {
                return null;
            }
        }
    }

    //scenario outline: Diagonal jumps of a player near wall. no new methods
    //scenario outline: Diagonal jump over opponent at edge of the board. no new methods
}
