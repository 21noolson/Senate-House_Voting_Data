import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Tester {

  public static void XMLTester(File file) {
    
    // Declare the SAX factory, Parser and Handler for processing data
    SAXParserFactory factory = null;
    SAXParser saxParser = null;
    CongressVoteHandler handler = null;
    
    // Create SAX Parser and parse the given dataFile
    try {
      factory = SAXParserFactory.newInstance();
      saxParser = factory.newSAXParser();
      handler = new CongressVoteHandler();
      saxParser.parse(file, handler);
    }
    
    // Print Exception information if parser Fails initialization and returns
    catch(Exception e) {
      System.out.println("Failed to intialize SAX Factory, Parser, or handler");
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    XMLTester(new File("D:\\Senate and House Data and processing\\Senate_102_1_85.xml"));

  }

}
