/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 9 "../Quorridor.ump"
// line 153 "../Quorridor.ump"
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum StartingPosition { Top, Bot, Left, Right }
  public enum GameMode { Versus, Replay }
  public enum Color { Black, White }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int currentTurn;
  private int totalGameTime;
  private GameMode mode;

  //Game Associations
  private List<Wall> walls;
  private List<Pawn> pawns;
  private List<Move> moves;
  private List<Player> player;
  private Quoridor quoridor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(int aCurrentTurn, int aTotalGameTime, GameMode aMode, Quoridor aQuoridor)
  {
    currentTurn = aCurrentTurn;
    totalGameTime = aTotalGameTime;
    mode = aMode;
    walls = new ArrayList<Wall>();
    pawns = new ArrayList<Pawn>();
    moves = new ArrayList<Move>();
    player = new ArrayList<Player>();
    boolean didAddQuoridor = setQuoridor(aQuoridor);
    if (!didAddQuoridor)
    {
      throw new RuntimeException("Unable to create game due to quoridor");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCurrentTurn(int aCurrentTurn)
  {
    boolean wasSet = false;
    currentTurn = aCurrentTurn;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalGameTime(int aTotalGameTime)
  {
    boolean wasSet = false;
    totalGameTime = aTotalGameTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setMode(GameMode aMode)
  {
    boolean wasSet = false;
    mode = aMode;
    wasSet = true;
    return wasSet;
  }

  public int getCurrentTurn()
  {
    return currentTurn;
  }

  public int getTotalGameTime()
  {
    return totalGameTime;
  }

  public GameMode getMode()
  {
    return mode;
  }
  /* Code from template association_GetMany */
  public Wall getWall(int index)
  {
    Wall aWall = walls.get(index);
    return aWall;
  }

  public List<Wall> getWalls()
  {
    List<Wall> newWalls = Collections.unmodifiableList(walls);
    return newWalls;
  }

  public int numberOfWalls()
  {
    int number = walls.size();
    return number;
  }

  public boolean hasWalls()
  {
    boolean has = walls.size() > 0;
    return has;
  }

  public int indexOfWall(Wall aWall)
  {
    int index = walls.indexOf(aWall);
    return index;
  }
  /* Code from template association_GetMany */
  public Pawn getPawn(int index)
  {
    Pawn aPawn = pawns.get(index);
    return aPawn;
  }

  public List<Pawn> getPawns()
  {
    List<Pawn> newPawns = Collections.unmodifiableList(pawns);
    return newPawns;
  }

  public int numberOfPawns()
  {
    int number = pawns.size();
    return number;
  }

  public boolean hasPawns()
  {
    boolean has = pawns.size() > 0;
    return has;
  }

  public int indexOfPawn(Pawn aPawn)
  {
    int index = pawns.indexOf(aPawn);
    return index;
  }
  /* Code from template association_GetMany */
  public Move getMove(int index)
  {
    Move aMove = moves.get(index);
    return aMove;
  }

  public List<Move> getMoves()
  {
    List<Move> newMoves = Collections.unmodifiableList(moves);
    return newMoves;
  }

  public int numberOfMoves()
  {
    int number = moves.size();
    return number;
  }

  public boolean hasMoves()
  {
    boolean has = moves.size() > 0;
    return has;
  }

  public int indexOfMove(Move aMove)
  {
    int index = moves.indexOf(aMove);
    return index;
  }
  /* Code from template association_GetMany */
  public Player getPlayer(int index)
  {
    Player aPlayer = player.get(index);
    return aPlayer;
  }

  public List<Player> getPlayer()
  {
    List<Player> newPlayer = Collections.unmodifiableList(player);
    return newPlayer;
  }

  public int numberOfPlayer()
  {
    int number = player.size();
    return number;
  }

  public boolean hasPlayer()
  {
    boolean has = player.size() > 0;
    return has;
  }

  public int indexOfPlayer(Player aPlayer)
  {
    int index = player.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_GetOne */
  public Quoridor getQuoridor()
  {
    return quoridor;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWalls()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWalls()
  {
    return 20;
  }
  /* Code from template association_AddOptionalNToOne */
  public Wall addWall(Player aOwner, Player aPlayedBy)
  {
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return null;
    }
    else
    {
      return new Wall(this, aOwner, aPlayedBy);
    }
  }

  public boolean addWall(Wall aWall)
  {
    boolean wasAdded = false;
    if (walls.contains(aWall)) { return false; }
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return wasAdded;
    }

    Game existingGame = aWall.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);
    if (isNewGame)
    {
      aWall.setGame(this);
    }
    else
    {
      walls.add(aWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWall(Wall aWall)
  {
    boolean wasRemoved = false;
    //Unable to remove aWall, as it must always have a game
    if (!this.equals(aWall.getGame()))
    {
      walls.remove(aWall);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWallAt(Wall aWall, int index)
  {  
    boolean wasAdded = false;
    if(addWall(aWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWalls()) { index = numberOfWalls() - 1; }
      walls.remove(aWall);
      walls.add(index, aWall);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWallAt(Wall aWall, int index)
  {
    boolean wasAdded = false;
    if(walls.contains(aWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWalls()) { index = numberOfWalls() - 1; }
      walls.remove(aWall);
      walls.add(index, aWall);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWallAt(aWall, index);
    }
    return wasAdded;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfPawnsValid()
  {
    boolean isValid = numberOfPawns() >= minimumNumberOfPawns() && numberOfPawns() <= maximumNumberOfPawns();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfPawns()
  {
    return 2;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPawns()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPawns()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Pawn addPawn(StartingPosition aStartPosition, Tile aTile, Player aPlayer)
  {
    if (numberOfPawns() >= maximumNumberOfPawns())
    {
      return null;
    }
    else
    {
      return new Pawn(aStartPosition, aTile, this, aPlayer);
    }
  }

  public boolean addPawn(Pawn aPawn)
  {
    boolean wasAdded = false;
    if (pawns.contains(aPawn)) { return false; }
    if (numberOfPawns() >= maximumNumberOfPawns())
    {
      return wasAdded;
    }

    Game existingGame = aPawn.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPawns() <= minimumNumberOfPawns())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPawn.setGame(this);
    }
    else
    {
      pawns.add(aPawn);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePawn(Pawn aPawn)
  {
    boolean wasRemoved = false;
    //Unable to remove aPawn, as it must always have a game
    if (this.equals(aPawn.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (2)
    if (numberOfPawns() <= minimumNumberOfPawns())
    {
      return wasRemoved;
    }
    pawns.remove(aPawn);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfMoves()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Move addMove(Tile aTile)
  {
    return new Move(aTile, this);
  }

  public boolean addMove(Move aMove)
  {
    boolean wasAdded = false;
    if (moves.contains(aMove)) { return false; }
    Game existingGame = aMove.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);
    if (isNewGame)
    {
      aMove.setGame(this);
    }
    else
    {
      moves.add(aMove);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMove(Move aMove)
  {
    boolean wasRemoved = false;
    //Unable to remove aMove, as it must always have a game
    if (!this.equals(aMove.getGame()))
    {
      moves.remove(aMove);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addMoveAt(Move aMove, int index)
  {  
    boolean wasAdded = false;
    if(addMove(aMove))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMoves()) { index = numberOfMoves() - 1; }
      moves.remove(aMove);
      moves.add(index, aMove);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMoveAt(Move aMove, int index)
  {
    boolean wasAdded = false;
    if(moves.contains(aMove))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMoves()) { index = numberOfMoves() - 1; }
      moves.remove(aMove);
      moves.add(index, aMove);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMoveAt(aMove, index);
    }
    return wasAdded;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfPlayerValid()
  {
    boolean isValid = numberOfPlayer() >= minimumNumberOfPlayer() && numberOfPlayer() <= maximumNumberOfPlayer();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfPlayer()
  {
    return 2;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayer()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPlayer()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Player addPlayer(int aTime_thinking, Color aColor, User aUser, Pawn aPawn)
  {
    if (numberOfPlayer() >= maximumNumberOfPlayer())
    {
      return null;
    }
    else
    {
      return new Player(aTime_thinking, aColor, this, aUser, aPawn);
    }
  }

  public boolean addPlayer(Player aPlayer)
  {
    boolean wasAdded = false;
    if (player.contains(aPlayer)) { return false; }
    if (numberOfPlayer() >= maximumNumberOfPlayer())
    {
      return wasAdded;
    }

    Game existingGame = aPlayer.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPlayer() <= minimumNumberOfPlayer())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPlayer.setGame(this);
    }
    else
    {
      player.add(aPlayer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayer(Player aPlayer)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlayer, as it must always have a game
    if (this.equals(aPlayer.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (2)
    if (numberOfPlayer() <= minimumNumberOfPlayer())
    {
      return wasRemoved;
    }
    player.remove(aPlayer);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setQuoridor(Quoridor aNewQuoridor)
  {
    boolean wasSet = false;
    if (aNewQuoridor == null)
    {
      //Unable to setQuoridor to null, as game must always be associated to a quoridor
      return wasSet;
    }
    
    Game existingGame = aNewQuoridor.getGame();
    if (existingGame != null && !equals(existingGame))
    {
      //Unable to setQuoridor, the current quoridor already has a game, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Quoridor anOldQuoridor = quoridor;
    quoridor = aNewQuoridor;
    quoridor.setGame(this);

    if (anOldQuoridor != null)
    {
      anOldQuoridor.setGame(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while (walls.size() > 0)
    {
      Wall aWall = walls.get(walls.size() - 1);
      aWall.delete();
      walls.remove(aWall);
    }
    
    while (pawns.size() > 0)
    {
      Pawn aPawn = pawns.get(pawns.size() - 1);
      aPawn.delete();
      pawns.remove(aPawn);
    }
    
    while (moves.size() > 0)
    {
      Move aMove = moves.get(moves.size() - 1);
      aMove.delete();
      moves.remove(aMove);
    }
    
    while (player.size() > 0)
    {
      Player aPlayer = player.get(player.size() - 1);
      aPlayer.delete();
      player.remove(aPlayer);
    }
    
    Quoridor existingQuoridor = quoridor;
    quoridor = null;
    if (existingQuoridor != null)
    {
      existingQuoridor.setGame(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "currentTurn" + ":" + getCurrentTurn()+ "," +
            "totalGameTime" + ":" + getTotalGameTime()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "mode" + "=" + (getMode() != null ? !getMode().equals(this)  ? getMode().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "quoridor = "+(getQuoridor()!=null?Integer.toHexString(System.identityHashCode(getQuoridor())):"null");
  }
}