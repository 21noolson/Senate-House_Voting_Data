
public class CongressPerson {
  String memberID; // Congress assigned Member ID
  String firstName;
  String lastName;
  String[] congressSessions; // List of congressional sessions CongressPerson was/is apart of
  Character[] parties; // List of party affiliations for each congressional Session
  String[] states; // List of states represented for each congressional Session
  public int length;
  
  /**
   * @param firstName
   * @param lastName
   * @param memberID Congress assigned Member ID
   */
  public CongressPerson(String memberID, String firstName, String lastName) {
    this(memberID, firstName, lastName, new String[0], new Character[0], new String[0]);
  }
  
  /**
   * 
   * @param firstName
   * @param lastName
   * @param memberID Congress assigned Member ID
   * @param congressSession congressional session the CongressPerson was/is apart of.
   *        Formated as Session-Congress; #-###
   * @param party party affiliation during congressional Session
   * @param state state represented for congressional Session
   */
  public CongressPerson(String memberID, String firstName, String lastName, 
      String congressSession, Character party, String state) {
    this(memberID, firstName, lastName);
    if(congressSession == null || party == null || state == null) {
      throw new IllegalArgumentException("Argument cann't be null");
    }
    this.congressSessions = new String[1];
    this.parties = new Character[1];
    this.states = new String[1];
    this.congressSessions[0] = congressSession;
    this.parties[0] = party;
    this.states[0] = state;
    this.length = 1;
  }
  
  /**
   * @param firstName
   * @param lastName
   * @param memberID Congress assigned Member ID
   * @param congressSessions congressional sessions the CongressPerson 
   *        was/is apart of. Formated as Session-Congress; #-###
   * @param party party affiliations during each congressional Session
   * @param state states represented for each congressional Session
   */
  public CongressPerson(String memberID, String firstName, String lastName, 
      String[] congressSessions, Character[] parties, String[] states) {
    if(memberID == null || firstName == null || lastName == null || congressSessions == null
        || parties == null || states == null) {
      throw new IllegalArgumentException("Argument cann't be null");
    }
    this.firstName = firstName;
    this.lastName = lastName;
    this.memberID = memberID;
    this.congressSessions = congressSessions;
    this.parties = parties;
    this.states = states;
    this.length = 1;
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
	  
	  // Adds old of the current data to the new Arrays
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
  
  public boolean combinePerson(CongressPerson otherPerson) {
    // If the two CongressPersons are not the same RETURN FALSE
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
      // OTHERWISE increment length and create new Arrays
      this.length++;
      String[] newSessions = new String[this.length];
      Character[] newParties = new Character[this.length];
      String[] newStates = new String[this.length];
      // Add the old array data to the newArrays and set the old array variables to the new array
      for(int j=0; j<this.length-1; j++) {
        newSessions[j] = this.congressSessions[j];
        newParties[j] = this.parties[j];
        newStates[j] = this.states[j];
      }
      this.congressSessions = newSessions;
      this.parties = newParties;
      this.states = newStates;
      // Add the new data to the arrays
      this.congressSessions[this.length-1] = otherSession;
      this.parties[this.length-1] = otherPerson.getParties()[i];
      this.states[this.length-1] = otherPerson.getStates()[i];
    }
    return true;
  }
  
  @Override
  public boolean equals(Object o) {
    // If the object isn't the same class RETURN FALSE
    if(!(o instanceof CongressPerson)) { return false; }
    // Cast the object to a CongressPerson
    CongressPerson person = (CongressPerson) o;
    // If the CongressPerson has a different MemberID, firstName, or lastName RETURN FALSE
    if(!(this.memberID.equals(person.memberID))) { return false; }
    if(!(this.firstName.equals(person.firstName))) { return false; }
    if(!(this.lastName.equals(person.lastName))) { return false; }
    return true; // Return true if all the data checks were the same
  }
  
  public String getName() {
    String name = this.firstName + " " + this.lastName;
    return name;
  }
  
  public String getFirstName() { return this.firstName; }
  
  public String getLastName() { return this.lastName; }
  
  public String getMemberID() { return this.memberID; }
  
  public String[] getCongressSessions() { return this.congressSessions; }
  
  public Character[] getParties() { return this.parties; }
  
  public String[] getStates() { return this.states; }
}
