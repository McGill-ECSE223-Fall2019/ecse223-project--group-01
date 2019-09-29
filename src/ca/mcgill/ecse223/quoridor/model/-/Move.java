/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 32 "../Quorridor.ump"
// line 126 "../Quorridor.ump"
public class Move
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Move Associations
  private Tile tile;
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Move(Tile aTile, Game aGame)
  {
    if (!setTile(aTile))
    {
      throw new RuntimeException("Unable to create Move due to aTile");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create move due to game");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
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
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removeMove(this);
    }
    game.addMove(this);
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
      placeholderGame.removeMove(this);
    }
  }

}