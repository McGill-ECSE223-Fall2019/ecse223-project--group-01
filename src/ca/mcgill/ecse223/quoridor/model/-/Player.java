/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 16 "../Quorridor.ump"
// line 109 "../Quorridor.ump"
public class Player
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Color { Black, White }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private int time_thinking;
  private Color color;

  //Player Associations
  private Game game;
  private User user;
  private Pawn pawn;
  private List<Wall> stockWalls;
  private List<Wall> playedWalls;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(int aTime_thinking, Color aColor, Game aGame, User aUser, Pawn aPawn)
  {
    time_thinking = aTime_thinking;
    color = aColor;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create player due to game");
    }
    if (!setUser(aUser))
    {
      throw new RuntimeException("Unable to create Player due to aUser");
    }
    if (aPawn == null || aPawn.getPlayer() != null)
    {
      throw new RuntimeException("Unable to create Player due to aPawn");
    }
    pawn = aPawn;
    stockWalls = new ArrayList<Wall>();
    playedWalls = new ArrayList<Wall>();
  }

  public Player(int aTime_thinking, Color aColor, Game aGame, User aUser, StartingPosition aStartPositionForPawn, Tile aTileForPawn, Game aGameForPawn)
  {
    time_thinking = aTime_thinking;
    color = aColor;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create player due to game");
    }
    boolean didAddUser = setUser(aUser);
    if (!didAddUser)
    {
      throw new RuntimeException("Unable to create player due to user");
    }
    pawn = new Pawn(aStartPositionForPawn, aTileForPawn, aGameForPawn, this);
    stockWalls = new ArrayList<Wall>();
    playedWalls = new ArrayList<Wall>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTime_thinking(int aTime_thinking)
  {
    boolean wasSet = false;
    time_thinking = aTime_thinking;
    wasSet = true;
    return wasSet;
  }

  public boolean setColor(Color aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public int getTime_thinking()
  {
    return time_thinking;
  }

  public Color getColor()
  {
    return color;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public User getUser()
  {
    return user;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
  }
  /* Code from template association_GetMany */
  public Wall getStockWall(int index)
  {
    Wall aStockWall = stockWalls.get(index);
    return aStockWall;
  }

  public List<Wall> getStockWalls()
  {
    List<Wall> newStockWalls = Collections.unmodifiableList(stockWalls);
    return newStockWalls;
  }

  public int numberOfStockWalls()
  {
    int number = stockWalls.size();
    return number;
  }

  public boolean hasStockWalls()
  {
    boolean has = stockWalls.size() > 0;
    return has;
  }

  public int indexOfStockWall(Wall aStockWall)
  {
    int index = stockWalls.indexOf(aStockWall);
    return index;
  }
  /* Code from template association_GetMany */
  public Wall getPlayedWall(int index)
  {
    Wall aPlayedWall = playedWalls.get(index);
    return aPlayedWall;
  }

  public List<Wall> getPlayedWalls()
  {
    List<Wall> newPlayedWalls = Collections.unmodifiableList(playedWalls);
    return newPlayedWalls;
  }

  public int numberOfPlayedWalls()
  {
    int number = playedWalls.size();
    return number;
  }

  public boolean hasPlayedWalls()
  {
    boolean has = playedWalls.size() > 0;
    return has;
  }

  public int indexOfPlayedWall(Wall aPlayedWall)
  {
    int index = playedWalls.indexOf(aPlayedWall);
    return index;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to player
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (2)
    if (aGame.numberOfPlayer() >= Game.maximumNumberOfPlayer())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePlayer(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPlayer(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setUser(User aNewUser)
  {
    boolean wasSet = false;
    if (aNewUser != null)
    {
      user = aNewUser;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfStockWalls()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfStockWalls()
  {
    return 10;
  }
  /* Code from template association_AddOptionalNToOne */
  public Wall addStockWall(Game aGame, Player aPlayedBy)
  {
    if (numberOfStockWalls() >= maximumNumberOfStockWalls())
    {
      return null;
    }
    else
    {
      return new Wall(aGame, this, aPlayedBy);
    }
  }

  public boolean addStockWall(Wall aStockWall)
  {
    boolean wasAdded = false;
    if (stockWalls.contains(aStockWall)) { return false; }
    if (numberOfStockWalls() >= maximumNumberOfStockWalls())
    {
      return wasAdded;
    }

    Player existingOwner = aStockWall.getOwner();
    boolean isNewOwner = existingOwner != null && !this.equals(existingOwner);
    if (isNewOwner)
    {
      aStockWall.setOwner(this);
    }
    else
    {
      stockWalls.add(aStockWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeStockWall(Wall aStockWall)
  {
    boolean wasRemoved = false;
    //Unable to remove aStockWall, as it must always have a owner
    if (!this.equals(aStockWall.getOwner()))
    {
      stockWalls.remove(aStockWall);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addStockWallAt(Wall aStockWall, int index)
  {  
    boolean wasAdded = false;
    if(addStockWall(aStockWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStockWalls()) { index = numberOfStockWalls() - 1; }
      stockWalls.remove(aStockWall);
      stockWalls.add(index, aStockWall);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStockWallAt(Wall aStockWall, int index)
  {
    boolean wasAdded = false;
    if(stockWalls.contains(aStockWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStockWalls()) { index = numberOfStockWalls() - 1; }
      stockWalls.remove(aStockWall);
      stockWalls.add(index, aStockWall);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStockWallAt(aStockWall, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayedWalls()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPlayedWalls()
  {
    return 10;
  }
  /* Code from template association_AddOptionalNToOne */
  public Wall addPlayedWall(Game aGame, Player aOwner)
  {
    if (numberOfPlayedWalls() >= maximumNumberOfPlayedWalls())
    {
      return null;
    }
    else
    {
      return new Wall(aGame, aOwner, this);
    }
  }

  public boolean addPlayedWall(Wall aPlayedWall)
  {
    boolean wasAdded = false;
    if (playedWalls.contains(aPlayedWall)) { return false; }
    if (numberOfPlayedWalls() >= maximumNumberOfPlayedWalls())
    {
      return wasAdded;
    }

    Player existingPlayedBy = aPlayedWall.getPlayedBy();
    boolean isNewPlayedBy = existingPlayedBy != null && !this.equals(existingPlayedBy);
    if (isNewPlayedBy)
    {
      aPlayedWall.setPlayedBy(this);
    }
    else
    {
      playedWalls.add(aPlayedWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayedWall(Wall aPlayedWall)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlayedWall, as it must always have a playedBy
    if (!this.equals(aPlayedWall.getPlayedBy()))
    {
      playedWalls.remove(aPlayedWall);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlayedWallAt(Wall aPlayedWall, int index)
  {  
    boolean wasAdded = false;
    if(addPlayedWall(aPlayedWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayedWalls()) { index = numberOfPlayedWalls() - 1; }
      playedWalls.remove(aPlayedWall);
      playedWalls.add(index, aPlayedWall);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlayedWallAt(Wall aPlayedWall, int index)
  {
    boolean wasAdded = false;
    if(playedWalls.contains(aPlayedWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayedWalls()) { index = numberOfPlayedWalls() - 1; }
      playedWalls.remove(aPlayedWall);
      playedWalls.add(index, aPlayedWall);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPlayedWallAt(aPlayedWall, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayer(this);
    }
    user = null;
    Pawn existingPawn = pawn;
    pawn = null;
    if (existingPawn != null)
    {
      existingPawn.delete();
    }
    for(int i=stockWalls.size(); i > 0; i--)
    {
      Wall aStockWall = stockWalls.get(i - 1);
      aStockWall.delete();
    }
    for(int i=playedWalls.size(); i > 0; i--)
    {
      Wall aPlayedWall = playedWalls.get(i - 1);
      aPlayedWall.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "time_thinking" + ":" + getTime_thinking()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "user = "+(getUser()!=null?Integer.toHexString(System.identityHashCode(getUser())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null");
  }
}