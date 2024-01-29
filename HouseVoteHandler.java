import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HouseVoteHandler extends DefaultHandler {
  private HouseAction action;
  public void startDocument() {}
  
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    System.out.println("URI:");
    System.out.println(uri);
    System.out.println("localName:");
    System.out.println(localName);
    System.out.println("qName:");
    System.out.println(qName);
    System.out.println("Attributes:");
    for(int i=0; i<attributes.getLength(); i++) {
      System.out.print("Name:\t");
      System.out.println(attributes.getQName(i));
      System.out.print("Value:\t");
      System.out.println(attributes.getValue(i));
    }
    
  }
  
  public void endElement(String uri, String localName, String qName) {}
  
  public void endDocument() {}
  
  public HouseAction getAction() {
    return action;
  }
  
}
