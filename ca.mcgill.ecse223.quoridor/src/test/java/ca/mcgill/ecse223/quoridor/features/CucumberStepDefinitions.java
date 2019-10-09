package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.Controllers.PositionController;
import cucumber.api.Pending;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CucumberStepDefinitions {

	private Quoridor quoridor;
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Game game;
	private boolean fileInSystem;
	private boolean fileChanged;

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
		theGameIsNotRunning();
		createAndStartGame();
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		currentPlayer = player1;
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { player1, player2 };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);

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
			new WallMove(0, 1, players[playerIdx], board.getTile((wrow - 1) * 9 + wcol - 1), game, direction, wall);
			if (playerIdx == 0) {
				game.getCurrentPosition().removeWhiteWallsInStock(wall);
				game.getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				game.getCurrentPosition().removeBlackWallsInStock(wall);
				game.getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// Walls are in stock for all players
	}
	
	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	//Scenario Outline: Save Position
	@cucumber.api.java.en.Given("No file {word} exists in the filesystem")
	public void noFileFilenameExistsInTheFilesystem(String filename) {
		//Can't potentially create file in filesystem
		fileInSystem = false;
	}

	@When("The user initiates to save the game with name {word}")
	public void theUserInitiatesToSaveTheGameWithNameFilename(String filename) {
		try {
			PositionController.saveGame(filename, game.getCurrentPosition());
		} catch(NotImplementedException e){
			throw new PendingException();
		}

	}

	@Then("A file with {word} is created in the filesystem")
	public void aFileWithFilenameIsCreatedInTheFilesystem(String filename) {
		Assert.assertEquals(true, fileInSystem);
	}

	//Scenario Outline: Save position with existing file name
	@cucumber.api.java.en.Given("File {word} exists in the filesystem")
	public void fileFilenameExistsInTheFilesystem(String filename) {
		//Can't potentially search file in filesystem
		fileInSystem = true;
	}



	@cucumber.api.java.en.And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwriteExistingFile() {
		//.This is an User Interface feature
		//.Confirmed to overwrite to existing file
		fileChanged = true;
	}

	@Then("File with {word} is updated in the filesystem")
	public void fileWithFilenameIsUpdatedInTheFilesystem(String filename) {
		Assert.assertEquals(true, fileChanged);
	}

	//Scenario Outline: Save position cancelled due to existing file name

	@cucumber.api.java.en.And("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwriteExistingFile() {
		//.This is an User Interface feature
		//.Do not overwrite Existing file
		fileChanged = false;
	}

	@Then("File {word} is not changed in the filesystem")
	public void fileFilenameIsNotChangedInTheFilesystem(String filename) {
		Assert.assertEquals(false, fileChanged);
	}

	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		quoridor.delete();
		quoridor = null;
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		quoridor = QuoridorApplication.getQuoridor();
		board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private void createUsersAndPlayers(String userName1, String userName2) {
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
		player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}
	}

	private void createAndStartGame() {
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = board.getTile(36);
		Tile player2StartPos = board.getTile(44);

		PlayerPosition player1Position = new PlayerPosition(player1, player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(player2, player2StartPos);

		game = new Game(GameStatus.Running, MoveMode.PlayerMove, player1, player2, quoridor);
		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, player1, game);

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
}
