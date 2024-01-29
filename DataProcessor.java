import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Scanner;

public class DataProcessor {
  // File Locations
  final static String ProgramDataLocation = "Data\\ProgramData\\";
  final static File senateFile = new File(ProgramDataLocation + "All_Senate_Actions.csv");
  final static File houseFile = new File(ProgramDataLocation + "All_House_Actions.csv");
  final static File memberFile = new File(ProgramDataLocation + "All_Members.csv");
  
  // HashTable containing all Members using their memberID as the hashtable key
  static Hashtable<String, Member> senateMemberID;
  static Hashtable<String, Member> houseNameID;
  // Arraylist containing all Congressional Actions 
  static ArrayList<SenateAction> senateActions;
  static ArrayList<HouseAction> houseActions;
  // Arraylists containing all Members sorted by first and last name respectively
  static ArrayList<Member> MemberFirstName;
  static ArrayList<Member> MemberLastName;
  

  /**
   * Loads all the data
   * 
   * @throws FileNotFoundException
   * @return True if all data was loaded and False otherwise
   */
  public static void loadData() throws FileNotFoundException {
    loadMemberData();
    loadSenateData();
    loadHouseData();
    dataSort();
  }
  
  @SuppressWarnings("unchecked")
  private static void dataSort() {
    // Grabs list of house and senate members
    Collection<Member> senateMembers = senateMemberID.values();
    Collection<Member> houseMembers = houseNameID.values();
    
    // Adds senate Members to list of all Members.
    // Adds or combines members from house list.
    ArrayList<Member> allMembers = new ArrayList<Member>(senateMembers);
    for(Member member: houseMembers) {
      int memberLocation = allMembers.indexOf(member);
      if(memberLocation == -1) {
        allMembers.add(member);
      }
      else {
        allMembers.get(memberLocation).combineMember(member);
      }
    }
    
    // Sort Member list
    allMembers.sort(new firstNameCompartor());
    MemberFirstName = (ArrayList<Member>) allMembers.clone();
    allMembers.sort(new lastNameCompartor());
    MemberLastName = (ArrayList<Member>) allMembers.clone();
  }
  
  /**
   * Loads data from memberFile
   * 
   * @throws FileNotFoundException
   */
  private static void loadMemberData() throws FileNotFoundException {
    // Split the file by line
    Scanner memberScanner  = new Scanner(memberFile);
    memberScanner.useDelimiter("\n");
    
    while(memberScanner.hasNext()) {
      // Grab line and split it by commas
      String nextLine = memberScanner.next();
      String[] memberData = nextLine.split(",");
      
      // Add Member Name
      Member member = new Member(memberData[0], memberData[1]);
      
      // Adds all House Data then Name ID
      int i;
      for(i = 2; !memberData[i].equals("&&"); i+=3) {
        member.addHouseSession(memberData[i], memberData[i+1].charAt(0), memberData[i+2]);
      }
      String nameID = memberData[i+1];
      if(nameID != null) {
        member.setNameID(nameID);
        houseNameID.put(nameID, member);
      }
      
      // Adds all Senate Data then Member ID
      for(i = i+2; !memberData[i].equals("&&"); i+=3) {
        member.addSenateSession(memberData[i], memberData[i+1].charAt(0), memberData[i+2]);
      }
      String memberID = memberData[i+1];
      if(memberID != null) {
        member.setNameID(memberID);
        senateMemberID.put(memberID, member);
      }
    }
    memberScanner.close();
  }
  
  /**
   * Loads Data from senateFile
   * 
   * @throws FileNotFoundException
   */
  private static void loadSenateData() throws FileNotFoundException {
    // Split the file by line
    Scanner senateScanner  = new Scanner(senateFile);
    senateScanner.useDelimiter("\n");
    
    while(senateScanner.hasNext()) {
      // Grab line and split it by commas
      String nextLine = senateScanner.next();
      String[] actionData = nextLine.split(",");
      
      // Creates arrays to hold members and votes
      int numMembers = (actionData.length-8)/2;
      Member[] members = new Member[numMembers];
      Boolean[] votes = new Boolean[numMembers];
      
      // Grabs all the members and votes
      for(int i=8; i<numMembers; i+=2) {
        members[(i-8)/2] = senateMemberID.get(actionData[i]);
        String vote = actionData[i+1];
        if(vote.equals("YAY")) {
          votes[((i-8)/2)+1] = true;
        }
        else if(vote.equals("NAY")) {
          votes[((i-8)/2)+1] = false;
        }
        // Prints a message if the vote data is corrupt
        else if(!vote.equals("NULL")) {
          System.out.println("Failure when loading member ID:\t" + actionData[i]);
          System.out.println("Vote Entry Returned:\t" + actionData[i+1]);
        }
      }
      
      // Adds the action to senateActions
      SenateAction action = new SenateAction(Integer.getInteger(actionData[0]), Integer.getInteger(actionData[1]),
          Integer.getInteger(actionData[2]), Integer.getInteger(actionData[3]), Integer.getInteger(actionData[4]),
          Integer.getInteger(actionData[5]), actionData[6], actionData[7], members, votes);
      senateActions.add(action);
    }
    senateScanner.close();
  }
  
  /**
   * Loads Data from houseFile
   * 
   * @throws FileNotFoundException
   */
  private static void loadHouseData() throws FileNotFoundException {
    // Split the file by line
    Scanner senateScanner  = new Scanner(houseFile);
    senateScanner.useDelimiter("\n");
    
    while(senateScanner.hasNext()) {
      // Grab line and split it by commas
      String nextLine = senateScanner.next();
      String[] actionData = nextLine.split(",");
      
      // Creates arrays to hold members and votes
      int numMembers = (actionData.length-6)/2;
      Member[] members = new Member[numMembers];
      Boolean[] votes = new Boolean[numMembers];
      
      // Grabs all the members and votes
      for(int i=6; i<numMembers; i+=2) {
        members[(i-6)/2] = houseNameID.get(actionData[i]);
        String vote = actionData[i+1];
        if(vote.equals("YAY")) {
          votes[((i-6)/2)+1] = true;
        }
        else if(vote.equals("NAY")) {
          votes[((i-6)/2)+1] = false;
        }
        // Prints a message if the vote data is corrupt
        else if(!vote.equals("NULL")) {
          System.out.println("Failure when loading member ID:\t" + actionData[i]);
          System.out.println("Vote Entry Returned:\t" + actionData[i+1]);
        }
      }
      
      // Adds the action to houseActions
      HouseAction action = new HouseAction(Integer.getInteger(actionData[0]), Integer.getInteger(actionData[1]),
          Integer.getInteger(actionData[2]), Integer.getInteger(actionData[3]), actionData[4], actionData[5],
          members, votes);
      houseActions.add(action);
    }
    senateScanner.close();
  }

  
}
