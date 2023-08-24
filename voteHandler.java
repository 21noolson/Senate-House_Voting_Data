import java.util.concurrent.TimeUnit;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class voteHandler extends DefaultHandler {
  private Actions action;
  String currentTag;
  
  String memberID; // Congress assigned Member ID
  String firstName;
  String lastName;
  String congressSession;
  char party;
  String state;
  
  int yayVotes;   // Number of yay Votes
  int nayVotes;   // Number of nay Votes
  int absent;     // Number of members absent from vote
  int congress;   // Congress voting on action
  int session;    // Congress Session voting on action
  int voteNumber; // Holds the current Vote Number for the session
  int year;
  String date;
  String voteQuestion;      // What is being voted on
  int votesCounted = 0;     // Number of votes recorded so far
  CongressPerson[] voters = new CongressPerson[100];  // Congress people voting on this action
  Boolean[] votes = new Boolean[100];          // Votes by the list of Congress people.
                                               // True for yay, False for nay, and Null for Not Voting
  
  //public void startDocument() {}
  
  /**
   * 
   */
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    currentTag = qName;
    //System.out.println(qName);
  }
  
  public void characters(char[] ch, int start, int length) {
    // Converts the char[] to s a string
    String data = "";
    for(int i=start; i<start+length; i++) {
      data = data + ch[i];
    }
    switch(currentTag) {
      case "congress": this.congress = Integer.valueOf(data);
        return;
      case "session": this.session = Integer.valueOf(data);
        return;
      case "congress_year": 
        this.year = Integer.valueOf(data);
        this.congressSession = this.session+"-";
        this.congressSession = this.congressSession + this.congress;
        //System.out.println(this.congressSession + "\tat count");
        return;
      case "vote_number": this.voteNumber = Integer.valueOf(data);
      case "vote_date": this.date = data;
        return;
      case "vote_question_text": this.voteQuestion = data;
        return;
      case "yeas": this.yayVotes = Integer.valueOf(data);
        return;
      case "nays": this.nayVotes = Integer.valueOf(data);
        return;
      case "member": 
        this.firstName = null;
        this.lastName = null;
        this.memberID = null;
        return;
      case "last_name": this.lastName = data;
        return;
      case "first_name": this.firstName = data;
        return;
      case "party": this.party = data.charAt(0);
        return;
      case "state": this.state = data;
        return;
      case "vote_cast":
        if(data.equals("Yea")) { votes[votesCounted] = true; }
        else if(data.equals("Nay")) { votes[votesCounted] = false; }
        return;
      case "lis_member_id": this.memberID = data;
        return;
        
    }
  }
  
  public void endElement(String uri, String localName, String qName) {
    if(qName.equals("member")) {
      System.out.println(this.memberID);
      System.out.println(this.firstName);
      System.out.println(this.lastName);
      System.out.println(this.congressSession);
      System.out.println(this.party);
      System.out.println(this.state);
      CongressPerson member = new CongressPerson(this.memberID, this.firstName, this.lastName,
          this.congressSession, this.party, this.state);
      this.voters[votesCounted] = member;
      this.votesCounted++;
    }
  }
  
  public void endDocument() {
    if(votesCounted != 100) {
      CongressPerson[] voters = new CongressPerson[votesCounted];
      Boolean[] votes = new Boolean[votesCounted];
      for(int i=0; i<votesCounted; i++) {
        voters[i] = this.voters[i];
        votes[i] = this.votes[i];
      }
      this.voters = voters;
      this.votes = votes;
    }
    action = new Actions(this.yayVotes, this.nayVotes, this.absent, this.congress, this.session,
        this.year, this.date, this.voteQuestion, this.voters, this.votes);
  }
  
  public Actions getAction() {
    return action;
  }
}
