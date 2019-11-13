package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.controllers.PawnController;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.util.PendingException;

public class PawnStepDefinition {

    // ***********************************************
    // Background step definitions
    // ***********************************************

    @Given("^The game is not running$")
    public void theGameIsNotRunning() {
        initQuoridorAndBoard();
        createUsersAndPlayers("user1", "user2");
    }

    @Given("^The game is running$")
    public void theGameIsRunning() {
        initQuoridorAndBoard();
        ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
        createAndStartGame(createUsersAndPlayers);
    }

    /**
     * Move Pawn Step Definition
     * @author Kevin Li
     */

    //This section is move one tile
    @Given("The player to move is {string}")
    public void thePlayerToMoveIs(String player){

    }

    @And("The player is located at {int}:{int}")
    public void thePlayerIsLocatedAt(int row, int col){

    }

    @And("There are no {string} walls {string} from the player")
    public void thereAreNoWallsFromThePlayer(String dir, String side){

    }

    @And("The opponent is not {string} from the player")
    public void theOpponentIsNotFromThePlayer(String side){

    }

    @When("Player {string} initiates to move {string}")
    public void iInitiateToLoadASavedGameFilename(String playerColor, String side) {
        try{
            PawnController.movePawn(side);
        }catch (NotImplementedException e){
            throw new PendingException();
        }
    }

    @Then("The move {string} shall be {string}")
    public void theMoveShallBe(String side, String status){

    }

    @And("Player's new position shall be {int}:{int}")
    public void playerNewPositionShallBe(int nrow, int ncol){

    }

    @And("The next player to move shall become {string}")
    public void theNextPlayerToMoveShallBecome(String nplayer){

    }

    //This section is move of player blocked by wall

    @And("There is a {string} wall at {int}:{int}")
    public void thereIsAWallAt(String dir, int wrow, int wcol){

    }

    @And("My opponent is not {string} from the player")
    public void myOpponentIsNotFromThePlayer(String side){

    }
}
