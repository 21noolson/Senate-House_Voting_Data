// Java program to read and download
// webpage in html file
import java.io.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;  


/** Class for downloading all senate voting files that currently exist
 * 
 * @author NoahOlson
 */
public class DataDownload {
  // Folder where you will store your data. Data is approximately 12,000 files and 300MB
  static final String SenateFileLocation = "Data\\DownloadData\\Senate_Voting_Data\\"; 
  static final String HouseFileLocation = "Data\\DownloadData\\House_Voting_Data\\";
  
  // First and Last Congress Session and House Year
  static final int firstCongress = 101;
  static final int lastCongress = 118;
  static final int firstHouseYear = 1990;
  static final int lastHouseYear = 2023;
  
  //Declare the SAX factory, Parser and Handler for processing data
  static SAXParserFactory factory;
  static SAXParser saxParser;
  static CongressVoteMenuHandler senateHandler;
  static HouseVoteHandler houseHandler;
  
  
  
  /**
   * Initializes the class before any of the other functions can be run
   * 
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static void start() throws ParserConfigurationException, SAXException {
    factory = SAXParserFactory.newInstance();
    saxParser = factory.newSAXParser();
    senateHandler = new CongressVoteMenuHandler();
    houseHandler = new HouseVoteHandler();
  }
  
  
  
  /**
   * Downloads the a file from 'webpage' to 'fileName'
   * Will make up to six requests for the data, requests will be spaced by 45 seconds.
   * 
   * @param webPage String the URL to the file to be downloaded
   * @param fileName the name of the file after being downloaded
   * @throws IOException If an I/O error occurs 6 times in a row, this likely indicates failure to download data
   */
  public static void DownloadWebPage(String webPage, String fileName) throws IOException {
      DownloadWebPage(webPage, fileName, 0);
  }
  
  /**
   * Helper class for DownloadWebPage
   */
  private static void DownloadWebPage(String webPage, String fileName, int attempts) throws IOException {
    // Declare reader and writer outside of try block so they can be closed in finally statement
    BufferedReader readr = null;
    BufferedWriter writer = null;
    try {
      // Create URL object
      URL url = new URL(webPage);
      
      // Initialize reader and writer
      readr = new BufferedReader(new InputStreamReader(url.openStream()));
      writer = new BufferedWriter(new FileWriter(fileName));
      
      // read each line from stream till end
      String line;
      while ((line = readr.readLine()) != null) {
        writer.write(line);
      }
      System.out.println("Successfully Downloaded:\t" + fileName);
      readr.close();
      writer.close();
    }

    // Exceptions
    catch (MalformedURLException mue) {
      System.out.println("Malformed URL Exception raised:\t" + webPage);
    }
    catch(FileNotFoundException e) {
      throw e;
    }
    catch (IOException ie) {
      // Print info about exception
      ie.printStackTrace();
      System.out.println("IOException raised:\t" + webPage);
      System.out.println("Trying again in 45 seconds");
      
      // Wait 15 seconds
      try {
        TimeUnit.SECONDS.sleep(45);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      // Try to grab data again if less then four attempts have been tried
      if(attempts < 6) { DownloadWebPage(webPage, fileName, (attempts+1)); }
      else {
        System.out.println("Failed to get data from " + webPage);
        throw ie;
      }
    }
    // Close reader and writer to avoid a resource leak
  }
  
  
  
  /**
   * Downloads all of the vote data for the given congress and session
   * 
   * @param congress congress number to be downloaded
   * @param session session number to be downloaded
   * @param saxParser SAXParser to parse data with
   * @param handler handler for counting data
   */
  private static void downloadCongressSessionData(int congress, int session, SAXParser saxParser, 
      CongressVoteMenuHandler handler) {
    
    // Creates link to the senate menu page for the given congress and session
    String sessionsPage = "https://www.senate.gov/legislative/LIS/roll_call_lists/vote_menu_" 
        + congress + "_" + session +".xml";
    // Creates the start of a link to all votes in the given congress and session
    String votePage = "https://www.senate.gov/legislative/LIS/roll_call_votes/vote" + congress +
        session + "/vote_" + congress + "_" + session + "_";
    
    // Attempts to downloads the senate session page 
    try {
      String location = SenateFileLocation + "tempData.xml";
      DownloadWebPage(sessionsPage, location);
    } 
    catch (IOException e1) {
      System.out.println("Failed to grab the session data for Congress:\t" + congress +
          "\tSession:\t" + session);
    }
    // Counts the number of votes conducted
    File voteMenu = new File(SenateFileLocation + "tempData.xml");
    try {
      saxParser.parse(voteMenu, handler);
    } catch (Exception e) {
      System.out.println("Failed to parse vote menu for Congress:\t" + congress +
          "\tSession:\t" + session);
    }
    int items = handler.getVotes();
    
    // Downloads all data from the given congress and session
    for(int action=1; action<=items; action++) {
      // Creates the action number in the format used by the Senate (\d{5,5})
      String actionNumber = Integer.toString(action);
      while(actionNumber.length() != 5) { actionNumber = "0" + actionNumber;}
      
      // Attempts to download the vote page
      String currentVotePage = votePage + actionNumber + ".xml";
      String fileName = "Senate_" + congress + "_" + session + "_" + action;
      fileName = SenateFileLocation + fileName + ".xml";
      try {
        DownloadWebPage(currentVotePage, fileName);
      } catch (IOException e) {}
    }
  }
  
  /**
   * 
   * @param year of House Session to be downloaded
   * @param saxParser to parse data with
   */
  private static void downloadHouseYearData(int year, SAXParser saxParser) {
    // Location of the house page for the current year
    final String housePage = "https://clerk.house.gov/evs/" + year + "/roll";
    // Creates a rollCallNum variable and a rollCallFormatted variable
    int rollCallNum = 1;
    String rollCallFormatted = "001";
    
    // First URL location
    String downloadablePage = housePage+rollCallFormatted+".xml";
    
    while(true) {     
      // Created fileLocation
      String fileLocation = "House_" + year + "_" + rollCallNum;
      fileLocation = HouseFileLocation+fileLocation+".xml";
      
      // Download Page
      try {
        DownloadWebPage(downloadablePage, fileLocation);
      }
      catch(FileNotFoundException e) {
        return;
      }
      catch(Exception e) {
        e.printStackTrace();
      }
      
      // Increments Roll Call 
      rollCallNum++;
      rollCallFormatted = Integer.toString(rollCallNum);
      while(rollCallFormatted.length() < 3) { rollCallFormatted = "0" + rollCallFormatted; }
      
      // Updates the URL object
      downloadablePage = housePage+rollCallFormatted+".xml"; 
    }
  }
  
  
  
  /**
   * Downloads All Congress Data
   */
  public static void downloadAllCongressData() {
   downloadCongressData(firstCongress, lastCongress);
  }
  
  /**
   * Downloads Congress Data from the given startCongress up to the current Congress
   * 
   * @param startCongress the Congress number to begin the Download
   */
  public static void downloadCongressData(int startCongress) {
    downloadCongressData(startCongress, lastCongress);
  }
  
  /**
   * Downloads Congress data starting with the startCongress continuing until endCongress (Inclusive)
   *          
   * @param startCongress the congress number to begin with
   * @param endCongress the congress number to end with
   */
  public static void downloadCongressData(int startCongress, int endCongress) {
    if(startCongress < firstCongress  || endCongress > lastCongress || startCongress > endCongress) {
      throw new IndexOutOfBoundsException();
    }
    // Loops through all current Congressional session which are on the senates website
    for(int congress=startCongress; congress<=endCongress; congress++) {
      for(int session=1; session<=2; session++) {
        if(!(congress == lastCongress && session == 2)) { 
          downloadCongressSessionData(congress, session, saxParser, senateHandler);
        }
      }
    }
  }
  
  
  
  /**
   * Downloads All House Data
   */
  public static void downloadAllHouseData() {
    downloadHouseData(firstHouseYear, lastHouseYear);
  }
  
  /**
   * Downloads House Data from the startYear up to the current year
   * 
   * @param startYear first year to start downloading data
   */
  public static void downloadHouseData(int startYear) {
    downloadHouseData(startYear, lastHouseYear);
  }
  
  /**
   * Downloads House Data from the startYear up to the endYear
   * 
   * @param startYear first year to start downloading data
   * @param endYear last year to download data
   */
  public static void downloadHouseData(int startYear, int endYear) {
    if(startYear < firstHouseYear || endYear > lastHouseYear || endYear < startYear) {
      throw new IndexOutOfBoundsException();
    }
    for(int i=startYear; i<=endYear; i++) {
      downloadHouseYearData(i, saxParser);
    }
  }
  
  
  
  /**
   * Downloads all Data
   */
  public static void downloadAllData() {
    downloadAllCongressData();
    downloadAllHouseData();
  }
  
  
  
  
  public static void main(String[] args) {
    try {
      start();
      downloadCongressData(114);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
