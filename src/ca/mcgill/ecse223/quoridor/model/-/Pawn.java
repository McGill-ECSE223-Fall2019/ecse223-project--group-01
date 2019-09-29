/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 22 "../Quorridor.ump"
// line 115 "../Quorridor.ump"
public class Pawn
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum StartingPosition { Top, Bot, Left, Right }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Pawn Attributes
  private StartingPosition startPosition;

  //Pawn Associations
  private Tile tile;
  private Game game;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(StartingPosition aStartPosition, Tile aTile, Game aGame, Player aPlayer)
  {
    startPosition = aStartPosition;
    if (!setTile(aTile))
    {
      throw new RuntimeException("Unable to create Pawn due to aTile");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    if (aPlayer == null || aPlayer.getPawn() != null)
    {
      throw new RuntimeException("Unable to create Pawn due to aPlayer");
    }
    player = aPlayer;
  }

  public Pawn(StartingPosition aStartPosition, Tile aTile, Game aGame, int aTime_thinkingForPlayer, Color aColorForPlayer, Game aGameForPlayer, User aUserForPlayer)
  {
    startPosition = aStartPosition;
    boolean didAddTile = setTile(aTile);
    if (!didAddTile)
    {
      throw new RuntimeException("Unable to create pawn due to tile");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    player = new Player(aTime_thinkingForPlayer, aColorForPlayer, aGameForPlayer, aUserForPlayer, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartPosition(StartingPosition aStartPosition)
  {
    boolean wasSet = false;
    startPosition = aStartPosition;
    wasSet = true;
    return wasSet;
  }

  public StartingPosition getStartPosition()
  {
    return startPosition;
  }
  /* Code from template association_GetOne */
  public Tile getTile()
  {
    return tile;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setTile(Tile aNewTile)
  {
    boolean wasSet = false;
    if (aNewTile != null)
    {
      tile = aNewTile;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to pawn
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (2)
    if (aGame.numberOfPawns() >= Game.maximumNumberOfPawns())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePawn(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPawn(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    tile = null;
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePawn(this);
    }
    Player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startPosition" + "=" + (getStartPosition() != null ? !getStartPosition().equals(this)  ? getStartPosition().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "tile = "+(getTile()!=null?Integer.toHexString(System.identityHashCode(getTile())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null");
  }
}