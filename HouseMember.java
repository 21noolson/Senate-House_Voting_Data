
public class HouseMember {
  String nameID; // House assigned nameID
  String[] houseSessions; // List of congressional sessions CongressPerson was/is apart of
  Character[] parties; // List of party affiliations for each congressional Session
  String[] states; // List of states represented for each congressional Session
  public int length;
  
  /**
   * @param nameID House assigned nameID
   */
  public HouseMember(String nameID) {
    this.nameID = nameID;
  }
  
  /**
   * Adds new session data to the HouseMember
   * 
   * @param houseSession the HouseMember was/is apart of. Formated as Session-Congress; #-###
   * @param party party affiliation during house Session
   * @param state state represented for house Session 
   */
  public void addSession(String houseSession, Character party, String state) {
    // Create new Arrays for sessions, party, and state with incremented length
    int newLength = length+1;
    String[] newhouseSessions = new String[newLength];
    Character[] newParties = new Character[newLength];
    String[] newStates = new String[newLength];
    
    // Adds the current data to the new Arrays
    for(int i=0; i<length; i++) {
      newhouseSessions[i] = houseSessions[i];
      newParties[i] = parties[i];
      newStates[i] = states[i];
    }
    
    // Adds the new data to the new Arrays
    newhouseSessions[length] = houseSession;
    newParties[length] = party;
    newStates[length] = state;
    
    // Assigns the new Arrays to the appropriate variables in the object
    houseSessions = newhouseSessions;
    parties = newParties;
    states = newStates;
    
    // Update length value
    length = newLength;
  }
  
  /**
   * If the same HouseMember is stored in two HouseMember Objects they can be combined
   * 
   * @param otherPerson the other HouseMember to be combined with
   * @return true if the combination was successful and false if it wasn't;
   *         IE the people were not equal
   */
  public boolean combineMember(HouseMember otherPerson) {
    // If the two HouseMembers are not the same RETURN FALSE
    if(!this.equals(otherPerson)) { return false; }
    
    // Loop Through all of the HouseSessions in the otherPerson
    for(int i=0; i<otherPerson.length; i++) {
      // Determines if the current Session already exists in the current HousePerson
      boolean sessionFound = false;
      String otherSession = otherPerson.getHouseSessions()[i];
      // Loops through all the stored sessions in the current HousePerson
      for(int j=0; j<this.length; j++) {
        String session = this.houseSessions[j];
        // If the session is found mark appropriate variable and break the loop
        if(session.equals(otherSession)) {
          sessionFound = true; 
          break;
        }
      }
      // If the session was found skip and move to the next session
      if(sessionFound) { continue; }
      // OTHERWISE add the newSession data
      this.addSession(otherSession, otherPerson.getParties()[i], otherPerson.getStates()[i]);
    }
    return true;
  }
  
  /**
   * Two HouseMembers are considered equal if and only if their
   *     firstName, lastName, and memberID are the same
   */
  @Override
  public boolean equals(Object o) {
    // If the object isn't the same class RETURN FALSE
    if(!(o instanceof HouseMember)) { return false; }
    // Cast the object to a CongressPerson
    HouseMember member = (HouseMember) o;
    return this.nameID.equals(member.nameID);
  } 
  
  public String getNameID() {
    return this.nameID; 
  }
  
  public String[] getHouseSessions() {
    return this.houseSessions;
  }
  
  public Character[] getParties() { 
    return this.parties; 
  }
  
  public String[] getStates() { 
    return this.states; 
  }
  
}
