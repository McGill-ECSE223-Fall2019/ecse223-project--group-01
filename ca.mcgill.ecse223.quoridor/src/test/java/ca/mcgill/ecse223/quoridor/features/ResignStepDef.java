package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.ResignGameController;
import ca.mcgill.ecse223.quoridor.controllers.UserController;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.User;
import cucumber.api.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ResignStepDef {

    private Player playerToMove;

    @Given("The player to move is {player}")
    public void nextPlayerToMove(String argo){
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        if(argo.equals("white")){
            playerToMove = quoridor.getCurrentGame().getWhitePlayer();
        }
        else if(argo.equals("black")){
            playerToMove = quoridor.getCurrentGame().getBlackPlayer();
        }
    }

    @When("Player initates to resign")
    public void playerInitatesToResign() {
        try {


            }
        catch(UnsupportedOperationException e){
            throw new PendingException();

        }

        }



    @Then("Game result shall be {string}")
    public void gameResultShallBe(String arg0) {
    }

    @And("The game shall no longer be running")
    public void theGameShallNoLongerBeRunning() {
    }
}
