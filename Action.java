
/**
 * Represents a political action
 * 
 * @author NoahOlson
 * @param <T> Type of voters voting on political action
 */
public class Action<T> {
  private final int yayVotes;   // Number of yay Votes
  private final int nayVotes;   // Number of nay Votes
  private final int absent;     // Number of voters absent from vote
  private final int year;
  private final String date;
  private final String voteQuestion;  // What is being voted on
  private final T[] voters;           // List of voters voting on this action
  private final Boolean[] votes;      // Votes by the list of Congress people.
                                      // true for yay, false for nay, and null for absent
  
  /**
   * @param yayVotes number of yay Votes
   * @param nayVotes number of nay votes
   * @param absent number of voters absent
   * @param year
   * @param date
   * @param voteQuestion question being voted on
   * @param voters
   * @param votes List of voters. true for yay, false for nay, and null for absent
   */
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
