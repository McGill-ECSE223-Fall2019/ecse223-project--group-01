package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.controllers.PawnController;
import io.cucumber.java.en.When;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.util.PendingException;

public class PawnStepDefinition {


    @When("Player {string} initiates to move {string}")
    public void iInitiateToLoadASavedGameFilename(String playerColor, String side) {
        try{
            PawnController.movePawn(side);
        }catch (NotImplementedException e){
            throw new PendingException();
        }

    }
}
