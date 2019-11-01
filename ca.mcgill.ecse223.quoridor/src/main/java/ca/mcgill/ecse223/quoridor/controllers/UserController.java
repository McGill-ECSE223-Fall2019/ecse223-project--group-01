package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.Player;

import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;


import java.util.List;

public class UserController {

    /**
     * This controller method is responsible for player to select or create their username
     * Returns true if operation is successful
     *
     * @param username  The target username of player
     * @return          The operation of the method
     * @throws UnsupportedOperationException
     *
     * @Author: Jason Lau
     */
    public static User newUsername(String username)throws UnsupportedOperationException {

        List<User> users = QuoridorApplication.getQuoridor().getUsers();

        for(User user : users){
            if(user.getName().equals(username)){
                return null;
            }
        }
        return QuoridorApplication.getQuoridor().addUser(username);
    }


    /**
     * This method is responsible for player to select existing user name
     * Returns true if the user name selection is completed
     *
     * @param username  target user name of current player
     * @return          operation of the method
     * @throws UnsupportedOperationException
     *
     * @Author: Jason Lau
     */

    public static User selectExistingUsername(String username)throws UnsupportedOperationException{

        List<User> users = QuoridorApplication.getQuoridor().getUsers();
        User userToSet= null;

        for(User user : users){
            if(user.getName().equals(username)){
                userToSet = user;
            }
        }

        return userToSet;

    }


}
