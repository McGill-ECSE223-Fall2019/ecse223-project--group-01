/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 43 "../Quorridor.ump"
// line 138 "../Quorridor.ump"
public class WallMove extends Move
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Orientation { Vertical, Horizontal }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WallMove Attributes
  private Orientation orientation;

  //WallMove Associations
  private Wall wall;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WallMove(Tile aTile, Game aGame, Orientation aOrientation, Wall aWall)
  {
    super(aTile, aGame);
    orientation = aOrientation;
    boolean didAddWall = setWall(aWall);
    if (!didAddWall)
    {
      throw new RuntimeException("Unable to create wallMove due to wall");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOrientation(Orientation aOrientation)
  {
    boolean wasSet = false;
    orientation = aOrientation;
    wasSet = true;
    return wasSet;
  }

  public Orientation getOrientation()
  {
    return orientation;
  }
  /* Code from template association_GetOne */
  public Wall getWall()
  {
    return wall;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setWall(Wall aNewWall)
  {
    boolean wasSet = false;
    if (aNewWall == null)
    {
      //Unable to setWall to null, as wallMove must always be associated to a wall
      return wasSet;
    }
    
    WallMove existingWallMove = aNewWall.getWallMove();
    if (existingWallMove != null && !equals(existingWallMove))
    {
      //Unable to setWall, the current wall already has a wallMove, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Wall anOldWall = wall;
    wall = aNewWall;
    wall.setWallMove(this);

    if (anOldWall != null)
    {
      anOldWall.setWallMove(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Wall existingWall = wall;
    wall = null;
    if (existingWall != null)
    {
      existingWall.setWallMove(null);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orientation" + "=" + (getOrientation() != null ? !getOrientation().equals(this)  ? getOrientation().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "wall = "+(getWall()!=null?Integer.toHexString(System.identityHashCode(getWall())):"null");
  }
}