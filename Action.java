
public class Action<T> {
  private final int yayVotes;   // Number of yay Votes
  private final int nayVotes;   // Number of nay Votes
  private final int absent;     // Number of members absent from vote
  private final int year;
  private final String date;
  private final String voteQuestion;      // What is being voted on
  private final T[] voters;  // Congress people voting on this action
  private final Boolean[] votes;          // Votes by the list of Congress people.
  
  public Action(int yayVotes, int nayVotes, int absent, int year, String date, String voteQuestion,
      T[] voters, Boolean[] votes) {
    this.yayVotes = yayVotes;
    this.nayVotes = nayVotes;
    this.absent = absent;
    this.year = year;
    this.date = date;
    this.voteQuestion = voteQuestion;
    this.voters = voters;
    this.votes = votes;
  }
  
  public String getCommitte() {
    return "Override needed";
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

  public int getYear() {
    return year;
  }

  public String getDate() {
    return date;
  }

  public String getVoteQuestion() {
    return voteQuestion;
  }

  public T[] getVoters() {
    return voters;
  }

  public Boolean[] getVotes() {
    return votes;
  }
}
