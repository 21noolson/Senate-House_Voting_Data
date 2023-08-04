import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class countingHandler extends DefaultHandler {
  int votes = 0; // Number of votes that have taken place
  
  /**
   * Sets votes to zero when a new file is being parsed 
   */
  public void startDocument() {
    votes = 0;
  }
  
  /**
   * Increments votes by one when a XML tag is named 'vote'
   */
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if(qName.equals("vote")) {
      votes++;
    }
  }
  
  /** 
   * @return the number of votes from the previously parsed file
   */
  public int getVotes() {
    return votes;
  }
}
