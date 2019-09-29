/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package -;

// line 53 "../Quorridor.ump"
// line 148 "../Quorridor.ump"
public class User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private String username;

  //User Associations
  private Quoridor quoridor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aUsername, Quoridor aQuoridor)
  {
    username = aUsername;
    boolean didAddQuoridor = setQuoridor(aQuoridor);
    if (!didAddQuoridor)
    {
      throw new RuntimeException("Unable to create user due to quoridor");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    username = aUsername;
    wasSet = true;
    return wasSet;
  }

  public String getUsername()
  {
    return username;
  }
  /* Code from template association_GetOne */
  public Quoridor getQuoridor()
  {
    return quoridor;
  }
  /* Code from template association_SetOneToMany */
  public boolean setQuoridor(Quoridor aQuoridor)
  {
    boolean wasSet = false;
    if (aQuoridor == null)
    {
      return wasSet;
    }

    Quoridor existingQuoridor = quoridor;
    quoridor = aQuoridor;
    if (existingQuoridor != null && !existingQuoridor.equals(aQuoridor))
    {
      existingQuoridor.removeUser(this);
    }
    quoridor.addUser(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Quoridor placeholderQuoridor = quoridor;
    this.quoridor = null;
    if(placeholderQuoridor != null)
    {
      placeholderQuoridor.removeUser(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "quoridor = "+(getQuoridor()!=null?Integer.toHexString(System.identityHashCode(getQuoridor())):"null");
  }
}