
public class Actions {
  private final int yayVotes;   // Number of yay Votes
  private final int nayVotes;   // Number of nay Votes
  private final int absent;     // Number of members absent from vote
  private final int congress;   // Congress voting on action
  private final int session;    // Congress Session voting on action
  private final int year;
  private final String date;
  private final String voteQuestion;      // What is being voted on
  private final CongressPerson[] voters;  // Congress people voting on this action
  private final Boolean[] votes;          // Votes by the list of Congress people.
                // True for yay, False for nay, and Null for abstain and present

  
  public Actions(int yayVotes, int nayVotes, int absent, int congress, int session,
      int year, String date, String voteQuestion, CongressPerson[] voters, Boolean[] votes) {
    this.yayVotes = yayVotes;
    this.nayVotes = nayVotes;
    this.absent = absent;
    this.congress = congress;
    this.session = session;
    this.year = year;
    this.date = date;
    this.voteQuestion = voteQuestion;
    this.voters = voters;
    this.votes = votes;
  }
  
  public int getYayVotes() {
    return yayVotes;
  }

  public int getNayVotes() {
    return nayVotes;
  }

  public int getAbsent() {
    return absent;
  }

  public int getCongress() {
    return congress;
  }

  public int getSession() {
    return session;
  }

  public int getYear() {
    return year;
  }

  public String getDate() {
    return date;
  }

  public String getVoteQuestion() {
    return voteQuestion;
  }

  public CongressPerson[] getVoters() {
    return voters;
  }

  public Boolean[] getVotes() {
    return votes;
  }

  
}
