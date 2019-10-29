package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.*;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import cucumber.api.PendingException;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;


public class CucumberStepDefinitions {

	private boolean fileInSystem;
	private boolean fileChanged;
	private boolean displayError;

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

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
			Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

			String dir = map.get("wdir");

			Direction direction;
			switch (dir) {
			case "horizontal":
				direction = Direction.Horizontal;
				break;
			case "vertical":
				direction = Direction.Vertical;
				break;
			default:
				throw new IllegalArgumentException("Unsupported wall direction was provided");
			}
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1), quoridor.getCurrentGame(), direction, wall);
			if (playerIdx == 0) {
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// GUI-related feature -- TODO for later
	}

	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}


	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	/*
	 * TODO Insert your missing step definitions here
	 *
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 *
	/**
	 * @author Kevin Li
	 */
	@When("I initiate to load a saved game {string}")
	public void iInitiateToLoadASavedGameFilename(String filename) {
		try {
			PositionController.loadGame(filename);
		}catch(java.lang.UnsupportedOperationException e){
			throw new PendingException();
		}
	}

	/**
	 * @author Kevin Li
	 */
	@And("The position to load is valid")
	public void thePositionToLoadIsValid() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gameposition = quoridor.getCurrentGame().getCurrentPosition();
		try {
			boolean isPositionValid = PositionController.validatePosition(gameposition);
		} catch(java.lang.UnsupportedOperationException e){
			throw new PendingException();
		}
	}

	/**
	 * @author Kevin Li
	 */
	@Then("It shall be {string}'s turn")
	public void itShallBe(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player expectedPlayer = quoridor.getCurrentGame().getBlackPlayer();
		assertEquals(player,expectedPlayer.getUser().getName());
	}

	/**
	 * @author Kevin Li
	 */
	@And("{string} shall be at {int}:{int}")
	public void ShallBeAt(String player, int row, int col) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition position = quoridor.getCurrentGame().getCurrentPosition();

		if(player.equals("player"))
		{
			int p_row = position.getWhitePosition().getTile().getRow();
			int p_col = position.getWhitePosition().getTile().getColumn();
			assertEquals(row, p_row);
			assertEquals(col, p_col);
		}

		else if(player.equals("opponent"))
		{
			int o_row = position.getBlackPosition().getTile().getRow();
			int o_col = position.getWhitePosition().getTile().getColumn();
			assertEquals(row, o_row);
			assertEquals(col, o_col);
		}
	}

	/**
	 * @author Kevin Li
	 */
	@And("{string} shall have a {string} wall at {int}:{int}")
	public void shallHaveAWallAt(String player, String orientation, int row, int col) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();

		Player currentPlayer;
		Direction ExpectedDir;

		if(player.equals("player")){
			currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
			ExpectedDir= Direction.valueOf(orientation);

			assertEquals(player, currentPlayer);
		}

		else{ //If player is an opponent
			currentPlayer = quoridor.getCurrentGame().getBlackPlayer();
			ExpectedDir= Direction.valueOf(orientation);

			assertEquals(player, currentPlayer);
		}

		boolean wallFound = false;

		for (Wall wall : currentPlayer.getWalls()){
			Direction wallDirection = wall.getMove().getWallDirection();
			int wallRow = wall.getMove().getTargetTile().getRow();
			int wallCol = wall.getMove().getTargetTile().getColumn();
			if( wallDirection == ExpectedDir && wallRow == row && wallCol == col)
				wallFound = true;
		}

		assertEquals(true ,wallFound);
	}

	/**
	 * @author Kevin Li
	 */
	@And("Both players shall have {int} in their stacks")
	public void bothPlayersShallHaveInTheirStacks(int remaining_walls) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		int blackWallsLeft = quoridor.getCurrentGame().getBlackPlayer().numberOfWalls();
		int whiteWallsLeft = quoridor.getCurrentGame().getWhitePlayer().numberOfWalls();

		assertEquals(remaining_walls,blackWallsLeft);
		assertEquals(remaining_walls,whiteWallsLeft);
	}

	/**
	 * @author Kevin Li
	 */
	@And("The position to load is invalid")
	public void thePositionToLoadIsInvalid() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gameposition = quoridor.getCurrentGame().getCurrentPosition();
		try {
			boolean positionIsNotValid = PositionController.validatePosition(gameposition);
		} catch(java.lang.UnsupportedOperationException e){
			throw new PendingException();
		}

		displayError = true;
	}

	/**
	 * @author Kevin Li
	 */
	@Then("The load shall return an error")
	public void theLoadShallReturnAnError() {
		assertEquals(true, displayError);
	}

	//Scenario Outline: Save Position

    /**
     * @author Kevin Li
     */
	@Given("No file {string} exists in the filesystem")
	public void noFileFilenameExistsInTheFilesystem(String filename) {
		//Can't potentially create file in filesystem
		fileInSystem = false;
	}

    /**
     * @author Kevin Li
     */
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithNameFilename(String filename) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gameposition = quoridor.getCurrentGame().getCurrentPosition();
		Player currentplayer = quoridor.getCurrentGame().getWhitePlayer();
		try {
			PositionController.saveGame(filename, currentplayer, gameposition);
		} catch(java.lang.UnsupportedOperationException e){
			throw new PendingException();
		}

	}

    /**
     * @author Kevin Li
     */
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithFilenameIsCreatedInTheFilesystem(String filename) {
		assertEquals(true, fileInSystem);
	}

	//Scenario Outline: Save position with existing file name
    /**
     * @author Kevin Li
     */
	@Given("File {string} exists in the filesystem")
	public void fileFilenameExistsInTheFilesystem(String filename) {
		//Can't potentially search file in filesystem
		fileInSystem = true;
	}


    /**
     * @author Kevin Li
     */
	@And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwriteExistingFile() {
		// GUI-related feature -- TODO for later
		//.Confirmed to overwrite to existing file
		fileChanged = true;
	}

    /**
     * @author Kevin Li
     */
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithFilenameIsUpdatedInTheFilesystem(String filename) {
		assertEquals(true, fileChanged);
	}

	//Scenario Outline: Save position cancelled due to existing file name

    /**
     * @author Kevin Li
     */
	@And("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwriteExistingFile() {
		// GUI-related feature -- TODO for later
		//.Do not overwrite Existing file
		fileChanged = false;
	}

    /**
     * @author Kevin Li
     */
	@Then("File {string} shall not be changed in the filesystem")
	public void fileFilenameIsNotChangedInTheFilesystem(String filename) {
		assertEquals(false, fileChanged);
	}




	/*scenario:Initiate a new game*/
	/**
	 * @author Fulin Huang
	 */
	@When("A new game is being initialized")
	public void aNewGameIsBeingInitialized() {
		try{
			StartNewGameController.initializeGame();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Fulin Huang
	 */
	@And("White player chooses a username")
	public void whitePlayerChoosesAUsername() {
		try {
			StartNewGameController.whitePlayerChoosesAUsername();

		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}

	}

	/**
	 * @author Fulin Huang
	 */
	@And("Black player chooses a username")
	public void blackPlayerChoosesAUsername() {
		try {
			StartNewGameController.blackPlayerChooseAUsername();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Fulin Huang
	 */
	@And("Total thinking time is set")
	public void totalThinkingTimeIsSet() {
		try {
			StartNewGameController.setTotalThinkingTime();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Fulin Huang
	 */
	@Then("The game shall become ready to start")
	public void theGameShallBecomeReadyToStart() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(GameStatus.ReadyToStart, quoridor.getCurrentGame().getGameStatus());

	}

	/*Scenario: Start clock */

	/**
	 * @author Fulin Huang
	 */
	@Given("The game is ready to start")
	public void theGameIsReadyToStart() {
		createAndReadyToStartGame();
	}

	/**
	 * @author Fulin Huang
	 */
	@When("I start the clock")
	public void iStartTheClock() {
		try {
			StartNewGameController.startTheClock();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Fulin Huang
	 */
	@Then("The game shall be running")
	public void theGameShallBeRunning() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(GameStatus.Running, quoridor.getCurrentGame().getGameStatus());
	}

	/**
	 * @author Fulin Huang
	 */
	@And("The board shall be initialized")
	public void theBoardShallBeInitialized() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(true, quoridor.getBoard().hasTiles());
	}

	/*set TotalThinkingTime*/


	/**
	 * @author Fulin Huang
	 */
	@Given("A new game is initializing")
	public void aNewGameIsInitializing() {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndInitializeGame(createUsersAndPlayers);
	}

	/**
	 * @author Fulin Huang
	 */
	@When("{int}:{int} is set as the thinking time")
	public void minSecIsSetAsTheThinkingTime(int minutes, int seconds) {
		try {
			StartNewGameController.setTotalThinkingTime(minutes, seconds);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}

	}

	/**
	 * @author Fulin Huang
	 */
	@Then("Both players shall have {int}:{int} remaining time left")
	public void bothPlayersShallHaveMinSecRemainingTimeLeft(int minutes, int seconds)  {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		long millis = minutes * 60 * 1000 + seconds * 1000;
		Date date = new Date();
		long currentMillis = date.getTime();
		Time time = new Time(millis+currentMillis);
		assertEquals(time, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime());
		assertEquals(time, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime());

	}


	// Move wall

	/**
	 * @author Tritin Truong
	 */
	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithDirAtPositionRowCol(String dir, Integer row, Integer col) {
		Direction direction = this.stringToDirection(dir);
		setupWallMoveCandidates(row, col, direction);
	}

	/**
	 * @author Tritin Truong
	 */
	@And("The wall candidate is not at the {string} edge of the board")
	public void theWallCandidateIsNotAtTheSideEdgeOfTheBoard(String side) {
        assertFalse(this.isWallMoveCandidateAtEdge(side));
	}

	/**
	 * @author Tritin Truong
	 */
	@When("I try to move the wall {string}")
	public void iTryToMoveTheWallSide(String side) {
		Game game = ModelQuery.getCurrentGame();
		WallMove move = game.getWallMoveCandidate();
		boolean outcome;
		try {
			outcome = WallController.shiftWall(side, move);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Tritin Truong
	 */
	@Then("The wall shall be moved over the board to position \\({int}, {int})")
	public void theWallShallBeMovedOverTheBoardToPositionNrowNcol(int nrow, int ncol) {
		//	TODO GUI step
	}

	/**
	 * @author Tritin Truong
	 */
	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithDirAtPositionNrowNcol(String direction, int nrow, int ncol) {
		Game game = ModelQuery.getCurrentGame();

		Direction dir = this.stringToDirection(direction);
		assertEquals(game.getWallMoveCandidate().getTargetTile().getRow(), nrow);
		assertEquals(game.getWallMoveCandidate().getTargetTile().getColumn(), ncol);
		assertEquals(game.getWallMoveCandidate().getWallDirection(), dir);
	}

	//	Invalid move

	/**
	 * @author Tritin Truong
	 */
	@Then("I should be notified that my move is illegal")
	public void iShouldBeNotifiedThatMyMoveIsIllegal() {
//		TODO GUI step
	}

	/**
	 * @author Tritin Truong
	 */
	@And("The wall candidate is at the {string} edge of the board")
	public void theWallCandidateIsAtTheSideEdgeOfTheBoard(String side) {
		boolean isAtEdge =this.isWallMoveCandidateAtEdge(side) ;
        assertTrue(isAtEdge);
	}

	// Drop wall

	/**
	 * @author Tritin Truong
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
	public void theWallMoveCandidateWithDirAtPositionRowColIsValid(String dir, int row, int col) {
		Direction direction = this.stringToDirection(dir);
		setupWallMoveCandidates(row, col, direction);
	}

	/**
	 * @author Tritin Truong
	 */
	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand() {
		WallMove move = ModelQuery.getWallMoveCandidate();
		Player player = ModelQuery.getWhitePlayer();
		try{
			WallController.dropWall(move, player);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Tritin Truong
	 */
	@Then("A wall move shall be registered with {string} at position \\({int}, {int})")
	public void aWallMoveIsRegisteredWithDirAtPositionRowCol(String direction, int row, int col) {
		Game game = ModelQuery.getCurrentGame();
		Direction dir = this.stringToDirection(direction);
		int move_size = game.getMoves().size();

//		Check if at least one move has been registered
		assertTrue( move_size > 0);
		Move move = game.getMoves().get(move_size-1);

//		Check if the most recent move was a wall move
		assert move instanceof WallMove;
		WallMove wall_move = (WallMove) move;

//		Verify that the wall move is the same as the one just played
		assertEquals(wall_move.getWallDirection(),dir);
		assertEquals(wall_move.getTargetTile().getColumn(), col);
		assertEquals(wall_move.getTargetTile().getRow(), row);
	}

	/**
	 * @author Tritin Truong
	 */
	@And("My move shall be completed")
	public void myMoveIsCompleted() {

		Game game = ModelQuery.getCurrentGame();

		// The wallmove candidate should be gone
		assertNull(game.getWallMoveCandidate());

		// White should have more walls on board
		assertEquals(game.getCurrentPosition().getWhiteWallsOnBoard().size(), 2);

		// White should have less walls in stock
        assertEquals(9, game.getCurrentPosition().getWhiteWallsInStock().size());

	}

	/**
	 * @author Tritin Truong
	 */
	@And("I shall not have a wall in my hand")
	public void iShallNotHaveAWallInMyHand() {
		// TODO GUI
	}

	// Invalid drop wall

	/**
	 * @author Tritin Truong
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
	public void theWallMoveCandidateWithDirAtPositionRowColIsInvalid(String dir, int row, int col) {
		Direction direction = this.stringToDirection(dir);
		setupWallMoveCandidates(row, col, direction);
	}

	/**
	 * @author Tritin Truong
	 */
	@Then("I shall be notified that my move is illegal")
	public void iShallBeNotifiedThatMyWallMoveIsIllegal() {
		// TODO GUI step
	}


	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
	}

	/**
	 * @author Tritin Truong
	 */
	@Then("No wall move shall be registered with {string} at position \\({int}, {int})")
	public void noWallMoveIsRegisteredWithDirAtPositionRowCol(String direction, int row, int col) {
		List moves = ModelQuery.getMoves();
		// Setup added no moves so there should still be no moves in the move list.
		assertEquals(moves.size(), 0);
	}

	/**
	 * @author Tritin Truong
	 */
	@And("It shall not be my turn to move")
	public void itIsNotMyTurnToMove() {
	    // operating under the assumption that is was white's turn to move
		Player player1 = ModelQuery.getBlackPlayer();
		Player playerToMove = ModelQuery.getPlayerToMove();
		assertEquals(playerToMove, player1);
	}

	/**
	 * @author Tritin Truong
	 */
	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
        // operating under the assumption that is was white's turn to move
        Player player1 = ModelQuery.getWhitePlayer();
        Player playerToMove = ModelQuery.getPlayerToMove();
        assertEquals(playerToMove, player1);
	}

	// Computer Control

	/**
	 * @author Tritin Truong
	 */
	@Given("It is not my turn to move")
	public void itIsNotMyTurn() {
		Player currentPlayer = ModelQuery.getBlackPlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}


	/**
	 * @author Tritin Truong
	 */
	@When("The computer computes a move")
	public void theComputerComputesAMove() {
		Player player = ModelQuery.getBlackPlayer();
		try{
			ComputerController.computeMove(player);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}


	/**
	 * @author Tritin Truong
	 */
	@Then("The move is registered")
	public void theMoveIsValid() {
		Game game = ModelQuery.getCurrentGame();
		Player computer = ModelQuery.getBlackPlayer();
		int move_size = game.getMoves().size();

//		Check if at least one move has been registered
		assertTrue( move_size > 0);
		Move move = game.getMoves().get(move_size-1);

//		Check if the most recent move was a wall move
		assertEquals(computer, move.getPlayer());
	}

//  Move hint

	/**
	 * @author Tritin Truong
	 */
	@When("I ask for a move suggestion")
	public void iAskForAMoveSuggestion() {
		Player human_player = ModelQuery.getWhitePlayer();
		try{
			ComputerController.computeMove(human_player);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}


	/**
	 * @author Tritin Truong
	 */
	@Then("I am notified of a possible move")
	public void iAmNotifiedOfAPossibleMove() {
		// TODO GUI step
	}

	//grab wall
	//scenario start wall placement
	/**
	 * @author Kate Ward
	 */
	@Given("I have more walls on stock")
	public void iHaveMoreWallsOnStock() {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		Player currentPlayer = game.getCurrentPosition().getPlayerToMove();
		//check which is current player and then add a wall to stock from board if none left
		if (currentPlayer.equals(game.getBlackPlayer())) {
			int numWalls = game.getCurrentPosition().getBlackWallsInStock().size();
			if (numWalls == 0) {
				Wall placedWall = (Wall) game.getCurrentPosition().getBlackWallsOnBoard();
				game.getCurrentPosition().addBlackWallsInStock(placedWall);
			}

		}
		else {
			int numWalls = game.getCurrentPosition().getWhiteWallsInStock().size();
			if (numWalls == 0) {
				Wall placedWall = (Wall) game.getCurrentPosition().getWhiteWallsOnBoard();
				game.getCurrentPosition().addWhiteWallsInStock(placedWall);
			}
		}

	}

	/**
	 * @author Kate Ward
	 */
	@When("I try to grab a wall from my stock")
	public void iTryToGrabAWallFromMyStock() {
		try {
			WallController.grabWall();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Kate Ward
	 */
	@Then("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		assertEquals(true, game.hasWallMoveCandidate());
	}

	/**
	 * @author Kate Ward, Tritin Truong
	 */
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		//GUI TODO later
		throw new PendingException();
	}

	/**
	 * @author Kate Ward
	 */
	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShouldDisappearFromMyStock() {
		//GUI
		throw new PendingException();
	}

	//scenario no more walls in stock
	/**
	 * @author Kate Ward
	 */
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallsOnStock() {
		Game game = QuoridorApplication.getQuoridor().getCurrentGame();
		Player currentPlayer = game.getCurrentPosition().getPlayerToMove();

		if (currentPlayer.equals(game.getBlackPlayer())) {
			for (Wall wall: game.getCurrentPosition().getBlackWallsInStock()){
				if (wall!=null) {
					game.getCurrentPosition().addBlackWallsOnBoard(wall);
				}
			}
		}
		else {
			for (Wall wall: game.getCurrentPosition().getWhiteWallsInStock()){
				if (wall!=null) {
					game.getCurrentPosition().addWhiteWallsOnBoard(wall);
				}
			}
		}

	}

	/**
	 * @author Kate Ward
	 */
	@Then("I shall be notified that I have no more walls")
	public void iShallBeNotifiedThatIHaveNoMoreWalls() {
		//GUI TODO later
		throw new PendingException();
	}

	/**
	 * @author Kate Ward
	 */
	@And("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() {
		//GUI TODO later
		throw new PendingException();
	}

	//rotate wall

	/**
	 * @author Kate Ward
	 */
	@When("I try to flip the wall")
	public void iTrytoFlipTheWall() {
		try {
			WallController.rotateWall();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	/**
	 * @author Kate Ward
	 */
	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardToNewDir() {
		//GUI TODO later
		throw new PendingException();
	}
	/**
	 * @author: Mark Zhu
	 * Validate Position feature
	 */
	private int row;
	private int col;
	private boolean valid;
	private boolean pawnPos; //true if validating position of pawn, false if wall
	private Direction dir;

	//@author: Mark Zhu
	@Given("A game position is supplied with pawn coordinate {int}:{int}")
	public void gamePositionSuppliedPawnCoordinates(int row, int col) {
		this.row = row;
		this.col = col;
		pawnPos=true;
	}

	//@author: Mark Zhu
	@When("Validation of the position is initiated")
	public void validationPositionInitiated() {
		if (pawnPos) {
			try {
				valid = ValidatePositionController.validatePawnPosition(row,col);
			} catch (UnsupportedOperationException e) {
				throw new PendingException();
			}
		} else {
			try {
				valid = ValidatePositionController.validateWallPosition(row,col,dir);
			} catch (UnsupportedOperationException e) {
				throw new PendingException();
			}
		}
	}

	//@author: Mark Zhu
	@Then("The position shall be {string}")
	public void returnPawnPositionValidity(String result) {
		if (valid)
		{
			assertEquals("ok",result);
		}
		else
		{
			assertEquals("error",result);
		}

	}

	//@author: Mark Zhu
	@Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
	public void gamePositionSuppliedWallCoordinates(int row, int col, String direct) {
		this.row = row;
		this.col = col;
		if (direct == "horizontal") {
			this.dir = Direction.Horizontal;
		}
		else if (direct == "vertical") {
			this.dir = Direction.Vertical;
		}
		pawnPos=false;
	}

	//@author: Mark Zhu
	@Then("The position shall be valid")
	public void positionValid() {
		assertEquals(valid,true);
	}

	//@author: Mark Zhu
	@Then("The position shall be invalid")
	public void positionInvalid() {
		assertEquals(valid,false);
	}

	//************************************
	//Switch Player
	//@author: Mark Zhu

	String originalPlayerColor;
	String nextPlayerColor;

	@Given("The player to move is {string}")
	public void thePlayerToMoveIs(String playerColor) {
		originalPlayerColor = playerColor;
	}

	//@author: Mark Zhu
	@And("The clock of {string} is running")
	public void currentClockRunning(String playerColor) {
		//TODO: GUI step
	}

	//@author: Mark Zhu
	@And("The clock of {string} is stopped")
	public void nextClockStopped(String playerColor) {
		//TODO: GUI step
	}

	//@author: Mark Zhu
	@When("Player {string} completes his move")
	public void playerMoveCompleted(String playerColor) {
		try {
			nextPlayerColor = SwitchPlayerController.SwitchActivePlayer(playerColor);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	//@author: Mark Zhu
	@Then("The user interface shall be showing it is {string} turn")
	public void displayWhoseTurn(String playerColor) {
		//TODO: GUI step
	}

	//@author: Mark Zhu
	@And("The clock of {string} shall be stopped")
	public void selfClockStop(String playerColor) {
		//TODO: GUI step
	}

	//@author: Mark Zhu
	@And("The clock of {string} shall be running")
	public void nextClockRun(String playerColor) {
		//TODO: GUI step
	}

	//@author: Mark Zhu
	@And("The next player to move shall be {string}")
	public void checkActivePlayer(String playerColor) {
		if (originalPlayerColor == "white") {
			assertEquals(nextPlayerColor,"black");
		} else {
			assertEquals(nextPlayerColor,"white");
		}
	}


	// Feature 4  Initialize Board

	/**
	 * @author Jason Lau
	 */
	@When("The initialization of the board is initiated")
	public void theInitializationOfTheBoardIsInitiated() {
		try {
			BoardController.initializeBoard();
		}
		catch(UnsupportedOperationException e){
			throw new PendingException();
		}
	}

	/**
	 * @author Jason Lau
	 */
	@Then("It shall be white player to move")
	public void itShallBeWhitePlayerToMove() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove(), quoridor.getCurrentGame().getWhitePlayer());
	}


	/**
	 * @author Jason Lau
	 */
	@And("White's pawn shall be in its initial position")
	public void whiteSPawnShallBeInItsInitialPosition() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		PlayerPosition whitePlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), quoridor.getBoard().getTile(36));

		assertEquals(whitePlayerPos, quoridor.getCurrentGame().getCurrentPosition().getWhitePosition());

	}

	/**
	 * @author Jason Lau
	 */
	@And("Black's pawn shall be in its initial position")
	public void blackSPawnShallBeInItsInitialPosition() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		PlayerPosition blackPlayerPos =  new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), quoridor.getBoard().getTile(44));

		assertEquals(blackPlayerPos, quoridor.getCurrentGame().getCurrentPosition().getBlackPosition());


	}

	/**
	 * @author Jason Lau
	 */
	@And("All of White's walls shall be in stock")
	public void allOfWhiteSWallsShallBeInStock() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();

		assertEquals(10,quoridor.getCurrentGame().getWhitePlayer().getWalls().size());

	}

	/**
	 * @author Jason Lau
	 */
	@And("All of Black's walls shall be in stock")
	public void allOfBlackSWallsShallBeInStock() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(10,quoridor.getCurrentGame().getBlackPlayer().getWalls().size());
	}

	/**
	 * @author Jason Lau
	 */
	@And("White's clock shall be counting down")
	public void whiteSClockShallBeCountingDown() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();

		// TODO GUI FEATURE
	}

	/**
	 * @author Jason Lau
	 */
	@And("It shall be shown that this is White's turn")
	public void itShallBeShownThatThisIsWhiteSTurn() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
	}

// Feature 2 Provide or select user name

	private Player playertoselect;

	/**
	 * @author Jason Lau
	 */
	@Given("Next player to set user name is {string}")
	public void nextPlayerToSetUserNameIs(String arg0) {


		Quoridor quoridor = QuoridorApplication.getQuoridor();

		if(arg0.equals("white")){

			playertoselect = quoridor.getCurrentGame().getWhitePlayer();
		}

		else if(arg0.equals("black")){

			playertoselect = quoridor.getCurrentGame().getBlackPlayer();
		}
	}

	/**
	 * @author Jason Lau
	 */
	@And("There is existing user {string}")
	public void thereIsExistingUser(String arg0) {

		User.hasWithName(arg0);
	}

	/**
	 * @author Jason Lau
	 */
	@When("The player selects existing {string}")
	public void thePlayerSelectsExisting(String arg0) {
		try {

			Quoridor quoridor = QuoridorApplication.getQuoridor();
			UserController.selectExistingUsername(playertoselect, arg0);

		}catch(UnsupportedOperationException e){
			throw new PendingException();

		}
	}

	/**
	 * @author Jason Lau
	 */
	@Then("The name of player {string} in the new game shall be {string}")
	public void theNameOfPlayerInTheNewGameShallBe(String arg0, String arg1) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();

		if( arg0.equals("white")){

			assertEquals(arg1, quoridor.getCurrentGame().getWhitePlayer().getUser().getName());
		}

		else if( arg0.equals("black")){

			assertEquals(arg1, quoridor.getCurrentGame().getBlackPlayer().getUser().getName());
		}


	}

	/**
	 * @author Jason Lau
	 */
	@And("There is no existing user {string}")
	public void thereIsNoExistingUser(String arg0) {

		assertEquals(false, User.hasWithName(arg0));
	}

	/**
	 * @author Jason Lau
	 */
	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUserName(String arg0) {

		try{
			UserController.newUsername(playertoselect,arg0);

		}catch(UnsupportedOperationException e){

			throw new PendingException();
		}
	}

	/**
	 * @author Jason Lau
	 */
	@Then("The player shall be warned that {string} already exists")
	public void thePlayerShallBeWarnedThatAlreadyExists(String arg0) {

		/*Quoridor quoridor = QuoridorApplication.getQuoridor();
		username = User.hasWithName(arg0);

		if(username) {

			System.out.println("The username " + arg0 + " already exists");
		}
        */

		//TODO GUI FEATURE

	}

	/**
	 * @author Jason Lau
	 */
	@And("Next player to set user name shall be {string}")
	public void nextPlayerToSetUserNameShallBe(String arg0) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();

		if(arg0.equals("white")) {

			assertEquals(quoridor.getCurrentGame().getWhitePlayer(), playertoselect.getNextPlayer());

		}

		else if(arg0.equals("black")){

			assertEquals(quoridor.getCurrentGame().getBlackPlayer(), playertoselect.getNextPlayer());
		}

	}


	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Avoid null pointer for step definitions that are not yet implemented.
		if (quoridor != null) {
			quoridor.delete();
			quoridor = null;
		}
		for (int i = 0; i < 20; i++) {
			Wall wall = Wall.getWithId(i);
			if(wall != null) {
				wall.delete();
			}
		}
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int thinkingTime = 180;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 *
		 */
		//@formatter:on
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}

		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);

		return playersList;
	}

	private void createAndInitializeGame(ArrayList<Player> players ) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);
	}

	private void createAndReadyToStartGame() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser("userWhite");
		User user2 = quoridor.addUser("userBlack");
		int totalThinkingTime = 180;
		Player player1 = new Player(new Time(totalThinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(totalThinkingTime), user2, 1, Direction.Horizontal);
		Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, player1, player2, quoridor);
	}


	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);

		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
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

	private boolean isWallMoveCandidateAtEdge(String side){
	    Game game = ModelQuery.getCurrentGame();
        switch(side){
            case "left":{
            	return game.getWallMoveCandidate().getTargetTile().getColumn() == 1;
            }
            case "right":{
                return game.getWallMoveCandidate().getTargetTile().getColumn() == 8;
            }
            case "up":{
                return game.getWallMoveCandidate().getTargetTile().getRow() == 1;
            }
            case "down":{
                return game.getWallMoveCandidate().getTargetTile().getRow() == 8;
            }
        }
        return false;
    }

	private void setupWallMoveCandidates(int row, int col, Direction direction) {
		Player player1 = ModelQuery.getWhitePlayer();
		Board board = ModelQuery.getBoard();
		Game game = ModelQuery.getCurrentGame();
		Wall wall = player1.getWall(3);

		WallMove move = new WallMove(0, 1, player1, board.getTile((row - 1) * 9 + col - 1), game, direction, wall);
		game.setWallMoveCandidate(move);
	}


}
