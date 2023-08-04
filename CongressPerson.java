
public class CongressPerson {
  String memberID; // Congress assigned Member ID
  String firstName;
  String lastName;
  String[] congressSessions; // List of congressional sessions CongressPerson was/is apart of
  Character[] parties; // List of party affiliations for each congressional Session
  String[] states; // List of states represented for each congressional Session
  
  /**
   * @param firstName
   * @param lastName
   * @param memberID Congress assigned Member ID
   */
  public CongressPerson(String memberID, String firstName, String lastName) {
    this(firstName, lastName, memberID, new String[0], new Character[0], new String[0]);
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
    this(firstName, lastName, memberID);
    this.congressSessions = new String[1];
    this.parties = new Character[1];
    this.states = new String[1];
    this.congressSessions[0] = congressSession;
    this.parties[0] = party;
    this.states[0] = state;
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
    this.firstName = firstName;
    this.lastName = lastName;
    this.memberID = memberID;
    this.congressSessions = congressSessions;
    this.parties = parties;
    this.states = states;
  }
  
  public String getName() {
    String name = this.firstName + " " + this.lastName;
    return name;
  }
  
  public String getFirstName() { return this.firstName; }
  
  public String getLastName() { return this.lastName; }
  
  public String memberID() { return this.memberID; }
  
  public String[] getCongressSessions() { return this.congressSessions; }
  
  public Character[] getParties() { return this.parties; }
  
  public String[] getStates() { return this.states; }
}
