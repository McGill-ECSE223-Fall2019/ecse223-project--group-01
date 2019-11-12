package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;


import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StartNewGameController {

    private static Game game;
    private static boolean whitePlayerChooseName = false;
    private static boolean blackPlayerChooseName = false;
    private static boolean redPlayerChooseName = false;
    private static boolean bluePlayerChooseName = false;
    private static boolean thinkingTimeIsSet = false;
    private static long millis;
    private static Timer timer;
    private static long timeToSet;


    public StartNewGameController(){};

    /**
     * @Author Fulin Huang
     *
     * Attempts to initialize a game and
     * set game status to be initializing
     *
     */
    public static void initializeGame()  {
        if (ModelQuery.getCurrentGame() != null) {
            ModelQuery.getCurrentGame().delete(); //delete the previous game
        }
        whitePlayerChooseName=false;
        blackPlayerChooseName=false;
        thinkingTimeIsSet=false;
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        game = new Game(Game.GameStatus.Initializing, Game.MoveMode.PlayerMove, quoridor);
    }

    /**
     * @Author Fulin Huang
     *
     * White player chooses a username by either creating a new name or by choosing
     * from an existing name list.
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
        Time x = new Time(40);
        int tempThinkingTime = 90;
        Player player = new Player(new Time(tempThinkingTime), white_user, 9, Direction.Horizontal);
        ModelQuery.getCurrentGame().setWhitePlayer(player); //set White player
        ModelQuery.getWhitePlayer().setUser(white_user);

        isReadyToStart(); //check if white and black player chose name and if total thinking time is set
    }

    /**
     * @Author Fulin Huang
     *
     * Black player chooses a username by either creating a new game or
     * by choosing from an existing name list.
     *
     * @param username name of the black user
     */
    public static void blackPlayerChooseAUsername(String username) {
        User black_user = null;
        if (usernameExists(username)) {
            black_user = UserController.selectExistingUsername(username);
            blackPlayerChooseName = true;
        } else {
            black_user = UserController.newUsername(username);
            blackPlayerChooseName = true;
        }
        int tempThinkingTime = 90;
        Player player = new Player(new Time(tempThinkingTime), black_user, 1, Direction.Vertical);
        ModelQuery.getCurrentGame().setBlackPlayer(player);
        ModelQuery.getBlackPlayer().setUser(black_user);

        isReadyToStart();  //check if white and black player chose name and if total thinking time is set
    }

    /**
     * @Author Fulin Huang
     *
     * Attempts to set total thinking time for each player to ensure
     * that a game does not last forever
     *
     * @param minutes duration that the player wants to set
     * @param seconds duration that the player wants to set
     *
     */
    public static void setTotalThinkingTime (int minutes, int seconds) {
        //total thinking time is able to set only if players are existed
        if (whitePlayerChooseName && blackPlayerChooseName) {
            setThinkingTime(minutes, seconds);   //set total thinking time
            thinkingTimeIsSet = true;
        }

        isReadyToStart();
    }

    /**
     * @Author Fulin Huang
     *
     * Start the clock once the game is "ReadyToStart"
     * Initialize board if the board is not yet been initialized
     * then set the game status to be "Running"
     *
     */
    public static void startTheClock() {
        //set up timer
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timeToSet = millis;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run () {

                    Player currentPlayer;
                    try {
                        if (timeToSet == 0) {
//                            System.out.println("00:00 time left!");
                            timer.cancel(); //stop timer if zero time left

                        } else if (ModelQuery.getBlackPlayer() == null && ModelQuery.getWhitePlayer() == null) {
                            timer.cancel();
                        }
                        else {
                            currentPlayer = ModelQuery.getPlayerToMove();
                            timeToSet = timeToSet - 1000; // time to set in milliseconds
                            Date date = new Date();
                            long currentMillis = date.getTime();
                            Time newThinkingTime = new Time(timeToSet + currentMillis);
                            currentPlayer.setRemainingTime(newThinkingTime);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }


        }, delay, period);
        //initialize board
        if (ModelQuery.getBoard()== null){
            BoardController.initializeBoard();
        }
        //change game status
        ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.Running);

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
        millis = minutes * 60 * 1000 + seconds * 1000;
        Date date = new Date();
        long currentMillis = date.getTime();
        Time totalThinkingTime = new Time(millis+currentMillis);
        ModelQuery.getWhitePlayer().setRemainingTime(totalThinkingTime);
        ModelQuery.getBlackPlayer().setRemainingTime(totalThinkingTime);
        return totalThinkingTime;
    }

    /**
     * @Author Fulin Huang
     *
     * This method checks if white player choose name, black player choose name,
     * and total thinking time is set.
     * If they are all set, then the game state will update to "ReadyToStart"
     *
     */
    public static void isReadyToStart(){
        if(whitePlayerChooseName && blackPlayerChooseName & thinkingTimeIsSet){
            ModelQuery.getCurrentGame().setGameStatus(Game.GameStatus.ReadyToStart);
            ModelQuery.getWhitePlayer().setNextPlayer(ModelQuery.getBlackPlayer());
            ModelQuery.getBlackPlayer().setNextPlayer(ModelQuery.getWhitePlayer());
        }

    }

    /**
     * @Author Fulin Huang
     * This method checks if the username exists
     *
     * @param username name of a player
     * @return a boolean to indicate if the username exists
     *
     */
    public static boolean usernameExists(String username) {
        boolean nameExist = false;
        List<User> users = QuoridorApplication.getQuoridor().getUsers();
        for (User user: users) {
            if (user.getName().equals(username)) {
                nameExist = true;
            }
        }
        return nameExist;
    }

    /**
     * @Author Fulin Huang
     *
     * Attempts to show remaining time on UI
     *
     * @return a string that indicates the remaining time
     */
    public static String toTimeStr() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeToSet);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeToSet) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeToSet));
        StringBuilder sb = new StringBuilder();
        if (minutes < 10) {
            sb.append(0).append(minutes);
        } else {
            sb.append(minutes);
        }
        sb.append(":");
        if (seconds < 10) {
            sb.append(0).append(seconds);
        } else {
            sb.append(seconds);
        }
        return sb.toString();
    }
    
    /**
     * @Author Fulin Huang
     *
     * Attempts to get the total thinking time that the user set
     *
     * @return the total thinking time
     */
    public static Time getTotalThinkingTime() {
        Date date = new Date();
        long currentMillis = date.getTime();
        Time totalThinkingTime = new Time(timeToSet + currentMillis);
        return totalThinkingTime;
    }

    /**
     * @Author Fulin Huang
     * Check if the white player set a name
     *
     * @return a boolean to indicate if the white player set a name
     */
    public static boolean whitePlayerNameIsSet() {
        return  whitePlayerChooseName;
    }

    /**
     * @Author Fulin Huang
     * check if the black player set a name
     *
     * @return a boolean to indicate if the black player set a name
     */
    public static boolean blackPlayerNameIsSet() {
        return blackPlayerChooseName;
    }

    /**
     * @Author Fulin Huang
     * check if the total thinking time is set
     *
     * @return a boolean to indicate if the total thinking time is set
     */
    public static boolean totalTimeIsSet() {
        return thinkingTimeIsSet;
    }

    /**
     * @Author Fulin Huang
     * Attempts to get all the users
     *
     * @return a list of users
     */
    public static List<User> existedUsers() {
        List<User> users = QuoridorApplication.getQuoridor().getUsers();

        return users;
    }
    
    /**
     * @Author Mark Zhu
     * Resets timeToThink between rounds
     */
    public static void resetTimeToSet() {
        timeToSet = millis;
    }
    
    /**
     * @Author Mark Zhu
     * returns whether or not the timer has run out
     * @returns true if the timer has run out, false otherwise
     */
    public static boolean timeOver() {
        return timeToSet==0;
    }
}
