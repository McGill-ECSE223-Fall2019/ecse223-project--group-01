package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class ReplayModeController {
    public static void enterReplayMode(){

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        StartNewGameController.initializeGame();
        quoridor.getCurrentGame().setGameStatus(Game.GameStatus.Replay);
    }

    public static void continueGame(){

        Quoridor quoridor = QuoridorApplication.getQuoridor();

        quoridor.getCurrentGame().setGameStatus(Game.GameStatus.ReadyToStart);
    }
}
