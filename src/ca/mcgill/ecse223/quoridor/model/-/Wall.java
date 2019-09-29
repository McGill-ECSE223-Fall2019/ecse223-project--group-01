/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 48 "../Quorridor.ump"
// line 142 "../Quorridor.ump"
public class Wall
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wall Associations
  private Game game;
  private WallMove wallMove;
  private Player owner;
  private Player playedBy;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(Game aGame, Player aOwner, Player aPlayedBy)
  {
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create wall due to game");
    }
    boolean didAddOwner = setOwner(aOwner);
    if (!didAddOwner)
    {
      throw new RuntimeException("Unable to create stockWall due to owner");
    }
    boolean didAddPlayedBy = setPlayedBy(aPlayedBy);
    if (!didAddPlayedBy)
    {
      throw new RuntimeException("Unable to create playedWall due to playedBy");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public WallMove getWallMove()
  {
    return wallMove;
  }

  public boolean hasWallMove()
  {
    boolean has = wallMove != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getOwner()
  {
    return owner;
  }
  /* Code from template association_GetOne */
  public Player getPlayedBy()
  {
    return playedBy;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to wall
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (20)
    if (aGame.numberOfWalls() >= Game.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removeWall(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addWall(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setWallMove(WallMove aNewWallMove)
  {
    boolean wasSet = false;
    if (wallMove != null && !wallMove.equals(aNewWallMove) && equals(wallMove.getWall()))
    {
      //Unable to setWallMove, as existing wallMove would become an orphan
      return wasSet;
    }

    wallMove = aNewWallMove;
    Wall anOldWall = aNewWallMove != null ? aNewWallMove.getWall() : null;

    if (!this.equals(anOldWall))
    {
      if (anOldWall != null)
      {
        anOldWall.wallMove = null;
      }
      if (wallMove != null)
      {
        wallMove.setWall(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setOwner(Player aOwner)
  {
    boolean wasSet = false;
    //Must provide owner to stockWall
    if (aOwner == null)
    {
      return wasSet;
    }

    //owner already at maximum (10)
    if (aOwner.numberOfStockWalls() >= Player.maximumNumberOfStockWalls())
    {
      return wasSet;
    }
    
    Player existingOwner = owner;
    owner = aOwner;
    if (existingOwner != null && !existingOwner.equals(aOwner))
    {
      boolean didRemove = existingOwner.removeStockWall(this);
      if (!didRemove)
      {
        owner = existingOwner;
        return wasSet;
      }
    }
    owner.addStockWall(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPlayedBy(Player aPlayedBy)
  {
    boolean wasSet = false;
    //Must provide playedBy to playedWall
    if (aPlayedBy == null)
    {
      return wasSet;
    }

    //playedBy already at maximum (10)
    if (aPlayedBy.numberOfPlayedWalls() >= Player.maximumNumberOfPlayedWalls())
    {
      return wasSet;
    }
    
    Player existingPlayedBy = playedBy;
    playedBy = aPlayedBy;
    if (existingPlayedBy != null && !existingPlayedBy.equals(aPlayedBy))
    {
      boolean didRemove = existingPlayedBy.removePlayedWall(this);
      if (!didRemove)
      {
        playedBy = existingPlayedBy;
        return wasSet;
      }
    }
    playedBy.addPlayedWall(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removeWall(this);
    }
    WallMove existingWallMove = wallMove;
    wallMove = null;
    if (existingWallMove != null)
    {
      existingWallMove.delete();
    }
    Player placeholderOwner = owner;
    this.owner = null;
    if(placeholderOwner != null)
    {
      placeholderOwner.removeStockWall(this);
    }
    Player placeholderPlayedBy = playedBy;
    this.playedBy = null;
    if(placeholderPlayedBy != null)
    {
      placeholderPlayedBy.removePlayedWall(this);
    }
  }

}