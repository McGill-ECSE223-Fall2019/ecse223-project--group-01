/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;
import java.util.*;

// line 5 "../Quorridor.ump"
// line 99 "../Quorridor.ump"
public class Board
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Board Associations
  private Quoridor quoridor;
  private List<Tile> tiles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Board(Quoridor aQuoridor)
  {
    boolean didAddQuoridor = setQuoridor(aQuoridor);
    if (!didAddQuoridor)
    {
      throw new RuntimeException("Unable to create board due to quoridor");
    }
    tiles = new ArrayList<Tile>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Quoridor getQuoridor()
  {
    return quoridor;
  }
  /* Code from template association_GetMany */
  public Tile getTile(int index)
  {
    Tile aTile = tiles.get(index);
    return aTile;
  }

  public List<Tile> getTiles()
  {
    List<Tile> newTiles = Collections.unmodifiableList(tiles);
    return newTiles;
  }

  public int numberOfTiles()
  {
    int number = tiles.size();
    return number;
  }

  public boolean hasTiles()
  {
    boolean has = tiles.size() > 0;
    return has;
  }

  public int indexOfTile(Tile aTile)
  {
    int index = tiles.indexOf(aTile);
    return index;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setQuoridor(Quoridor aNewQuoridor)
  {
    boolean wasSet = false;
    if (aNewQuoridor == null)
    {
      //Unable to setQuoridor to null, as board must always be associated to a quoridor
      return wasSet;
    }
    
    Board existingBoard = aNewQuoridor.getBoard();
    if (existingBoard != null && !equals(existingBoard))
    {
      //Unable to setQuoridor, the current quoridor already has a board, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Quoridor anOldQuoridor = quoridor;
    quoridor = aNewQuoridor;
    quoridor.setBoard(this);

    if (anOldQuoridor != null)
    {
      anOldQuoridor.setBoard(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTilesValid()
  {
    boolean isValid = numberOfTiles() >= minimumNumberOfTiles() && numberOfTiles() <= maximumNumberOfTiles();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfTiles()
  {
    return 81;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTiles()
  {
    return 81;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTiles()
  {
    return 81;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Tile addTile(int aX, int aY)
  {
    if (numberOfTiles() >= maximumNumberOfTiles())
    {
      return null;
    }
    else
    {
      return new Tile(aX, aY, this);
    }
  }

  public boolean addTile(Tile aTile)
  {
    boolean wasAdded = false;
    if (tiles.contains(aTile)) { return false; }
    if (numberOfTiles() >= maximumNumberOfTiles())
    {
      return wasAdded;
    }

    Board existingBoard = aTile.getBoard();
    boolean isNewBoard = existingBoard != null && !this.equals(existingBoard);

    if (isNewBoard && existingBoard.numberOfTiles() <= minimumNumberOfTiles())
    {
      return wasAdded;
    }

    if (isNewBoard)
    {
      aTile.setBoard(this);
    }
    else
    {
      tiles.add(aTile);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTile(Tile aTile)
  {
    boolean wasRemoved = false;
    //Unable to remove aTile, as it must always have a board
    if (this.equals(aTile.getBoard()))
    {
      return wasRemoved;
    }

    //board already at minimum (81)
    if (numberOfTiles() <= minimumNumberOfTiles())
    {
      return wasRemoved;
    }
    tiles.remove(aTile);
    wasRemoved = true;
    return wasRemoved;
  }

  public void delete()
  {
    Quoridor existingQuoridor = quoridor;
    quoridor = null;
    if (existingQuoridor != null)
    {
      existingQuoridor.setBoard(null);
    }
    while (tiles.size() > 0)
    {
      Tile aTile = tiles.get(tiles.size() - 1);
      aTile.delete();
      tiles.remove(aTile);
    }
    
  }

}