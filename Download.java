// Java program to read and download
// webpage in html file
import java.io.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;

import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  


/** Class for downloading all senate voting files that currently exist
 * 
 * @author NoahOlson
 */
public class Download {
  // Folder where you will store your data. Data is approximately 12,000 files and 300MB
  static final String FileLocation = "D:\\Senate and House Data and processing\\"; 
  
  /**
   * Downloads the .xml file from the given web page and stores it in the given fileName in the FileLocation
   * Will make up to four requests for the data, requests will be spaced by 15 seconds.
   * 
   * @param webPage String the URL to the file to be downloaded
   * @param fileName the name of the file after being downloaded
   * @throws IOException If an I/O error occurs 4 times in a row, this likely indicates failure to download data
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
      writer = new BufferedWriter(new FileWriter(FileLocation+fileName+".xml"));
      
      // read each line from stream till end
      String line;
      while ((line = readr.readLine()) != null) {
        writer.write(line);
      }
      System.out.println("Successfully Downloaded:\t" + fileName);
    }

    // Exceptions
    catch (MalformedURLException mue) {
      System.out.println("Malformed URL Exception raised:\t" + webPage);
    }
    catch (IOException ie) {
      // Print info about exception
      ie.printStackTrace();
      System.out.println("IOException raised:\t" + webPage);
      System.out.println("Trying again in 15 seconds");
      
      // Wait 15 seconds
      try {
        TimeUnit.SECONDS.sleep(15);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      // Try to grab data again if less then four attempts have been tried
      if(attempts < 4) { DownloadWebPage(webPage, fileName, (attempts+1)); }
      else {
        System.out.println("Failed to get data from " + webPage);
        throw ie;
      }
    }
    // Close reader and writer to avoid a resource leak
    finally {
      readr.close();
      writer.close();
    }
  }
  
  /**
   * Downloads all of the vote data for the given congress and session
   * 
   * @param congress congress number to be downloaded
   * @param session session number to be downloaded
   * @param saxParser SAXParser to parse data with
   * @param handler handler for counting data
   */
  public static void grabSessionData(int congress, int session, SAXParser saxParser, 
      countingHandler handler) {
    
    // Creates link to the senate menu page for the given congress and session
    String sessionsPage = "https://www.senate.gov/legislative/LIS/roll_call_lists/vote_menu_" 
        + congress + "_" + session +".xml";
    // Creates the start of a link to all votes in the given congress and session
    String votePage = "https://www.senate.gov/legislative/LIS/roll_call_votes/vote" + congress +
        session + "/vote_" + congress + "_" + session + "_";
    
    // Attempts to downloads the senate session page 
    try {
      DownloadWebPage(sessionsPage, "tempData");
    } 
    catch (IOException e1) {
      System.out.println("Failed to grab the session data for Congress:\t" + congress +
          "\tSession:\t" + session);
    }
    // Counts the number of votes conducted
    File voteMenu = new File(FileLocation + "tempData.xml");
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
      try {
        DownloadWebPage(currentVotePage, fileName);
      } catch (IOException e) {}
    }
  }
  
  public static void main(String[] args) {
    // Declare the SAX factory, Parser and Handler for processing data
    SAXParserFactory factory = null;
    SAXParser saxParser = null;
    countingHandler handler = null;
    // Create SAX Parser to collect XML data
    try {
      factory = SAXParserFactory.newInstance();
      saxParser = factory.newSAXParser();
      handler = new countingHandler();
    }
    // Print Exception information if parser Fails initialization and returns
    catch(Exception e) {
      System.out.println("Failed to intialize SAX Factory, Parser, or handler");
      e.printStackTrace();
      return;
    }
      
    // Loops through all current Congressional session which are on the senates website
    for(int congress=101; congress<=118; congress++) {
      for(int session=1; session<=2; session++) {
        if(!(congress == 118 && session == 2)) { grabSessionData(congress, 
        		session, saxParser, handler); }
      }
    }
  }
}
