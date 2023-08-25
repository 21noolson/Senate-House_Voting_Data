import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DataConversion {
  static final String FileLocation = "D:\\Senate and House Data and processing\\Senate Data"; 
  static final String comma = ", ";
  static ArrayList<CongressPerson> congressPeople = new ArrayList<CongressPerson>();
  
  private static File[] getFiles(String fileFolder) {
    File folder = new File(fileFolder);
    File[] listOfFiles = folder.listFiles();

    return listOfFiles;
  }
  
  private static void addActionData(BufferedWriter actionWriter, Actions action) throws IOException{
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
  
  private static void addMemberData(BufferedWriter actionWriter, CongressPerson voter, Boolean vote) throws IOException{
    // Adds the voter's memberID and vote to AllActions.csv
    actionWriter.append(voter.getMemberID() + comma);
    actionWriter.append(vote + comma);
    
    //  Gets the location of the voter in the index of CongressPeople
    int location = congressPeople.indexOf(voter);
    //  If the voter isn't in the list add it to the list
    if(location == -1) {
      congressPeople.add(voter);
    }
    // If the voter is in the index add the information to the CongressPerson at the found location
    else {
      congressPeople.get(location).combinePerson(voter);
    }

  }
  
  private static void addData(BufferedWriter actionWriter, File dataFile) throws IOException{
    
    // Declare the SAX factory, Parser and Handler for processing data
    SAXParserFactory factory = null;
    SAXParser saxParser = null;
    voteHandler handler = null;
    // Create SAX Parser and parse the given dataFile
    try {
      factory = SAXParserFactory.newInstance();
      saxParser = factory.newSAXParser();
      handler = new voteHandler();
      saxParser.parse(dataFile, handler);
    }
    // Print Exception information if parser Fails initialization and returns
    catch(Exception e) {
      System.out.println("Failed to intialize SAX Factory, Parser, or handler");
      e.printStackTrace();
      try {
        TimeUnit.SECONDS.sleep(60);
      } catch (InterruptedException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      return;
    }
    
    // Grabs the action from the data parser
    Actions action = handler.getAction();
    
    // Adds the action data
    addActionData(actionWriter, action);
    
    // Gets the voter and vote data
    CongressPerson[] voters = action.getVoters();
    Boolean[] votes = action.getVotes();
    
    // For each voter adds the voter and vote information
    for(int i=0; i<voters.length; i++) {
      CongressPerson voter = voters[i];
      Boolean vote = votes[i];
      addMemberData(actionWriter, voter, vote);
    }
    
    // Moves to the next line of the action file
    actionWriter.append("\n");
  }
    
	public static void main(String[] args) {
	  // Creates the Files where we will store information and Writers 
    File actionFile = new File("All_Actions.csv");
    File memberFile = new File("All_Members.csv");
    try {
      actionFile.delete();
      memberFile.delete();
      actionFile.createNewFile();
      memberFile.createNewFile();
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
    BufferedWriter actionWriter = null;
    BufferedWriter memberWriter = null;
    try {
      actionWriter = new BufferedWriter(new FileWriter(actionFile));
      memberWriter = new BufferedWriter(new FileWriter(memberFile));
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    
	  // Gets all of the files information must be pulled from
		File[] allFiles = getFiles(FileLocation);		
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
		for(CongressPerson member:congressPeople) {
		  try {
  		  // Adds the memberID, firstName, and lastName
  		  memberWriter.append(member.getMemberID() + comma);
  		  memberWriter.append(member.getFirstName() + comma);
  		  memberWriter.append(member.getLastName() + comma);
  		  // Grabs the session, party, and state data 
  		  String[] sessions = member.getCongressSessions();
  		  Character[] parties = member.getParties();
  		  String[] states = member.getStates();
  		  // Adds all session data, all party data, and all state data
  		  for(int i=0; i<member.length; i++) {
  		    memberWriter.append(sessions[i] + comma);
  		  }
  		  for(int i=0; i<member.length; i++) {
          memberWriter.append(parties[i] + comma);
        }
  		  for(int i=0; i<member.length; i++) {
          memberWriter.append(states[i] + comma);
        }
  		  // Moves to the next line
  		  memberWriter.append("\n");
		  }
		  catch(IOException e) {
		    e.printStackTrace();
		  }
		}
		try {
      actionWriter.close();
      memberWriter.close();
    } 
		catch (IOException e) {
      e.printStackTrace();
    }
		
	}

}
