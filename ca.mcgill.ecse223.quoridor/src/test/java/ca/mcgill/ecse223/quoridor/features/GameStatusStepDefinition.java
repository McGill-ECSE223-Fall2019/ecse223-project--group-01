package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.controllers.GameStatusController;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.view.InitializeBoardController;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GameStatusStepDefinition extends ApplicationTest {
    InitializeBoardController controller;
    private Button button;

    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("InitializeBoard.fxml")));
        Parent root1 = (Parent) fxmlLoader.load();
        controller = (InitializeBoardController) fxmlLoader.getController();
        button  = controller.grabWallBtn;
        stage.setTitle("Quit Game");
        stage.setScene(new Scene(root1));
        stage.show();
        stage.toFront();
    }

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

    @Given("Player {string} has just completed his move")
    public void playerHasJustCompletedHisMove(String arg0) {
        Player player = stringToPlayer(arg0);
        ModelQuery.getCurrentPosition().setPlayerToMove(player);
    }

    @And("The last move of {string} is pawn move to {int}:{int}")
    public void theLastMoveOfIsPawnMoveToRow(String arg0, int row, int col) {
//        Getting the game
        Game game = ModelQuery.getCurrentGame();

        // Getting the player
        Player current_player = stringToPlayer(arg0);

        // get move number and round number
        int num_moves = ModelQuery.getCurrentGame().getMoves().size();
        StepMove last_move =(StepMove) ModelQuery.getCurrentGame().getMove(num_moves-1);

        int turn = last_move.getMoveNumber();
        int round =last_move.getRoundNumber();

        Tile target = ModelQuery.getTile(row,col);

        StepMove move = new StepMove(turn,round,current_player,target,game);

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

    @Test
    @And("The game shall no longer be running")
    public void theGameShallNoLongerBeRunning() {
//        clickOn(button);
//        Assertions.assertThat(lookup("#grabWallBtn").queryAs(Button.class)).hasText("click me!");
//        assertEquals(InitializeBoardController.PlayerState.PAWN, controller.state);
//        assertNull(ModelQuery.getCurrentGame().getCurrentPosition().getPlayerToMove());
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

    private Game.GameStatus stringToStatus(String status){
        switch (status){
            case "Drawn":{
                return Game.GameStatus.Draw;
            }
            case "Pending":{
                return Game.GameStatus.Running;
            }
            default:{
                return null;
            }
        }
    }
}