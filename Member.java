
public class Member {
  String firstName;
  String lastName;
  CongressMember congress;
  
  
  public Member(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
  
  public void addCongressSession(String memberID, String session, Character party, String state) {
    CongressMember tempMember = new CongressMember(memberID);
    tempMember.addSession(session, party, state);
    if(congress == null) {
      congress = tempMember;
    }
    else if(!congress.combineMember(tempMember)) {
      throw new RuntimeException("The given memberID does not match the memberID previosly recorded");
    }
  }
  
  public boolean combineMember(Member otherMember) {
    // If the two Members are not the same RETURN FALSE
    if(!this.equals(otherMember)) { return false; }
    this.congress.combineMember(otherMember.getCongress());
    
    // TODO Combine Senate member
    
    return true;
  }
  
  /**
   * Two Members are considered equal if and only if their
   *     firstName, lastName, and memberID are the same
   */
  @Override
  public boolean equals(Object o) {
    // Return false if the object is not Member type otherwise cast to Member
    if(!(o instanceof Member)) { return false; }
    Member member = (Member) o;
    
    // If the Member has a different firstName or lastName RETURN FALSE
    if(!(this.firstName.equals(member.firstName))) { return false; }
    if(!(this.lastName.equals(member.lastName))) { return false; }
    
    // Checks if the congressMember of each Object is equal
    CongressMember thisMember = this.congress;
    CongressMember otherMember = member.getCongress();
    if(thisMember != null && otherMember != null && !otherMember.equals(thisMember)) {
      return false;
    }
    
    // Checks if the senateMember of each Object is equal
    // TODO
    
    return true; // Return true if all the data checks were the same
  }
  
  public CongressMember getCongress() {
    return congress;
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
}
