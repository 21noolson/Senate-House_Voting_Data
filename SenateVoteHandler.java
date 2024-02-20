import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Extends DefaultHandler in order to parse a Congress Vote XML document and create an action from it
 * 
 * @author NoahOlson
 */
public class SenateVoteHandler extends DefaultHandler {
  //Keeps track of the XMl tag we are inside of between startElelement and characters methods
  String currentTag; 
  // Keeps track of Member data while processing
  String memberID; 
  String firstName;
  String lastName;
  String congressSession;
  char party;
  String state;
  // Keeps track of Action data while processing
  int yayVotes;   
  int nayVotes;   
  int absent;     
  int congress;   
  int session;    
  int voteNumber; 
  int year;
  String date;
  String voteQuestion;
  int votesCounted;     
  Member[] voters;  
  Boolean[] votes; // True for yay, False for nay, and Null for Not Voting
  //Action to be created 
  private SenateAction action;
  
  public void startDocument() {
    votesCounted = 0;
    voters = new Member[100];  
    votes = new Boolean[100];
  }

  /**
   * Grabs the name of the XML tag when an element is started
   */
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    currentTag = qName;
  }
  
  /**
   * Looks at the currentTag to determine where data can be saved when character data is encountered 
   */
  public void characters(char[] ch, int start, int length) {
    // Converts the char[] to s a string
    String data = "";
    for(int i=start; i<start+length; i++) {
      data = data + ch[i];
    }
    // Returns if characters was mistakenly called
    if(data.isBlank()) { return; }
    switch(currentTag) {
      case "congress": this.congress = Integer.valueOf(data);
        break;
      case "session": this.session = Integer.valueOf(data);
        break;
      case "congress_year": 
        this.year = Integer.valueOf(data);
        this.congressSession = this.session + "-" + this.congress;
        break;
      case "vote_number":  this.voteNumber = Integer.valueOf(data);
        break;
      case "vote_date": this.date = data;
        break;
      case "vote_question_text": this.voteQuestion = data;
        break;
      case "yeas": this.yayVotes = Integer.valueOf(data);
        break;
      case "nays": this.nayVotes = Integer.valueOf(data);
        break;
      case "member": 
        this.firstName = null;
        this.lastName = null;
        this.memberID = null;
        break;
      case "last_name": this.lastName = data;
        break;
      case "first_name": this.firstName = data;
        break;
      case "party": this.party = data.charAt(0);
        break;
      case "state": this.state = data;
        break;
      case "vote_cast":
        if(data.equals("Yea")) { votes[votesCounted] = true; }
        else if(data.equals("Nay")) { votes[votesCounted] = false; }
        break;
      case "lis_member_id": this.memberID = data;
        break;  
    }
  }
  
  /**
   * Creates a Member object and updates action data when a member XML element ends
   */
  public void endElement(String uri, String localName, String qName) {
    if(qName.equals("member")) {
      Member member = new Member(this.firstName, this.lastName);
      member.addSenateSession(this.congressSession, this.party, this.state);
      this.voters[votesCounted] = member;
      this.votesCounted++;
    }
  }

  /**
   * When the document ends a CongressAction is created
   */
  public void endDocument() {
    if(votesCounted != 100) {
      Member[] voters = new Member[votesCounted];
      Boolean[] votes = new Boolean[votesCounted];
      for(int i=0; i<votesCounted; i++) {
        voters[i] = this.voters[i];
        votes[i] = this.votes[i];
      }
      this.voters = voters;
      this.votes = votes;
    }
    action = new SenateAction(this.yayVotes, this.nayVotes, this.absent, this.congress, this.session,
        this.year, this.date, this.voteQuestion, this.voters, this.votes);
  }
  
  public SenateAction getAction() {
    return action;
  }
}
