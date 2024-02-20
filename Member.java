
public class Member {
  String firstName;
  String lastName;
  
  String nameID; // House assigned nameID
  String[] houseSessions; // List of congressional sessions CongressPerson was/is apart of
  Character[] houseParties; // List of party affiliations for each congressional Session
  String[] houseStates; // List of states represented for each congressional Session
  
  String memberID; // Congress assigned Member ID
  String[] senateSessions; // List of congressional sessions congressMember was/is apart of
  Character[] senateParties; // List of party affiliations for each congressional Session
  String[] senateStates; // List of states represented for each congressional Session
  
  /**
   * @param firstName
   * @param lastName
   */
  public Member(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    
    this.nameID = "";
    this.houseSessions = new String[0];
    this.houseParties = new Character[0];
    this.houseStates = new String[0];
    
    this.memberID = "";
    this.senateSessions = new String[0];
    this.senateParties = new Character[0];
    this.senateStates = new String[0];
  }
  
  public void setNameID(String nameID) {
    this.nameID = nameID;
  }
  
  public void setMemberID(String memberID) {
    this.memberID = memberID;
  }
  
  /**
   * Adds a house session
   * 
   * @param session the HouseMember was/is apart of. Formated as Session-Congress; #-###
   * @param party affiliation during house Session
   * @param state represented for house Session 
   */
  public void addHouseSession(String session, Character party, String state) {
    // Throws IllegalArgumentException if one of the parameters is null
    if(session == null || party == null || state == null) {
      throw new IllegalArgumentException();
    }
    
    // Creates new Lists to add the Session to
    int sessions = houseSessions.length;
    String[] newSessions = new String[sessions+1];
    Character[] newParties = new Character[sessions+1];
    String[] newStates = new String[sessions+1];
    
    // Adds the current data to the new Arrays
    for(int i=0; i<sessions; i++) {
      newSessions[i] = houseSessions[i];
      newParties[i] = houseParties[i];
      newStates[i] = houseStates[i];
    }
    
    // Adds the new data to the new Arrays
    newSessions[sessions] = session;
    newParties[sessions] = party;
    newStates[sessions] = state;
    
    // Assigns the new Arrays to the appropriate variables in the object
    houseSessions = newSessions;
    houseParties = newParties;
    houseStates = newStates;
    
    // Sets Name and Member ID to Null
    nameID = null;
    memberID = null;
  }
  
  /**
   * Adds a congressional session
   * 
   * @param session congressMember was/is apart of. Formated as Session-Congress; #-###
   * @param party affiliation during congressional Session
   * @param state represented for congressional Session 
   */
  public void addSenateSession(String session, Character party, String state) {
    // Throws IllegalArgumentException if one of the parameters is null
    if(session == null || party == null || state == null) {
      throw new IllegalArgumentException();
    }
    
    // Create new Arrays for sessions, party, and state with incremented length
    int sessions = senateSessions.length;
    String[] newSessions = new String[sessions+1];
    Character[] newParties = new Character[sessions+1];
    String[] newStates = new String[sessions+1];
    
    // Adds the current data to the new Arrays
    for(int i=0; i<senateSessions.length; i++) {
      newSessions[i] = senateSessions[i];
      newParties[i] = senateParties[i];
      newStates[i] = senateStates[i];
    }
    
    // Adds the new data to the new Arrays
    newSessions[sessions] = session;
    newParties[sessions] = party;
    newStates[sessions] = state;
    
    // Assigns the new Arrays to the appropriate variables in the object
    senateSessions = newSessions;
    senateParties = newParties;
    senateStates = newStates;
  }
  
  /**
   *  Combines two Member's into this if they are equal
   * 
   * @param otherMember the other Member to be combined with
   * @return true if the combination was successful and false if it wasn't; IE the people were not equal
   */
  public boolean combineMember(Member otherMember) {
    // If the two Members are not the same RETURN FALSE
    if(!this.equals(otherMember)) { return false; }
    
    // Adds all session data from otherPerson this CongressMember
    for(int i=0; i<otherMember.getSenateLength(); i++) {
      String otherSession = otherMember.getSenateSessions()[i];
      if(!this.hasSenateSession(otherSession)) {
        this.addSenateSession(otherSession, otherMember.getSenateParties()[i], otherMember.getSenateStates()[i]);
      }
    }
    
    // Adds all session data from otherPerson this HouseMember
    for(int i=0; i<otherMember.getHouseLength(); i++) {
      String otherSession = otherMember.getHouseSessions()[i];
      if(!this.hasHouseSession(otherSession)) {
        this.addHouseSession(otherSession, otherMember.getHouseParties()[i], otherMember.getHouseStates()[i]);
      }
    }
    
    // Return True after combining the two Member's
    return true;
  }
  
  /**
   * Determines if this CongressMember was apart of the specified Session
   * 
   * @param Session Congressional Session to check for
   * @return true if the CongressMember was apart of the specified Session
   */
  public boolean hasSenateSession(String Session) {
    // Loops through all the stored sessions in the current congressMember
    for(int j=0; j<this.getSenateLength(); j++) {
      String otherSession = this.senateSessions[j];
      // If the session is found mark appropriate variable and break the loop
      if(otherSession.equals(Session)) {
        return true; 
      }
    }
    // If the session wasn't found return false
    return false;
  }
  
  /**
   * Determines if this HouseMember was apart of the specified Session
   * 
   * @param Session HouseSession to check for. Formated as Session-Congress; #-###
   * @return true if the HouseMember was apart of the specified Session
   */
  public boolean hasHouseSession(String Session) {
    // Loops through all the stored sessions in the current houseMember
    for(int j=0; j<this.getHouseLength(); j++) {
      String otherSession = this.houseSessions[j];
      // If the session is found mark appropriate variable and break the loop
      if(otherSession.equals(Session)) {
        return true; 
      }
    }
    // If the session wasn't found return false
    return false;
  }
  
  /**
   * Two Members are considered equal if and only if their firstName, lastName, memberID, and nameID are the same
   */
  @Override
  public boolean equals(Object o) {
    // Return false if the object is not MemberFULL type otherwise cast to MemberFULL
    if(!(o instanceof Member)) { return false; }
    Member member = (Member) o;
    
    // Checks first and last Name
    if(!(this.firstName.equals(member.firstName))) { return false; }
    if(!(this.lastName.equals(member.lastName))) { return false; }
    // Checks Name and Member ID
    if(!(this.nameID.equals(member.getNameID()))) {return false; }
    if(!(this.memberID.equals(member.getMemberID()))) { return false; }
    
    // Return true if all the data checks were the same
    return true; 
  }
  
  public String getName() {
    String name = this.firstName + " " + this.lastName;
    return name;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public String getLastName() { 
    return this.lastName;
  }
  
  public String getNameID() {
    return this.nameID; 
  }
  
  public String[] getHouseSessions() {
    return this.houseSessions;
  }
  
  public Character[] getHouseParties() { 
    return this.houseParties; 
  }
  
  public String[] getHouseStates() { 
    return this.houseStates; 
  }

  public String getMemberID() {
    return this.memberID; 
  }
  
  public String[] getSenateSessions() {
    return this.senateSessions;
  }
  
  public Character[] getSenateParties() { 
    return this.senateParties; 
  }
  
  public String[] getSenateStates() { 
    return this.senateStates; 
  }
  
  public int getHouseLength() {
    return this.getHouseSessions().length;
  }
  
  public int getSenateLength() {
    return this.senateSessions.length;
  }
  
}
