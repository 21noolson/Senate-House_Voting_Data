import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CongressDataConversion {
  static final String FileLocation = "Senate_Voting_Data"; 
  static final String comma = ", ";
  static ArrayList<Member> congressPeople = new ArrayList<Member>();
  
  /**
   * Gets all of the files in the specified folder
   * 
   * @param fileFolder the location of the folder with files
   * @return List of Files in the fileFolder
   */
  private static File[] getFiles(String fileFolder) {
    File folder = new File(fileFolder);
    return folder.listFiles();
  }
  
  /**
   * Uses the provided BufferedWriter to write data about the given action. 
   *      Adds all data except memberID and votes
   * 
   * @param actionWriter is the writer to use to write data
   * @param action is the action to be recorded
   * @throws IOException
   */
  private static void addActionData(BufferedWriter actionWriter, CongressAction action) throws IOException{
    // Adds all the data except for the member votes to the Action File
    /* Data Formated as Follows:
     *      int yayVotes, int nayVotes, int absent, int congress, int session, int year,
     *      String date, String voteQuestion, 
     */
    actionWriter.append((action.getYayVotes() + comma));
    actionWriter.append((action.getNayVotes() + comma));
    actionWriter.append((action.getAbsent() + comma));
    actionWriter.append((action.getCongress() + comma));
    actionWriter.append((action.getSession() + comma));
    actionWriter.append((action.getYear() + comma));
    actionWriter.append((action.getDate() + comma));
    actionWriter.append((action.getVoteQuestion() + comma));
  }
  
  /**
   * Uses the provided BufferedWriter to write the voter and vote data. Also records the
   *      voter data to be written to a different file later
   * 
   * @param actionWriter is the writer to use to write the data
   * @param voter 
   * @param vote 
   * @throws IOException
   */
  private static void addMemberData(BufferedWriter actionWriter, Member voter, Boolean vote) throws IOException{
    // Adds the voter's memberID and vote to AllActions.csv
    actionWriter.append(voter.getCongress().getMemberID() + comma);
    actionWriter.append(vote + comma);
    
    //  Gets the location of the voter in the index of CongressPeople
    int location = congressPeople.indexOf(voter);
    //  If the voter isn't in the list add it to the list
    if(location == -1) {
      congressPeople.add(voter);
    }
    // If the voter is in the list add the information to the CongressPerson at the found location
    else {
      congressPeople.get(location).combineMember(voter);
    }
  }

  /**
   * Uses the given BufferedWriter to record all of the action data and create a list of all the voters
   * 
   * @param actionWriter is the writer used to write the data
   * @param dataFile is the file with data we are looking to write
   * @throws IOException
   */
  private static void addData(BufferedWriter actionWriter, File dataFile) throws IOException{
    
    // Declare the SAX factory, Parser and Handler for processing data
    SAXParserFactory factory = null;
    SAXParser saxParser = null;
    CongressVoteHandler handler = null;
    // Create SAX Parser and parse the given dataFile
    try {
      factory = SAXParserFactory.newInstance();
      saxParser = factory.newSAXParser();
      handler = new CongressVoteHandler();
      saxParser.parse(dataFile, handler);
    }
    // Print Exception information if parser Fails initialization and returns
    catch(Exception e) {
      System.out.println("Failed to intialize SAX Factory, Parser, or handler");
      e.printStackTrace();
      return;
    }
    
    // Grabs the action from the data parser
    CongressAction action = handler.getAction();
    
    // Adds the action data
    addActionData(actionWriter, action);
    
    // Gets the voter and vote data
    Member[] voters = action.getVoters();
    Boolean[] votes = action.getVotes();
    
    // For each voter adds the voter and vote information
    for(int i=0; i<voters.length; i++) {
      Member voter = voters[i];
      Boolean vote = votes[i];
      addMemberData(actionWriter, voter, vote);
    }
    
    // Moves to the next line of the action file
    actionWriter.append("\n");
  }
    
	public static void main(String[] args) {
	  // Creates new empty Files where we will store information
    File actionFile = new File("Program_Data\\All_Actions.csv");
    File memberFile = new File("Program_Data\\All_Members.csv");
    try {
      actionFile.delete();
      memberFile.delete();
      actionFile.createNewFile();
      memberFile.createNewFile();
    } 
    // Exception if the files couldn't be created
    catch (IOException e) {
      System.out.println("Files failed to initialize");
      e.printStackTrace();
    }
    
    // Creates BufferedWriters for the two files
    BufferedWriter actionWriter = null;
    BufferedWriter memberWriter = null;
    try {
      actionWriter = new BufferedWriter(new FileWriter(actionFile));
      memberWriter = new BufferedWriter(new FileWriter(memberFile));
    }
    // Exception if the bufferedWriters failed to initialize
    catch(IOException e) {
      System.out.println("BufferedWriter failed to initialize");
      e.printStackTrace();
    }
    
	  // Gets all of the files information must be pulled from
		File[] allFiles = getFiles(FileLocation);
		// Writes the data for each file except a temporary File
		for(File file: allFiles) {
		  if(!file.getName().equals("tempFile.xml")) {
		    System.out.println(file);
		    try {
		      addData(actionWriter, file);
		    }
		    catch(IOException e) {
		      e.printStackTrace();
		    }
		  }
		}
		
		// Writes all the memberData to the member File
		for(Member member:congressPeople) {
		  try {
		    CongressMember congressMember = member.getCongress();
  		  // Adds the memberID, firstName, and lastName
  		  memberWriter.append(congressMember.getMemberID() + comma);
  		  memberWriter.append(member.getFirstName() + comma);
  		  memberWriter.append(member.getLastName() + comma);
  		  
  		  
  		  // Grabs the session, party, and state data 
  		  String[] sessions = congressMember.getCongressSessions();
  		  Character[] parties = congressMember.getParties();
  		  String[] states = congressMember.getStates();
  		  
  		  // Adds all session data, all party data, and all state data
  		  for(int i=0; i<congressMember.length; i++) {
  		    memberWriter.append(sessions[i] + comma);
  		  }
  		  for(int i=0; i<congressMember.length; i++) {
          memberWriter.append(parties[i] + comma);
        }
  		  for(int i=0; i<congressMember.length; i++) {
          memberWriter.append(states[i] + comma);
        }
  		  
  		  
  		  // Moves to the next line
  		  memberWriter.append("\n");
		  }
		  
		  // Exception Thrown if data can't be written
		  catch(IOException e) {
		    System.out.println("Failed to write Member data to file");
		    e.printStackTrace();
		  }
		}
		// Closes both of the BufferedWriters
		try {
      actionWriter.close();
      memberWriter.close();
    } 
		catch (IOException e) {
      e.printStackTrace();
    }
		
	}

}
