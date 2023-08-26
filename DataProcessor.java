import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class DataProcessor {
  
  static Hashtable<String, CongressPerson> congressMemberID;
  static ArrayList<ArrayList<Actions>> congressActions;
  static ArrayList<CongressPerson> congressMemberFirstName;
  static ArrayList<CongressPerson> congressMemberLastName;
  
  public static boolean loadData() {
    // Create the Files where the data is located
    File actionFile = new File("All_Actions.csv");
    File memberFile = new File("All_Members.csv");
    Scanner memberScanner;
    try {
      // Load the array of congress Members unsorted
      ArrayList<CongressPerson> congressMemberRandom = memberLoad(memberFile);
      // Copy the array to the first and last name arrays
      congressMemberFirstName.addAll(congressMemberRandom);
      congressMemberLastName.addAll(congressMemberRandom);
      // Sort the arrays
      congressMemberFirstName.sort(new firstNameCompartor<CongressPerson>());
      congressMemberLastName.sort(new lastNameCompartor<CongressPerson>());
      
      // Add all members to the hashtable
      for(CongressPerson member: congressMemberRandom) {
        congressMemberID.put(member.getMemberID(), member);
      } 
    }
    catch(FileNotFoundException e) {
      System.out.println("Failure to load File");
      e.printStackTrace();
      return false;
    }
    try {
      ArrayList<ArrayList<Actions>> actions = actionLoad(actionFile);
    }
    catch(FileNotFoundException e) {
      return false;
    }
    return true;
  }
  
  private static ArrayList<ArrayList<Actions>> actionLoad(File file) throws FileNotFoundException {
    // Scanner for memberFile and line to hold the current line of the file
    Scanner scanner = new Scanner(file);
    String line;
    
    // List to hold all actions
    ArrayList<ArrayList<Actions>> allActions = new ArrayList<ArrayList<Actions>>();
    ArrayList<Actions> actions = new ArrayList<Actions>();
    
    while(scanner.hasNextLine()) {
      line = scanner.nextLine();
      String[] actionInfo = line.split(",");
      String congressSession = actionInfo[3]+actionInfo[4];
      Actions lastAction = actions.get(actions.size());
      String lastCongressSession = String.valueOf(lastAction.getCongress()) + String.valueOf(lastAction.getSession());
      if(congressSession.equals(lastCongressSession)) {
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
        
        // Grab the lists
        CongressPerson[] members = new CongressPerson[memberID.size()];
        Boolean[] votes = new Boolean[vote.size()];
        for(int i=0; i<memberID.size(); i++) {
          members[i] = congressMemberID.get(memberID.get(i));
          votes[i] = vote.get(i); 
        }
        actions.add(new Actions(yayVotes, nayVotes, absent, congress, session, year,
                    date, voteQuestion, members, votes));
      }
      else {
        
      }
    }
  }
  
  private static ArrayList<CongressPerson> memberLoad(File file) throws FileNotFoundException {
    // Scanner for memberFile and line to hold the current line of the file
    Scanner scanner = new Scanner(file);
    String line;
    
    // List to hold the congressPeople
    ArrayList<CongressPerson> members = new ArrayList<CongressPerson>();
    
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
      CongressPerson member = new CongressPerson(memberInfo[0], memberInfo[1], memberInfo[2],
                                  memberSessions, memberParties, memberStates);
      members.add(member);
    }
    scanner.close();
    return members;
  }
}
