/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 26 "../Quorridor.ump"
// line 121 "../Quorridor.ump"
public class Tile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tile Attributes
  private int x;
  private int y;

  //Tile Associations
  private Board board;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tile(int aX, int aY, Board aBoard)
  {
    x = aX;
    y = aY;
    boolean didAddBoard = setBoard(aBoard);
    if (!didAddBoard)
    {
      throw new RuntimeException("Unable to create tile due to board");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setX(int aX)
  {
    boolean wasSet = false;
    x = aX;
    wasSet = true;
    return wasSet;
  }

  public boolean setY(int aY)
  {
    boolean wasSet = false;
    y = aY;
    wasSet = true;
    return wasSet;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }
  /* Code from template association_GetOne */
  public Board getBoard()
  {
    return board;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setBoard(Board aBoard)
  {
    boolean wasSet = false;
    //Must provide board to tile
    if (aBoard == null)
    {
      return wasSet;
    }

    //board already at maximum (81)
    if (aBoard.numberOfTiles() >= Board.maximumNumberOfTiles())
    {
      return wasSet;
    }
    
    Board existingBoard = board;
    board = aBoard;
    if (existingBoard != null && !existingBoard.equals(aBoard))
    {
      boolean didRemove = existingBoard.removeTile(this);
      if (!didRemove)
      {
        board = existingBoard;
        return wasSet;
      }
    }
    board.addTile(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Board placeholderBoard = board;
    this.board = null;
    if(placeholderBoard != null)
    {
      placeholderBoard.removeTile(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "x" + ":" + getX()+ "," +
            "y" + ":" + getY()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "board = "+(getBoard()!=null?Integer.toHexString(System.identityHashCode(getBoard())):"null");
  }
}