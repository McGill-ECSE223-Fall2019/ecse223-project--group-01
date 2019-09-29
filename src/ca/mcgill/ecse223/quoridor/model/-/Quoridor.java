/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 1 "../Quorridor.ump"
// line 94 "../Quorridor.ump"
public class Quoridor
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Quoridor Associations
  private Game game;
  private Board board;
  private List<User> users;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Quoridor()
  {
    users = new ArrayList<User>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }

  public boolean hasGame()
  {
    boolean has = game != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Board getBoard()
  {
    return board;
  }

  public boolean hasBoard()
  {
    boolean has = board != null;
    return has;
  }
  /* Code from template association_GetMany */
  public User getUser(int index)
  {
    User aUser = users.get(index);
    return aUser;
  }

  public List<User> getUsers()
  {
    List<User> newUsers = Collections.unmodifiableList(users);
    return newUsers;
  }

  public int numberOfUsers()
  {
    int number = users.size();
    return number;
  }

  public boolean hasUsers()
  {
    boolean has = users.size() > 0;
    return has;
  }

  public int indexOfUser(User aUser)
  {
    int index = users.indexOf(aUser);
    return index;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setGame(Game aNewGame)
  {
    boolean wasSet = false;
    if (game != null && !game.equals(aNewGame) && equals(game.getQuoridor()))
    {
      //Unable to setGame, as existing game would become an orphan
      return wasSet;
    }

    game = aNewGame;
    Quoridor anOldQuoridor = aNewGame != null ? aNewGame.getQuoridor() : null;

    if (!this.equals(anOldQuoridor))
    {
      if (anOldQuoridor != null)
      {
        anOldQuoridor.game = null;
      }
      if (game != null)
      {
        game.setQuoridor(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setBoard(Board aNewBoard)
  {
    boolean wasSet = false;
    if (board != null && !board.equals(aNewBoard) && equals(board.getQuoridor()))
    {
      //Unable to setBoard, as existing board would become an orphan
      return wasSet;
    }

    board = aNewBoard;
    Quoridor anOldQuoridor = aNewBoard != null ? aNewBoard.getQuoridor() : null;

    if (!this.equals(anOldQuoridor))
    {
      if (anOldQuoridor != null)
      {
        anOldQuoridor.board = null;
      }
      if (board != null)
      {
        board.setQuoridor(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfUsers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public User addUser(String aUsername)
  {
    return new User(aUsername, this);
  }

  public boolean addUser(User aUser)
  {
    boolean wasAdded = false;
    if (users.contains(aUser)) { return false; }
    Quoridor existingQuoridor = aUser.getQuoridor();
    boolean isNewQuoridor = existingQuoridor != null && !this.equals(existingQuoridor);
    if (isNewQuoridor)
    {
      aUser.setQuoridor(this);
    }
    else
    {
      users.add(aUser);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUser(User aUser)
  {
    boolean wasRemoved = false;
    //Unable to remove aUser, as it must always have a quoridor
    if (!this.equals(aUser.getQuoridor()))
    {
      users.remove(aUser);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addUserAt(User aUser, int index)
  {  
    boolean wasAdded = false;
    if(addUser(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(users.contains(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addUserAt(aUser, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Game existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.delete();
      existingGame.setQuoridor(null);
    }
    Board existingBoard = board;
    board = null;
    if (existingBoard != null)
    {
      existingBoard.delete();
      existingBoard.setQuoridor(null);
    }
    while (users.size() > 0)
    {
      User aUser = users.get(users.size() - 1);
      aUser.delete();
      users.remove(aUser);
    }
    
  }

}