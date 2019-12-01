package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.controllers.GameStatusController;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.view.InitializeBoardController;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javafx.scene.control.Button;

import java.sql.Time;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GameStatusStepDefinition {
    InitializeBoardController controller;
    private Button button;

    @Given("The following moves were executed:")
    public void theFollowingMovesWereExecuted(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> valueMaps = dataTable.asMaps();
        Game game = ModelQuery.getCurrentGame();

        Player[] players = {ModelQuery.getWhitePlayer(), ModelQuery.getBlackPlayer()};
        int playerIdx = 0;
        for (Map<String, String> map : valueMaps) {
            // Getting the current parameters
            Integer move = Integer.decode(map.get("move"));
            Integer turn = Integer.decode(map.get("turn"));
            Integer row = Integer.decode(map.get("row"));
            Integer col = Integer.decode(map.get("col"));

            // create moves
            Tile target = ModelQuery.getTile(row, col);
            Move curr_move = new StepMove(move, turn, players[playerIdx], target, game);
            game.addMove(curr_move);
            playerIdx++;
            playerIdx = playerIdx % 2;
        }
    }

    @And("The last move of {string} is pawn move to {int}:{int}")
    public void theLastMoveOfIsPawnMoveToRow(String arg0, int row, int col) {
//        Getting the game
        Game game = ModelQuery.getCurrentGame();

        // Getting the player
        Player current_player = stringToPlayer(arg0);

        // get move number and round number
        int num_moves = ModelQuery.getCurrentGame().getMoves().size();
        StepMove last_move = (StepMove) ModelQuery.getCurrentGame().getMove(num_moves - 1);

        int turn = last_move.getMoveNumber();
        int round = last_move.getRoundNumber();

        Tile target = ModelQuery.getTile(row, col);

        StepMove move = new StepMove(turn, round, current_player, target, game);

        // add it to the list
        game.addMove(move);
    }

    @When("Checking of game result is initated")
    public void checkingOfGameResultIsInitated() {
        GameStatusController.checkGameStatus();
    }

    @Then("Game result shall be {string}")
    public void gameResultShallBe(String arg0) {
        assertEquals(stringToStatus(arg0), ModelQuery.getCurrentGame().getGameStatus());
    }
    /* Resign game */
    /**
     * @author Jason Lau
     */

    public Player playerToMove;
    public Player winningPlayer;


    /* Identify Game Won */
    Player currentPlayer;
    String player;
    int rowVal;
    int colVal;
    @Given("Player {string} has just completed his move")
    public void givenPlayerHasJustCompletedHisMove(String arg0) {
        if (arg0.equals("white")) {
            currentPlayer = ModelQuery.getWhitePlayer();
            ModelQuery.getCurrentPosition().setPlayerToMove(currentPlayer);
//			PawnController.movePawn("left");
        }
        else if (arg0.equals("black")) {
            currentPlayer = ModelQuery.getBlackPlayer();
            ModelQuery.getCurrentPosition().setPlayerToMove(currentPlayer);
//			PawnController.movePawn("left");
        }
    }

    @And ("The new position of {string} is {int}:{int}")
    public void theNewPositionOfPlayerIs(String arg0, int row, int col) {
        rowVal = 10 - row;
        player = arg0;
        Tile tile = ModelQuery.getTile(rowVal, col);
        if (arg0.equals("white")) {
            ModelQuery.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
        }
        else if(arg0.equals("black")) {
            ModelQuery.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
        }
    }

    @And ("The clock of {string} is more than zero")
    public void theClockOfPlayerIsMoreThanZero(String arg0) {
        Time newThinkingTime = new Time(180);
        if (arg0.equals("white")) {
            ModelQuery.getWhitePlayer().setRemainingTime(newThinkingTime);
        }
        else if (arg0.equals("black")) {
            ModelQuery.getBlackPlayer().setRemainingTime(newThinkingTime);
        }
    }

//	@Then ("Game result shall be {string}")
//	public void gameResultShallBe (String arg0) {
//    	Quoridor quoridor = QuoridorApplication.getQuoridor();
//    	if (arg0.equals("BlackWon")){
//    		Assert.assertEquals(winningPlayer,quoridor.getCurrentGame().getBlackPlayer());
//    	}
//    	else if (arg0.equals("WhiteWon")){
//    		Assert.assertEquals(winningPlayer,quoridor.getCurrentGame().getWhitePlayer());
//    	}
//    	if (rowVal != 0 && colVal != 0) {
//			String result = EndGameController.checkPawnPosition(player, rowVal, colVal);
//			assertEquals(arg0, result);
//		}
//	}

    @And ("The game shall no longer be running")
    public void theGameShallNoLongerBeRunning () {
        //TODO: GUI Step
    }


    @When ("The clock of {string} counts down to zero")
    public void theClockOfPlayerCountsDownToZero(String arg0) {
        player = arg0;
        // Try setting the remaining time to zero
        Time newThinkingTime = new Time(0);
        if (player.equals("white")) {
            ModelQuery.getWhitePlayer().setRemainingTime(newThinkingTime);
        }
        else if (player.equals("black")) {
            ModelQuery.getBlackPlayer().setRemainingTime(newThinkingTime);
        }
        GameStatusController.checkGameStatus();
    }



    @Then("The final result shall be displayed")
    public void theFinalResultShallBeDisplayed() {
        //TODO: GUI Step

    }

    @And("White's clock shall not be counting down")
    public void whiteSClockShallNotBeCountingDown() {
        //TODO: GUI Step
    }

    @And("Black's clock shall not be counting down")
    public void blackSClockShallNotBeCountingDown() {
        //TODO: GUI Step
    }

    @And("White shall be unable to move")
    public void whiteShallBeUnableToMove() {
        //TODO: GUI Step

    }

    @And("Black shall be unable to move")
    public void blackShallBeUnableToMove() {
        //TODO: GUI step

    }


    private Player stringToPlayer(String playerColor) {
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

    private Game.GameStatus stringToStatus(String status) {
        switch (status) {
            case "drawn":
            case "Drawn": {
                return Game.GameStatus.Draw;
            }
            case "pending":
            case "Pending": {
                return Game.GameStatus.Running;
            }
            case "blackWon":
            case "BlackWon": {
                return Game.GameStatus.BlackWon;
            }
            case "whiteWon":
            case "WhiteWon": {
                return Game.GameStatus.WhiteWon;
            }
            default: {
                return null;
            }
        }
    }
}
