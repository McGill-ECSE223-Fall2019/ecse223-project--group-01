package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.controllers.PawnController;
import cucumber.api.PendingException;
import io.cucumber.java.en.When;



public class PawnStepDefinition {


    @When("Player {string} initiates to move {string}")
    public void iInitiateToLoadASavedGameFilename(String playerColor, String side) {
        try{
            PawnController.movePawn(side);
        }catch (UnsupportedOperationException e){
            throw new PendingException();
        }

    }
}
