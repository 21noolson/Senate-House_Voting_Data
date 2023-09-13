import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class CongressDataProcessor {
  
  // HashTable containing all Members using their memberID as the hashtable key
  static Hashtable<String, Member> MemberID;
  // Arraylist containing all Congressional Actions 
  static ArrayList<ArrayList<CongressAction>> congressActions;
  // Arraylists containing all Members sorted by first and last name respectively
  static ArrayList<Member> MemberFirstName;
  static ArrayList<Member> MemberLastName;
  
  /**
   * Loads data located in All_Actions.csv and All_Members.csv
   * 
   * @return True if all data was loaded and False otherwise
   */
  public static boolean loadData() {
    // Create the Files where the data is located
    File actionFile = new File("Program_Data\\All_Actions.csv");
    File memberFile = new File("Program_Data\\All_Members.csv");
    
    // Try to load and sort member Data
    try {
      ArrayList<Member> Members = memberLoad(memberFile);
      memberDataSort(Members); // Sort and add the memberData
    }
    // If a FileNotFoundException is throw print the stacktrace and return false
    catch(FileNotFoundException e) {
      System.out.println("Failure to load File");
      e.printStackTrace();
      return false;
    }
    
    // Try to load action Data
    try {
      congressActions = actionLoad(actionFile);
    }
    // If a FileNotFoundException is throw print the stacktrace and return false
    catch(FileNotFoundException e) {
      System.out.println("Failure to load File");
      e.printStackTrace();
      return false;
    }
    
    
    return true;
  }
  
  /**
   * Takes an ArrayList of CongressPerson's and adds the data to the first and 
   *      last name sorted ArrayLists and the hashtable
   * @param MemberRandom Arraylist of congressPerson's being added
   */
  private static void memberDataSort(ArrayList<Member> MemberRandom) {
    // Add all members to the hashtable
    for(Member member: MemberRandom) {
      MemberID.put(member.getCongress().getMemberID(), member);
    }
    
    // Copy the array to the first and last name arrays
    MemberFirstName.addAll(MemberRandom);
    MemberLastName.addAll(MemberRandom);
    // Sort the arrays
    MemberFirstName.sort(new firstNameCompartor<Member>());
    MemberLastName.sort(new lastNameCompartor<Member>()); 
  }
  
  /**
   * Loads all of the CongressActions
   * 
   * @param file
   * @return
   * @throws FileNotFoundException
   */
  private static ArrayList<ArrayList<CongressAction>> actionLoad(File file) throws FileNotFoundException {
    // Scanner for actionFile and 'line' to hold the current line of the file
    Scanner scanner = new Scanner(file);
    String line;
    
    // List to hold all actions
    ArrayList<ArrayList<CongressAction>> allActions = new ArrayList<ArrayList<CongressAction>>();
    ArrayList<CongressAction> actions = new ArrayList<CongressAction>();
    
    while(scanner.hasNextLine()) {
      // Grab and split the line into sections
      line = scanner.nextLine();
      String[] actionInfo = line.split(",");
      
      // Grabs the congressSession and the last congressSession
      String congressSession = actionInfo[3]+actionInfo[4];
      CongressAction lastAction = actions.get(actions.size());
      String lastCongressSession = String.valueOf(lastAction.getCongress()) + String.valueOf(lastAction.getSession());
      if(!congressSession.equals(lastCongressSession)) {
        allActions.add(actions);
        actions = new ArrayList<CongressAction>();
      }
      actions.add(grabActionLine(actionInfo));
    }
    scanner.close();
    return allActions;
  }
  
  /**
   * Given a String[] of a line representing an action and creates a CongressAction
   * 
   * @param actionInfo String[] representing each item in a line holding CongressActions
   * @return  CongressAction created with the given actionInfo
   */
  private static CongressAction grabActionLine(String[] actionInfo) {
    // Grab data about the action
    int yayVotes = Integer.valueOf(actionInfo[0]);
    int nayVotes = Integer.valueOf(actionInfo[1]);
    int absent = Integer.valueOf(actionInfo[2]);
    int congress = Integer.valueOf(actionInfo[3]);
    int session = Integer.valueOf(actionInfo[4]);
    int year = Integer.valueOf(actionInfo[5]);
    String date = actionInfo[6];
    String voteQuestion = actionInfo[7];
    
    // Create ArrayList's for memberID and votes and add the related data
    ArrayList<String> memberID = new ArrayList<String>();
    ArrayList<Boolean> vote = new ArrayList<Boolean>();
    for(int i=8; i<actionInfo.length; i++) {
      if(i%2==0) {
        memberID.add(actionInfo[i]);
      }
      else {
        vote.add(Boolean.valueOf(actionInfo[i]));
      }
    }
    // Grab the lists of members and votes
    Member[] members = new Member[memberID.size()];
    Boolean[] votes = new Boolean[vote.size()];
    for(int i=0; i<memberID.size(); i++) {
      members[i] = MemberID.get(memberID.get(i));
      votes[i] = vote.get(i); 
    }
    
    // Return the new created action
    return new CongressAction(yayVotes, nayVotes, absent, congress, session, year,
                date, voteQuestion, members, votes);
  }
  
  private static ArrayList<Member> memberLoad(File file) throws FileNotFoundException {
    // Scanner for memberFile and line to hold the current line of the file
    Scanner scanner = new Scanner(file);
    String line;
    
    // List to hold the congressPeople
    ArrayList<Member> members = new ArrayList<Member>();
    
    while(scanner.hasNextLine()) {
      // Gets the next line and splits it by commas
      line = scanner.nextLine();
      String[] memberInfo = line.split(",");
      
      // number of Sessions member served on
      int numSess = (memberInfo.length-3)/3;
      // Arrays holding session, party, and state data
      String[] memberSessions = new String[numSess];
      Character[] memberParties = new Character[numSess];
      String[] memberStates = new String[numSess];
      
      // Loop through the line data and sort session, party, and state 
      //    data into their appropriate array
      for(int i=3; i<memberInfo.length; i++) {
        if(i < numSess+3) {
          memberSessions[i-3] = memberInfo[i];
        }
        else if(i < 2*numSess+3) {
          memberParties[i-3-numSess] = memberInfo[i].charAt(0);
        }
        else {
          memberStates[i-3-2*numSess] = memberInfo[i];
        }
      }
      Member member = new Member(memberInfo[0], memberInfo[1]);
      for(int i=0; i<memberSessions.length; i++)
      member.addCongressSession(memberInfo[2], memberSessions[i], memberParties[i], memberStates[i]);
      members.add(member);
    }
    scanner.close();
    return members;
  }
  
}
