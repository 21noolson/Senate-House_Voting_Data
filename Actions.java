
public class Actions {
  int yayVotes;   // Number of yay Votes
  int nayVotes;   // Number of nay Votes
  int absent;   // Number of members absent from vote
  int congress;   // Congress voting on action
  int session;    // Congress Session voting on action
  int year;
  String date;
  String voteQuestion;      // What is being voted on
  CongressPerson[] voters;  // Congress people voting on this action
  Boolean[] votes;          // Votes by the list of Congress people.
                            // True for yay and False for nay
  
}
