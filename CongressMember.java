
public class CongressMember {
  String memberID; // Congress assigned Member ID
  String[] congressSessions; // List of congressional sessions CongressPerson was/is apart of
  Character[] parties; // List of party affiliations for each congressional Session
  String[] states; // List of states represented for each congressional Session
  public int length;
  
  /**
   * @param memberID Congress assigned Member ID
   */
  public CongressMember(String memberID) {
    this.memberID = memberID;
  }
  
  /**
   * Adds new session data to the congressPerson
   * 
   * @param congressSession congressional session the CongressPerson was/is apart of.
   *        Formated as Session-Congress; #-###
   * @param party party affiliation during congressional Session
   * @param state state represented for congressional Session 
   */
  public void addSession(String session, Character party, String state) {
	  // Create new Arrays for sessions, party, and state with incremented length
	  int sessions = congressSessions.length+1;
	  String[] newCongressSessions = new String[sessions];
	  Character[] newParties = new Character[sessions];
	  String[] newStates = new String[sessions];
	  
	  // Adds the current data to the new Arrays
	  for(int i=0; i<congressSessions.length; i++) {
		  newCongressSessions[i] = congressSessions[i];
		  newParties[i] = parties[i];
		  newStates[i] = states[i];
	  }
	  
	  // Adds the new data to the new Arrays
	  newCongressSessions[sessions-1] = session;
	  newParties[sessions-1] = party;
	  newStates[sessions-1] = state;
	  
	  // Assigns the new Arrays to the appropriate variables in the object
	  congressSessions = newCongressSessions;
	  parties = newParties;
	  states = newStates;
  }

  /**
   * If the same CongressPerson is stored in two CongressPerson Objects they can be combined
   * 
   * @param otherPerson the other CongressPerson to be combined with
   * @return true if the combination was successful and false if it wasn't;
   *         IE the people were not equal
   */
  public boolean combineMember(CongressMember otherPerson) {
    // If the two CongressMembers are not the same RETURN FALSE
    if(!this.equals(otherPerson)) { return false; }
    
    // Loop Through all of the CongressSessions in the otherPerson
    for(int i=0; i<otherPerson.length; i++) {
      // Determines if the current Session already exists in the current CongressPerson
      boolean sessionFound = false;
      String otherSession = otherPerson.getCongressSessions()[i];
      // Loops through all the stored sessions in the current CongressPerson
      for(int j=0; j<this.length; j++) {
        String session = this.congressSessions[j];
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
   * Two CongressPersons are considered equal if and only if their
   *     firstName, lastName, and memberID are the same
   */
  @Override
  public boolean equals(Object o) {
 // If the object isn't the same class RETURN FALSE
    if(!(o instanceof CongressMember)) { return false; }
    // Cast the object to a CongressPerson
    CongressMember member = (CongressMember) o;
    return this.memberID.equals(member.memberID);
  } 
  
  public String getMemberID() {
    return this.memberID; 
  }
  
  public String[] getCongressSessions() {
    return this.congressSessions;
  }
  
  public Character[] getParties() { 
    return this.parties; 
  }
  
  public String[] getStates() { 
    return this.states; 
  }
}
