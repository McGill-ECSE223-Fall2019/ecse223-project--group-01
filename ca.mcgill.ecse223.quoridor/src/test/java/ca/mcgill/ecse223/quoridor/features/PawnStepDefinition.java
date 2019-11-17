package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.*;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import cucumber.api.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class PawnStepDefinition {


    @When("Player {string} initiates to move {string}")
    public void iInitiateToLoadASavedGameFilename(String playerColor, String side) {
        try{
            PawnController.movePawn(side);
        }catch (NotImplementedException e){
            throw new PendingException();
        }

    }
    
    //Jump Pawn
    //implemented already in CucumberStepDefinition
	@Given("^The game is running$")
	public void theGameIsRunning() {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndStartGame(createUsersAndPlayers);
	}
	
	//scenario outline: Jump over opponent
	/**
	 * @author Kate Ward
	 */
	@Given("The player to move is {string}")
	public void thePlayerToMoveIs(Player currentPlayer) {
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}
	
	/**
	 * @author Kate Ward
	 */
	@And("The player is located at {string}:{string}")
	public void thePlayerIsLocatedAtRowCol(int row, int col) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player player = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		//player is white
		if (player.equals(quoridor.getCurrentGame().getWhitePlayer())) {
			Tile tile = new Tile(row, col, quoridor.getBoard());
			quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
		}
		//player is black
		else {
			Tile tile = new Tile(row, col, quoridor.getBoard());
			quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
		}
	}
	
	/**
	 * @author Kate Ward
	 */
	@And("The opponent is located at {string}:{string}")
	public void theOpponentIsLocatedAtRowCol(int row, int col) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player player = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		//player is not white (opponent is white)
		if (!player.equals(quoridor.getCurrentGame().getWhitePlayer())) {
			Tile tile = new Tile(row, col, quoridor.getBoard());
			quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(tile);
		}
		//player is not black (opponent is black)
		else {
			Tile tile = new Tile(row, col, quoridor.getBoard());
			quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
		}
	}
	
	/**
	 * @author Kate Ward
	 */
	@And("There are no {string} walls {string} from the player nearby")
	public void thereAreNoDirWallsSideFromThePlayerNearby(Direction dir, String side) {
		//do nothing
	}
	
	/**
	 * @author Kate Ward
	 */
	@When("Player {string} initiates to move {string}")
	public void playerInitiatesToMoveSide(Player player, String side) {
		try {
			PawnController.jumpPawn(player, side);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}
	
	/**
	 * @author Kate Ward
	 */
	@Then("The move {string} shall be {string}")
	public void theMoveSideShallBeStatus(String side, String status) {
		
	}
	
	/**
	 * @author Kate Ward
	 */
	@And("Player's new position shall be {string}:{string}")
	public void playersNewPositionShallBeRowCol(int row, int col) {
		Player player = ModelQuery.getPlayerToMove();
		Tile tile;
		//player is white
		if (player.equals(ModelQuery.getWhitePlayer())) {
			tile = ModelQuery.getCurrentPosition().getWhitePosition().getTile();
		}
		//player is black
		else {
			tile = ModelQuery.getCurrentPosition().getBlackPosition().getTile();
		}
		assertEquals(tile.getRow(), row);
		assertEquals(tile.getColumn(),col);
	}
	
	/**
	 * @author Kate Ward
	 */
	@And("The next player to move shall become {string}")
	public void theNextPlayerToMoveShallBecome(Player nextPlayer) {
		assertEquals(nextPlayer, ModelQuery.getCurrentPosition().getPlayerToMove().getNextPlayer());
	}
	
	//scenario outline: Jump of player blocked by wall
	/**
	 * @author Kate Ward
	 */
	@And("There is a {string} wall at {string}:{string}")
	public void thereIsAWallAtRowCol(Direction dir, int row, int col) {
		//place a wall
		Player player = ModelQuery.getWhitePlayer();
		Board board = ModelQuery.getBoard();
		Game game = ModelQuery.getCurrentGame();
		Wall wall = player.getWall(1);
		WallMove move = new WallMove(0,1,player,board.getTile((row-1)*9+col-1), game, dir, wall);
		game.setWallMoveCandidate(move);
		ModelQuery.getCurrentPosition().addWhiteWallsOnBoard(move.getWallPlaced());
	}
	
	//scenario outline: Diagonal jumps of a player near wall. no new methods
	//scenario outline: Diagonal jump over opponent at edge of the board. no new methods
	
	
	//helper methods from CucumberStepDefinitions
	// ***********************************************
	// Extracted helper methods
	// ***********************************************

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
			for (int j = 1; j <= 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}

		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);
		player1.setNextPlayer(player2);
		player2.setNextPlayer(player1);

		return playersList;
	}
	
	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);

		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
		game.setWhitePlayer(players.get(0));
		game.setBlackPlayer(players.get(1));

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 1; j <= 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 1; j <= 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
		game.getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
	}
	
	
	
}
