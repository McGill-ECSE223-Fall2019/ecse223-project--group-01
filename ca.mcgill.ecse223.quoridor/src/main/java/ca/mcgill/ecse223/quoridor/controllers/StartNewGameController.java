package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import com.sun.tools.internal.xjc.model.Model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartNewGameController {

    private static Game game;
    private static boolean whitePlayerChooseName = false;
    private static boolean blackPlayerChooseName = false;
    private static boolean thinkingTimeIsSet = false;

    public StartNewGameController(){};

    /**
     * @Author Fulin Huang
     *
     * Initialize game and set game status to be initializing
     */
    public static void initializeGame()  {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        game = new Game(Game.GameStatus.Initializing, Game.MoveMode.PlayerMove, quoridor);
    }

    /**
     * @Author Fulin Huang
     *
     * White player chooses a username by either creating a new name or by choosing
     * from a existing name
     *
     * @param username name of the white user
     */
    public static void whitePlayerChoosesAUsername(String username){

        User white_user = null;  //create a user before create a player
        if(usernameExists(username)){
            white_user = UserController.selectExistingUsername(username);
            whitePlayerChooseName = true;
        }
        else{
            white_user = UserController.newUsername(username);
            whitePlayerChooseName = true;
        }
        Player player = new Player(new Time(90), white_user, 9, Direction.Horizontal);
        ModelQuery.getCurrentGame().setWhitePlayer(player); //set White player
        player.setUser(white_user);
        isReadyToStart(); //check if white and black player chose name and if total thinking time is set
    }

    /**
     * @Author Fulin Huang
     *
     * Black player chooses a username by either choosing creating a new game or
     * by choosing from a existed name
     *
     * @param username name of the black user
     */
    public static void blackPlayerChooseAUsername(String username) {
        User black_user = null;
        if (usernameExists(username)) {
            black_user = UserController.selectExistingUsername(username);
            blackPlayerChooseName = true;
        } else {
            black_user = new UserController(username);
            blackPlayerChooseName = true;
        }
        Player player = new Player(new Time(90), black_user, 1, Direction.Vertical);
        ModelQuery.getCurrentGame().setBlackPlayer(player);
        player.setUser(black_user);
        isReadyToStart();  //check if white and black player chose name and if total thinking time is set
    }

    /**
     * @Author Fulin Huang
     *
     * Attempts to set total thinking time for each player to ensure
     * that a game does not last forever
     *
     * @param minutes how much minutes that the player want to set
     * @param seconds how much seconds that the player want to set
     *
     */
    public static void setTotalThinkingTime (int minutes, int seconds) {
        //total thinking time is able to set only if players are existed
        if (whitePlayerChooseName && blackPlayerChooseName) {
          Player whitePlayer = ModelQuery.getWhitePlayer();
          Player blackPlayer = ModelQuery.getBlackPlayer();

          Time totalThinkingTime = setThinkingTime(minutes, seconds);   //set total thinking time
          whitePlayer.setRemainingTime(totalThinkingTime);  //set total thinking time for the white player
          blackPlayer.setRemainingTime(totalThinkingTime);  //set total thinking time for the black player
        }

        isReadyToStart();
    }

    /**
     * @Author Fulin Huang
     *
     * Start the clock once the game is "ReadyToStart"
     * then set the game status to be "Running"
     *
     */
    public static void startTheClock() {
        BoardController.initializeBoard();
        ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.Running);
        //HOW TO START THE CLOCK??
        // TODO call board
        // TODO set game status to running
        // TODO start the clock

    }

    /**
     * @Author Fulin Huang
     *
     * Set total thinking time to specific minutes and seconds
     *
     * @param minutes how much minutes that the user want to set
     * @param seconds how much seconds that the usr want to set
     */
    public static Time setThinkingTime (int minutes, int seconds) {
        long millis = minutes * 60 * 1000 + seconds * 1000;
        Date date = new Date();
        long currentMillis = date.getTime();
        Time totalThinkingTime = new Time(millis+currentMillis);

        return totalThinkingTime;
    }

    /**
     * @Author Fulin Huang
     *
     * This method checks if white player choose name, black player chooose name,
     * and total thinking time is set.
     * If they all set, then the game state will change to "ReadyToStart"
     *
     */
    private static void isReadyToStart(){
        if(whitePlayerChooseName && blackPlayerChooseName & thinkingTimeIsSet){
            ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.ReadyToStart);
        }
    }

    /**
     * @Author Fulin Huang
     * This method checks if the username exist
     *
     * @param username name of a player
     * @return a boolean to indicate if the username exist
     *
     */
    private static boolean usernameExists(String username) {
        boolean nameExist = false;
        List<User> users = QuoridorApplication.getQuoridor().getUsers();
        for (User user: users) {
            if (user.getName().equals(username)) {
                nameExist = true;
            }
        }
        return nameExist;
    }

}
