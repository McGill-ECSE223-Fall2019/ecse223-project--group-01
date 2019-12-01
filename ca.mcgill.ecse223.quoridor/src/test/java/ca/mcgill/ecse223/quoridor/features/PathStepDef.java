package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.WallController;
import ca.mcgill.ecse223.quoridor.model.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PathStepDef {
    private List<Player> playerList;


    @Given("A {string} wall move candidate exists at position {int}:{int}")
    public void aWallMoveCandidateExistsAtPositionFrow(String dir, int row,int col) {
        setupWallMoveCandidates(row,col,stringToDirection(dir));
    }

    @And("The black player is located at {int}:{int}")
    public void theBlackPlayerIsLocatedAtBrow(int row,int col) {
        Tile target = ModelQuery.getTile(row,col);
        ModelQuery.getBlackPosition().setTile(target);
    }

    @And("The white player is located at {int}:{int}")
    public void theWhitePlayerIsLocatedAtWrow(int row,int col) {
        Tile target = ModelQuery.getTile(row,col);
        ModelQuery.getWhitePosition().setTile(target);
    }

    @When("Check path existence is initiated")
    public void checkPathExistenceIsInitiated() {
        playerList = WallController.pathExistsForPlayers();
    }

    @Then("Path is available for {string} player\\(s)")
    public void pathIsAvailableForPlayerS(String result) {
        switch(result){
            case "both":{
                assertEquals(2,playerList.size());
                break;
            }
            case "none":{
                assertEquals(0,playerList.size());
                break;
            }
            case "black":{
                assertEquals(ModelQuery.getBlackPlayer(), playerList.get(0));
                break;
            }
            case "white":{
                assertEquals(ModelQuery.getWhitePlayer(), playerList.get(0));
                break;
            }
            default:
                break;
        }
    }

    private void setupWallMoveCandidates(int row, int col, Direction direction) {
        Player player1 = ModelQuery.getWhitePlayer();
        Board board = ModelQuery.getBoard();
        Game game = ModelQuery.getCurrentGame();
        Wall wall = player1.getWall(3);

        WallMove move = new WallMove(0, 1, player1, board.getTile((row - 1) * 9 + col - 1), game, direction, wall);
        game.setWallMoveCandidate(move);
    }

    private Direction stringToDirection(String direction){
        switch (direction){
            case "horizontal":{
                return Direction.Horizontal;
            }
            case "vertical":{
                return Direction.Vertical;
            }
            default:{
                return null;
            }
        }
    }


    @And("The game has a final result")
    public void theGameHasAFinalResult() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game.GameStatus status =quoridor.getCurrentGame().getGameStatus();
    }
}
