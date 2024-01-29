import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;

import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;  


/** Class for downloading voting data from the United States Senate and House of Representatives
 *  Senate Data is approximately 12,000 files and 300MB
 *  House Data is approximately 21,000 files and 1.4GB
 * 
 * @author NoahOlson
 */
public class Data {
  static final String comma = ",";
  
  //First and Last Congress and House Year
  private static final int firstCongress = 101;
  private static final int lastCongress = 118;
  private static final int firstHouseYear = 1990;
  private static final int lastHouseYear = 2023;
  
  //Folder location of the unprocessedData and where the ProcessedData will go
  private static final String UnprocessedDataLocation = "Data\\DownloadData\\"; 
  private static final String ProcessedDataLocation = "Data\\ProgramData\\";
  // Folder where you will store your data. 
  private static final String SenateFileLocation = UnprocessedDataLocation + "Senate_Voting_Data\\"; 
  private static final String HouseFileLocation = UnprocessedDataLocation + "House_Voting_Data\\";
  // Creates new empty Files where we will store information
  private static final File senateActionFile = new File(ProcessedDataLocation + "All_Senate_Actions.csv");
  private static final File houseActionFile = new File(ProcessedDataLocation + "All_House_Actions.csv");
  private static final File memberFile = new File(ProcessedDataLocation + "All_Members.csv");
  
  // Declare the SAX factory and Parser 
  private static SAXParserFactory factory;
  private static SAXParser saxParser;
  // Declares Handlers for downloading data
  private static SenateVoteMenuHandler senateMenuHandler;
  private static HouseVoteHandler houseHandler;
  private static SenateVoteHandler senateHandler;
  // BufferedWriters for the writable files
  private static BufferedWriter senateActionWriter;
  private static BufferedWriter houseActionWriter;
  private static BufferedWriter memberWriter;
  
  // ArrayList to hold processed members
  static ArrayList<Member> members = new ArrayList<Member>();
  
  
  public static void main(String[] args) {
    intialize();
    downloadAllCongressData();
    downloadAllHouseData();
    processCongressData();
    processHouseData();
    processMemberData();
    // Closes both of the BufferedWriters
    try {
      senateActionWriter.close();
      houseActionWriter.close();
      memberWriter.close();
    } 
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Initializes the class before any of the other functions can be run
   * 
   * @return true if initialization was successful and false if it failed
   */
  public static boolean intialize() {
    try {
      // Initialize SAX Parser, Factory, and Handlers
      factory = SAXParserFactory.newInstance();
      saxParser = factory.newSAXParser();
      senateMenuHandler = new SenateVoteMenuHandler();
      houseHandler = new HouseVoteHandler();
      senateHandler = new SenateVoteHandler();
      
      // Clears Files
      senateActionFile.delete();
      memberFile.delete();
      senateActionFile.createNewFile();
      memberFile.createNewFile();
      
      // Intializes Writers
      senateActionWriter = new BufferedWriter(new FileWriter(senateActionFile));
      houseActionWriter = new BufferedWriter(new FileWriter(houseActionFile));
      memberWriter = new BufferedWriter(new FileWriter(memberFile));
      return true;
    }
    catch(Exception e) {
      return false;
    }
  }
   
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
   * Processes the Member Data
   * 
   * @return True if the data was processed and False otherwise
   */
  public static boolean processMemberData() {
    for(Member member:members) {
      try {
        // Adds first and last name to line
        String dataLine = member.getFirstName() + comma;
        dataLine = dataLine + member.getLastName() + comma;
        
        // Adds all House session info to line
        Character[] parties = member.getHouseParties();
        String[] sessions = member.getHouseSessions();
        String[] states = member.getHouseStates();
        int length = member.getHouseLength();
        for(int i=0; i<length; i++) {
          dataLine = dataLine + sessions[i] + comma + states[i] + comma + parties[i] + comma;
        }
        
        // Adds character to seperate House and Senate Data and Name ID
        dataLine = dataLine + "&&" + comma + member.getNameID() + comma;
        
        // Adds all Senate session info to line
        parties = member.getSenateParties();
        sessions = member.getSenateSessions();
        states = member.getSenateStates();
        length = member.getSenateLength();
        for(int i=0; i<length; i++) {
          dataLine = dataLine + sessions[i] + comma + states[i] + comma + parties[i] + comma;
        }
        // Adds charcter to indicate end and Member ID
        dataLine = dataLine + "&&" + comma + member.getMemberID();
        
        // Adds newLine character and writes the line to the dataFile
        dataLine = dataLine + "\n";
        memberWriter.append(dataLine);
      }
      catch (IOException e) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Processes the Congress Data
   * 
   * @return true if the data is successfully processed and false otherwise
   */
  public static boolean processCongressData() {
    // Gets all of the files information must be pulled from
    File[] allFiles = getFiles(UnprocessedDataLocation+"Senate_Voting_Data");
    // Writes the data for each file except a temporary File
    for(File file: allFiles) {
      if(!file.getName().equals("tempFile.xml")) {
        if(!writeCongressData(file)) {
          return false;
        }
      }
    }
    return true;
  }
  
  /**
   * Records all of the action data from the given dataFile to senateActionFile
   * 
   * @param dataFile 
   * @return true if the data is successfully written and false otherwise
   */
  private static boolean writeCongressData(File dataFile) {
   // Parse the given dataFile and return false if the parser fails
   try {
     saxParser.parse(dataFile, senateHandler);
   }
   catch(Exception e) {
     return false;
   }
   // Writes the Data to the senateActionFile
   SenateAction action = senateHandler.getAction();
   if(!writeCongressActionData(action)) {
     return false;
   }
   // Gets the voter and vote data
   Member[] voters = action.getVoters();
   Boolean[] votes = action.getVotes();
   // For each voter adds the voter and vote information
   for(int i=0; i<voters.length; i++) {
     if(!writeCongressMemberData(voters[i], votes[i])) {
       return false;
     }
   }
   return true;
 }
  
  /**
   * Writes all data except memberID and votes from the given action.to the seanteActionFile
   * 
   * @param action is the action to be recorded
   * @return true if the data was written and false if an exception occurred
   */
  private static boolean writeCongressActionData(SenateAction action) {
    try {
      String dataLine = action.getYayVotes() + comma;
      dataLine += action.getNayVotes() + comma;
      dataLine += action.getAbsent() + comma;
      dataLine += action.getCongress() + comma;
      dataLine += action.getSession() + comma;
      dataLine += action.getYear() + comma;
      dataLine += action.getDate() + comma;
      dataLine += action.getVoteQuestion() + comma;
      senateActionWriter.append(dataLine);
      return true;
    }
    catch (IOException e) {
      return false;
    }
  }
  
  /**
   * Writes voter memberID and vote to the seanteActionFile
   * 
   * @param voter 
   * @param vote 
   * @return true if the data was written and false otherwise
   */
  private static boolean writeCongressMemberData(Member voter, Boolean vote) {
    // Writes the Voter Data to the senateActionFile and returns false if an IOException is thrown
    String dataLine = voter.getMemberID() + comma + vote + "\n";
    try {
      senateActionWriter.append(dataLine);
    }
    catch(IOException e) {
      return false;
    }
    // Add Voter Data to members
    int location = members.indexOf(voter);
    if(location == -1) {
      members.add(voter);
    }
    else {
      members.get(location).combineMember(voter);
    }
    // Return true after the memberID and vote is written
    return true;
  }
  
  /**
   * Processes the Congress Data
   * 
   * @return true if the data is successfully processed and false otherwise
   */
  public static boolean processHouseData() {
    // Gets all of the files information must be pulled from
    File[] allFiles = getFiles(UnprocessedDataLocation+"House_Voting_Data");
    // Writes the data for each file except a temporary File
    for(File file: allFiles) {
      if(!writeHouseData(file)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Records all of the action data from the given dataFile to houseActionFile
   * 
   * @param dataFile
   * @return true if the data is successfully written and false otherwise
   */
  private static boolean writeHouseData(File dataFile){
    // Parse the given dataFile and return false if the parser fails
    try {
      saxParser.parse(dataFile, houseHandler);
    }
    // Print Exception information if parser Fails initialization and returns
    catch(Exception e) {
      return false;
    }
    // Writes the Data to the ctionFile
    HouseAction action = houseHandler.getAction();
    if(!writeHouseActionData(action)) {
      return false;
    }
    // Gets the voter and vote data
    Member[] voters = action.getVoters();
    Boolean[] votes = action.getVotes();
    // For each voter adds the voter and vote information
    for(int i=0; i<voters.length; i++) {
      if(!writeHouseMemberData(voters[i], votes[i])) {
        return false;
      }
    }
    // Moves to the next line of the action file
    try {
      senateActionWriter.append("\n");
    }
    catch(IOException e) {
      return false;
    }
    return true;
  }
  
  /**
   * Writes all data except memberID and votes from the given action.to the actionFile
   *
   * @param action to be recorded
   * @return true if the data was written and false if an exception occurred
   */
  private static boolean writeHouseActionData(HouseAction action) {
    try {
      String dataLine = action.getYayVotes() + comma;
      dataLine += action.getNayVotes() + comma;
      dataLine += action.getAbsent() + comma;
      dataLine += action.getYear() + comma;
      dataLine += action.getDate() + comma;
      dataLine += action.getVoteQuestion() + comma;
      houseActionWriter.append(dataLine);
      return true;
    }
    catch (IOException e) {
      return false;
    }
  }
  
  /**
   * Writes voter nameID and vote to the seanteActionFile
   * 
   * @param voter 
   * @param vote 
   * @return true if the data was written and false otherwise
   */
  private static boolean writeHouseMemberData(Member voter, Boolean vote) {
    // Writes the Voter Data to the senateActionFile and returns false if an IOException is thrown
    String dataLine = voter.getNameID() + comma + vote + comma;
    try {
      houseActionWriter.append(dataLine);
    }
    catch(IOException e) {
      return false;
    }
    // Add Voter Data to members
    int location = members.indexOf(voter);
    if(location == -1) {
      members.add(voter);
    }
    else {
      members.get(location).combineMember(voter);
    }
    // Return true after the memberID and vote is written
    return true;
  }
  
  /**
   * Downloads All House Data
   * 
   * @return true if all data successfully downloaded otherwise returns false
   */
  public static boolean downloadAllHouseData() {
    return downloadHouseData(firstHouseYear, lastHouseYear);
  }
  
  /**
   * Downloads House Data from the startYear up to the current year
   * 
   * @param startYear first year to start downloading data
   * @return true if all data successfully downloaded otherwise returns false
   */
  public static boolean downloadHouseData(int startYear) {
    return downloadHouseData(startYear, lastHouseYear);
  }
  
  /**
   * Downloads House Data from the startYear until endYear (Inclusive)
   * 
   * @param startYear first year to start downloading data
   * @param endYear last year to download data
   * @return true if all data successfully downloaded otherwise returns false
   */
  public static boolean downloadHouseData(int startYear, int endYear) {
    if(startYear < firstHouseYear || endYear > lastHouseYear || endYear < startYear) {
      throw new IndexOutOfBoundsException();
    }
    // Download all Years of house data and return false if any downloads fail
    for(int i=startYear; i<=endYear; i++) {
      if(!downloadHouseYearData(i)) {
        return false;
      }
    }
    return true; // Return true if all downloads are successful
  }
  
  /**
   * Downloads House Vote Data from year
   * 
   * @param year to download House Data
   * @return true if all data successfully downloaded otherwise returns false
   */
  private static boolean downloadHouseYearData(int year) {
    int rollCallNum = 0;
    String webPage;
    String fileLocation;
    do {
      // Creates String to hold webPage and fileLocation
      rollCallNum++; 
      String rollCallFormatted = Integer.toString(rollCallNum);
      while(rollCallFormatted.length() < 3) { rollCallFormatted = "0" + rollCallFormatted; }
      webPage = "https://clerk.house.gov/evs/" + year + "/roll" + rollCallFormatted + ".xml";
      fileLocation =  HouseFileLocation + "House_" + year + "_" + rollCallNum + ".xml";
    }
    // DownloadWebPage and move to the next if successful
    while(DownloadWebPage(webPage, fileLocation));
    return true; // Return TRUE when function concludes
  }
  
  /**
   * Downloads All Congress Data
   * 
   * @return true if all data successfully downloaded otherwise returns false
   */
  public static boolean downloadAllCongressData() {
   return downloadCongressData(firstCongress, lastCongress);
  }
  
  /**
   * Downloads Congress Data from the given startCongress up to the current Congress
   * 
   * @param startCongress the Congress number to begin the Download
   * @return true if all data successfully downloaded otherwise returns false
   */
  public static boolean downloadCongressData(int startCongress) {
     return downloadCongressData(startCongress, lastCongress);
  }
  
  /**
   * Downloads Congress data starting with the startCongress continuing until endCongress (Inclusive)
   *          
   * @param startCongress the congress number to begin with
   * @param endCongress the congress number to end with
   * @return true if all data successfully downloaded otherwise returns false
   */
  public static boolean downloadCongressData(int startCongress, int endCongress) {
    if(startCongress < firstCongress  || endCongress > lastCongress || startCongress > endCongress) {
      throw new IndexOutOfBoundsException();
    }
    // Loops through all current Congressional session which are on the senates website
    for(int congress=startCongress; congress<=endCongress; congress++) {
      for(int session=1; session<=2; session++) {
        if(!(congress == lastCongress && session == 2) && !downloadCongressSessionData(congress, session)) { 
          return false;
        }
      }
    }
    return true; // Returns true after all data successfully downloads
  }
  
  /**
   * Downloads all of the vote data for the given congress and session
   * 
   * @param congress congress number to be downloaded
   * @param session session number to be downloaded
   * @return true if all data successfully downloaded otherwise returns false
   */
  private static boolean downloadCongressSessionData(int congress, int session) {
    // Creates String to the senate menu page for the given congress and session
    String sessionsPage = "https://www.senate.gov/legislative/LIS/roll_call_lists/vote_menu_";
    sessionsPage += congress + "_" + session +".xml";
    
    // Creates file for voteMenu page
    String votePageFileLocation = SenateFileLocation + "tempData.xml";
    File voteMenu = new File(votePageFileLocation);
    
    // Attempts to download and parse voteMenu page and determine items otherwise prints exception and returns false
    int items = 0;
    try {
      if(!DownloadWebPage(sessionsPage, votePageFileLocation)) {
        return false;
      }
      saxParser.parse(voteMenu, senateMenuHandler);
      items = senateMenuHandler.getVotes();
    } 
    catch  (SAXException | IOException e) {
      System.out.print("Failed to download and parse vote menu for ");
      System.out.print("Congress:\t" + congress);
      System.out.println("\tSession:\t" + session);
      e.printStackTrace();
      return false;
    }
    // Downloads all data from the given congress and session
    return downloadCongressSessionData(congress, session, items);
  }
  
  /**
   * Helper Class for downloadCongressSessionData
   * 
   * @param congress
   * @param session
   * @param items Number of items to download
   * @return true if all data successfully downloaded otherwise returns false
   */
  private static boolean downloadCongressSessionData(int congress, int session, int items) {
    // Creates a template link to where vote data will be downloaded from
    String votePage = "https://www.senate.gov/legislative/LIS/roll_call_votes/vote";
    votePage += congress + session + "/vote_" + congress + "_" + session + "_";
    
    // Uses items to loop through and attempt to download all vote pages
    for(int action=1; action<=items; action++) {
      // Creates the action number in the format used by the Senate (\d{5,5})
      String actionNumber = Integer.toString(action);
      while(actionNumber.length() != 5) { actionNumber = "0" + actionNumber;}
      // Attempts to download the vote page and returns false if a download fails
      String currentVotePage = votePage + actionNumber + ".xml";
      String fileName = SenateFileLocation + "Senate_" + congress + "_" + session + "_" + action + ".xml";
      if(!DownloadWebPage(currentVotePage, fileName)) {return false;}
    }
    return true; // Return true after all data has been downloading
  }
  
  /**
   * Downloads the a file from 'url' to 'fileName'
   * Will make up to four requests for the data, requests will be spaced by 60 seconds.
   * 
   * @param url
   * @param fileName
   * @return true if the webPage was successfully downloaded and false otherwise
   */
  private static boolean DownloadWebPage(String webPage, String fileName) {
      return DownloadWebPage(webPage, fileName, 4);
  }
  
  /**
   * Downloads the a file from 'url' to 'fileName'
   * Will make up to 'attempts' requests for the data, requests will be spaced by 60 seconds.
   * 
   * @param webPage
   * @param fileName
   * @param attempts number of attempts to try to download the data
   * @return true if the webPage was successfully downloaded and false otherwise
   */
  private static boolean DownloadWebPage(String webPage, String fileName, int attempts) {
    // Initializes URL and writer
    URL url;
    BufferedWriter writer;
    try {
      url = new URL(webPage);
      writer = new BufferedWriter(new FileWriter(fileName));
    } 
    catch (MalformedURLException mue) {
      System.out.println("Malformed URL Exception raised:\t" + webPage);
      return false;
    }
    catch(IOException e) {
      e.printStackTrace();
      return false;
    }
    
    // Tries to download the file the number of times specified
    for(int i=0; i<attempts; i++) {
      try {
        // Initialize  reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        // Write information from URL
        String line;
        while ((line = reader.readLine()) != null) {
          writer.write(line);
        }
        // Close reader and writer and return true;
        reader.close();
        writer.close();
        return true;
      }
      catch(FileNotFoundException e) {
        return false;
      }
      catch(Exception e) {
        try {
          TimeUnit.SECONDS.sleep(60);
        } catch (Exception e1) {}
      }
    }
    // Closes writer, prints failure message and returns false if the file Failed to download
    try {
      writer.close();
    } catch(Exception e) {}
    System.out.println("Failed to download file:\t" + fileName);
    return false;
  }
}
