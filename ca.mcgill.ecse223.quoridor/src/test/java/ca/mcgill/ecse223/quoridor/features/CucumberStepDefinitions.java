package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.WallController;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import cucumber.api.PendingException;
import io.cucumber.java.After;

import io.cucumber.java.en.*;

import static junit.framework.TestCase.assertEquals;

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

	@And("I have a wall in my hand over the board")
	public void iHaveAWallInMyHand() {
//		TODO GUI step
	}

	@And("It is not my turn to move")
	public void itIsNotMyTurnToMove() {
		assertEquals(game.getCurrentPosition().getPlayerToMove(), player2);
	}

	// MOVE WALL FEATURE

	@Given("A wall move candidate exists with {word} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithDirAtPositionRowCol(String dir, Integer row, Integer col) {
		Direction direction = this.stringToDirection(dir);
		Wall wall = player1.getWall(0);
		WallMove move = new WallMove(0, 1, player1, board.getTile((row - 1) * 9 + col - 1), game, direction, wall);
		game.setWallMoveCandidate(move);
	}

	@And("The wall candidate is not at the {word} edge of the board")
	public void theWallCandidateIsNotAtTheSideEdgeOfTheBoard(String side) {
		switch(side){
			case "left":{
				assert game.getWallMoveCandidate().getTargetTile().getColumn() != 1;
			}
			case "right":{
				assert game.getWallMoveCandidate().getTargetTile().getColumn() != 9;
			}
			case "up":{
				assert game.getWallMoveCandidate().getTargetTile().getRow() != 9;
			}
			case "down":{
				assert game.getWallMoveCandidate().getTargetTile().getRow() != 1;
			}
		}
	}

	@When("I try to move the wall {word}")
	public void iTryToMoveTheWallSide(String side) {
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

	@And("A wall move candidate shall exist with {word} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithDirAtPositionNrowNcol(String direction, int nrow, int ncol) {
		Direction dir = this.stringToDirection(direction);

		assertEquals(game.getWallMoveCandidate().getTargetTile().getRow(), nrow);
		assertEquals(game.getWallMoveCandidate().getTargetTile().getColumn(), ncol);
		assertEquals(game.getWallMoveCandidate().getWallDirection(), dir);
	}


//	Invalid move

	@And("The wall candidate is at the {word} edge of the board")
	public void theWallCandidateIsAtTheSideEdgeOfTheBoard(String side) {
		switch(side){
			case "left":{
				assert game.getWallMoveCandidate().getTargetTile().getColumn() == 1;
			}
			case "right":{
				assert game.getWallMoveCandidate().getTargetTile().getColumn() == 9;
			}
			case "up":{
				assert game.getWallMoveCandidate().getTargetTile().getRow() == 9;
			}
			case "down":{
				assert game.getWallMoveCandidate().getTargetTile().getRow() == 1;
			}
		}
	}

	@Then("I should be notified that my move is illegal")
	public void iShouldBeNotifiedThatMyMoveIsIllegal() {
//		TODO GUI step
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

	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand() {
		// TODO controller
	}

	@But("A wall move is registered with {word} at position \\({int}, {int})")
	public void aWallMoveIsRegisteredWithDirAtPositionRowCol(String direction, int row, int col) {
		Direction dir = this.stringToDirection(direction);
		int move_size = game.getMoves().size();
		Move move = game.getMoves().get(move_size);

		assert move instanceof WallMove;
		WallMove wall_move = (WallMove) move;

		assertEquals(wall_move.getWallDirection(),dir);
		assertEquals(wall_move.getTargetTile().getColumn(), col);
		assertEquals(wall_move.getTargetTile().getRow(), row);
	}

	@And("My move is completed")
	public void myMoveIsCompleted() {
		assert game.getWallMoveCandidate() == null;
		assertEquals(game.getCurrentPosition().getWhiteWallsOnBoard().size(), 2);
	}

	@Given("The wall move candidate with {word} at position \\({int}, {int}) is valid")
	public void theWallMoveCandidateWithDirAtPositionRowColIsValid(String dir, int row, int col) {
		Direction direction = this.stringToDirection(dir);
		Wall wall = player1.getWall(3);
		WallMove move = new WallMove(0, 1, player1, board.getTile((row - 1) * 9 + col - 1), game, direction, wall);
		game.setWallMoveCandidate(move);
	}

	@Given("The wall move candidate with {word} at position \\({int}, {int}) is invalid")
	public void theWallMoveCandidateWithDirAtPositionRowColIsInvalid(String dir, int row, int col) {
		Direction direction = this.stringToDirection(dir);
		Wall wall = player1.getWall(3);
		WallMove move = new WallMove(0, 1, player1, board.getTile((row - 1) * 9 + col - 1), game, direction, wall);
		game.setWallMoveCandidate(move);
	}

	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		// TODO GUI step
	}

	@But("No wall move is registered with <dir> at position \\(<row>, <col>)")
	public void noWallMoveIsRegisteredWithDirAtPositionRowCol() {
	}

	// DROP WALL FEATURE


}
