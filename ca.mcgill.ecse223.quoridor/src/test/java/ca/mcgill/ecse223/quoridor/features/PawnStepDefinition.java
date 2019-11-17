package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controllers.ModelQuery;
import ca.mcgill.ecse223.quoridor.controllers.PawnController;
import ca.mcgill.ecse223.quoridor.model.*;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.util.PendingException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Player currentPlayer = getPlayerByColor(player);
        Player blackPlayer = ModelQuery.getBlackPlayer();
        Player whitePlayer = ModelQuery.getWhitePlayer();

        if(currentPlayer.equals(blackPlayer))
            setTurn(blackPlayer);

        else if(currentPlayer.equals(whitePlayer))
            setTurn(whitePlayer);

        else{
            //4 player test
        }
    }

    @And("The player is located at {int}:{int}")
    public void thePlayerIsLocatedAt(int row, int col){
        Player currentPlayer = ModelQuery.getCurrentPosition().getPlayerToMove();

        int tilePosition = (row-1)*9 + (col-1);
        Tile targetPosition = ModelQuery.getBoard().getTile(tilePosition);
        PlayerPosition position;
        if(currentPlayer.equals(ModelQuery.getBlackPlayer()))

            position = ModelQuery.getCurrentPosition().getBlackPosition();
        else
            position = ModelQuery.getCurrentPosition().getWhitePosition();

        position.setPlayer(currentPlayer);
        position.setTile(targetPosition);
    }

    @And("There are no {string} walls {string} from the player")
    public void thereAreNoWallsFromThePlayer(String dir, String side){
        GamePosition currentGamePosition = ModelQuery.getCurrentPosition();
        Direction direction = stringToDirection(dir);


    }

    @And("The opponent is not {string} from the player")
    public void theOpponentIsNotFromThePlayer(String side) throws Exception {
        Game currentGame = ModelQuery.getCurrentGame();
        Player currentPlayer = ModelQuery.getPlayerToMove();
        Tile playerTile;
        int playerTileIndex;
        Player opponent = null;
        int row = 0;
        int col = 0;

        if(currentPlayer == ModelQuery.getBlackPlayer()){
            opponent = currentGame.getWhitePlayer();
            playerTile = ModelQuery.getCurrentPosition().getBlackPosition().getTile();
            col = playerTile.getColumn();
            row = playerTile.getRow();
        }
        else if (currentPlayer == ModelQuery.getWhitePlayer()){
            opponent = currentGame.getBlackPlayer();
            playerTile = ModelQuery.getCurrentPosition().getWhitePosition().getTile();
            col = playerTile.getColumn();
            row = playerTile.getRow();
        }
        else{
            //4 player
        }
        playerTileIndex = (row-1)*9+col-1;

        if(!setOpponentSide(side, playerTileIndex,currentPlayer)){
            throw new Exception("Failed to set Opponent side");
        }
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
    public void playerNewPositionShallBe(int row, int col){
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

    @And("The next player to move shall become {string}")
    public void theNextPlayerToMoveShallBecome(String nextPlayer){
        assertEquals(nextPlayer, ModelQuery.getCurrentPosition().getPlayerToMove().getNextPlayer());
    }

    //This section is move of player blocked by wall

    @And("There is a {string} wall at {int}:{int}")
    public void thereIsAWallAt(Direction dir, int row, int col){
        //place a wall
        Player player = ModelQuery.getWhitePlayer();
        Board board = ModelQuery.getBoard();
        Game game = ModelQuery.getCurrentGame();
        Wall wall = player.getWall(1);
        WallMove move = new WallMove(0,1,player,board.getTile((row-1)*9+col-1), game, dir, wall);
        game.setWallMoveCandidate(move);
        ModelQuery.getCurrentPosition().addWhiteWallsOnBoard(move.getWallPlaced());
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

        Game game = new Game(Game.GameStatus.Running, Game.MoveMode.PlayerMove, quoridor);
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

    private Player getPlayerByColor(String color){
        Player player = null;
        String color_lower = color.toLowerCase();
        if (color_lower.equals("black")){
            player = ModelQuery.getBlackPlayer();
        }

        else if (color_lower.equals("white")){
            player = ModelQuery.getWhitePlayer();
        }

        else{
            //Implement for 4 players
        }

        return player;
    }

    private boolean setTurn(Player player){
        if(player.equals(ModelQuery.getBlackPlayer())){
            return ModelQuery.getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getBlackPlayer());
        }
        else if(player.equals(ModelQuery.getWhitePlayer())){
            return ModelQuery.getCurrentGame().getCurrentPosition().setPlayerToMove(ModelQuery.getWhitePlayer());
        }
        else{
            //4 player
        }
        return false;
    }

    private boolean setOpponentSide(String side, int playerTileIndx, Player player) {
        Tile tile = null;
        boolean execution = false;
        if (player.equals(ModelQuery.getBlackPlayer())) {
            switch (side) {
                case "left":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx + 1);
                    execution = ModelQuery.getCurrentPosition().getBlackPosition().setTile(tile);

                case "right":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx - 1);
                    execution = ModelQuery.getCurrentPosition().getBlackPosition().setTile(tile);

                case "up":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx + 1);
                    execution = ModelQuery.getCurrentPosition().getBlackPosition().setTile(tile);

                case "down":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx - 1);
                    execution = ModelQuery.getCurrentPosition().getBlackPosition().setTile(tile);
            }
        }
        else {
            switch (side) {
                case "left":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx + 1);
                    execution = ModelQuery.getCurrentPosition().getWhitePosition().setTile(tile);

                case "right":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx - 1);
                    execution = ModelQuery.getCurrentPosition().getWhitePosition().setTile(tile);

                case "up":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx + 1);
                    execution = ModelQuery.getCurrentPosition().getWhitePosition().setTile(tile);

                case "down":
                    tile = ModelQuery.getBoard().getTile(playerTileIndx - 1);
                    execution = ModelQuery.getCurrentPosition().getWhitePosition().setTile(tile);
            }
        }
        return execution;
    }

}
