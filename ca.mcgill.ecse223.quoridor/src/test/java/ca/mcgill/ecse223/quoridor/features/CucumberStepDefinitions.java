package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.ComputerController;
import ca.mcgill.ecse223.quoridor.controllers.WallController;
import ca.mcgill.ecse223.quoridor.controllers.StartNewGameController;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import cucumber.api.PendingException;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.Assert.*;

public class CucumberStepDefinitions {

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
	 */



	/*scenario:Initiate a new game*/
	@When("A new game is being initialized")
	public void aNewGameIsBeingInitialized() {
		try{
			StartNewGameController.initializeGame();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@And("White player chooses a username")
	public void whitePlayerChoosesAUsername() {
		try {
			StartNewGameController.whitePlayerChoosesAUsername();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}

	}

	@And("Black player chooses a username")
	public void blackPlayerChoosesAUsername() {
		try {
			StartNewGameController.blackPlayerChooseAUsername();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@And("Total thinking time is set")
	public void totalThinkingTimeIsSet() {
		try {
			StartNewGameController.setTotalThinkingTime();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@Then("The game shall become ready to start")
	public void theGameShallBecomeReadyToStart() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(GameStatus.ReadyToStart, quoridor.getCurrentGame().getGameStatus());

	}

	/*Scenario: Start clock */
	@Given("The game is ready to start")
	public void theGameIsReadyToStart() {
		createAndReadyToStartGame();
	}

	@When("I start the clock")
	public void iStartTheClock() {
		try {
			StartNewGameController.startTheClock();
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@Then("The game shall be running")
	public void theGameShallBeRunning() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(GameStatus.Running, quoridor.getCurrentGame().getGameStatus());
	}

	@And("The board shall be initialized")
	public void theBoardShallBeInitialized() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(true, quoridor.getBoard().hasTiles());
	}

	/*set TotalThinkingTime*/
	@Given("A new game is initializing")
	public void aNewGameIsInitializing() {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndInitializeGame(createUsersAndPlayers);
	}

	@When("{int}:{int} is set as the thinking time")
	public void minSecIsSetAsTheThinkingTime(int minutes, int seconds) {
		try {
			StartNewGameController.setTotalThinkingTime(minutes, seconds);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}

	}

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
	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithDirAtPositionRowCol(String dir, Integer row, Integer col) {
		Direction direction = this.stringToDirection(dir);
		setupWallMoveCandidates(row, col, direction);
	}

	@And("The wall candidate is not at the {string} edge of the board")
	public void theWallCandidateIsNotAtTheSideEdgeOfTheBoard(String side) {
        assertFalse(this.isWallMoveCandidateAtEdge(side));
	}

	@When("I try to move the wall {string}")
	public void iTryToMoveTheWallSide(String side) {
		Game game = ModelQuery.getCurrentGame();
		WallMove move = game.getWallMoveCandidate();
		try {
			WallController.shiftWall(side, move);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@Then("The wall shall be moved over the board to position \\({int}, {int})")
	public void theWallShallBeMovedOverTheBoardToPositionNrowNcol(int nrow, int ncol) {
		//	TODO GUI step
	}

	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithDirAtPositionNrowNcol(String direction, int nrow, int ncol) {
		Game game = ModelQuery.getCurrentGame();

		Direction dir = this.stringToDirection(direction);

		assertEquals(game.getWallMoveCandidate().getTargetTile().getRow(), nrow);
		assertEquals(game.getWallMoveCandidate().getTargetTile().getColumn(), ncol);
		assertEquals(game.getWallMoveCandidate().getWallDirection(), dir);
	}

	//	Invalid move
	@Then("I should be notified that my move is illegal")
	public void iShouldBeNotifiedThatMyMoveIsIllegal() {
//		TODO GUI step
	}

	@And("The wall candidate is at the {string} edge of the board")
	public void theWallCandidateIsAtTheSideEdgeOfTheBoard(String side) {
		boolean isAtEdge =this.isWallMoveCandidateAtEdge(side) ;
        assertTrue(isAtEdge);
	}

	// Drop wall

	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
	public void theWallMoveCandidateWithDirAtPositionRowColIsValid(String dir, int row, int col) {
		Direction direction = this.stringToDirection(dir);
		setupWallMoveCandidates(row, col, direction);
	}

	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand() {
		WallMove move = ModelQuery.getWallMoveCandidate();
		try{
			WallController.dropWall(move);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

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

	@And("My move shall be completed")
	public void myMoveIsCompleted() {

		Game game = ModelQuery.getCurrentGame();

		// The wallmove candidate should be gone
		assertNull(game.getWallMoveCandidate());

		// White should have more walls on board
		assertEquals(game.getCurrentPosition().getWhiteWallsOnBoard().size(), 2);

		// White should have less walls in stock
        assertEquals(game.getCurrentPosition().getWhiteWallsInStock().size(), 8);
        assertEquals(game.getWhitePlayer().getWalls().size(), 8);

	}

	@And("I shall not have a wall in my hand")
	public void iShallNotHaveAWallInMyHand() {
		// TODO GUI
	}

	// Invalid drop wall

	@Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
	public void theWallMoveCandidateWithDirAtPositionRowColIsInvalid(String dir, int row, int col) {
		Direction direction = this.stringToDirection(dir);
		setupWallMoveCandidates(row, col, direction);
	}

	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		// TODO GUI step
	}

	@Then("No wall move shall be registered with {string} at position \\({int}, {int})")
	public void noWallMoveIsRegisteredWithDirAtPositionRowCol(String direction, int row, int col) {
		List moves = ModelQuery.getMoves();
		// Setup added no moves so there should still be no moves in the move list.
		assertEquals(moves.size(), 0);
	}

	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		// TODO GUI
	}

	@And("It shall not be my turn to move")
	public void itIsNotMyTurnToMove() {
	    // operating under the assumption that is was white's turn to move
		Player player1 = ModelQuery.getBlackPlayer();
		Player playerToMove = ModelQuery.getPlayerToMove();
		assertEquals(playerToMove, player1);
	}

	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
        // operating under the assumption that is was white's turn to move
        Player player1 = ModelQuery.getWhitePlayer();
        Player playerToMove = ModelQuery.getPlayerToMove();
        assertEquals(playerToMove, player1);
	}

	// Computer Control
	@Given("It is not my turn to move")
	public void itIsNotMyTurn() {
		Player currentPlayer = ModelQuery.getBlackPlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@When("The computer computes a move")
	public void theComputerComputesAMove() {
		Player player = ModelQuery.getBlackPlayer();
		try{
			ComputerController.computeMove(player);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

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
	@When("I ask for a move suggestion")
	public void iAskForAMoveSuggestion() {
		Player human_player = ModelQuery.getWhitePlayer();
		try{
			ComputerController.computeMove(human_player);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@Then("I am notified of a possible move")
	public void iAmNotifiedOfAPossibleMove() {
		// TODO GUI step
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
