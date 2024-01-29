
public class SenateAction extends Action<Member> {
  private final int congress;   // Congress voting on action
  private final int session;    // Congress Session voting on action

  /**
   * Create an Action that occurred in the Senate
   * 
   * @param yayVotes Number of 'nay' votes
   * @param nayVotes Number of 'nay' votes
   * @param absent Number of members absent from the vote
   * @param congress Congressional Number
   * @param session Session Number
   * @param year  
   * @param date
   * @param voteQuestion
   * @param voters List of CongressPerson that voted on the action
   * @param votes List of votes in order of voters
   */
  public SenateAction(int yayVotes, int nayVotes, int absent, int congress, int session,
      int year, String date, String voteQuestion, Member[] voters, Boolean[] votes) {
    super(yayVotes, nayVotes, absent, year, date, voteQuestion, voters, votes);
    this.congress = congress;
    this.session = session;
  }
  
  public int getCongress() {
    return congress;
  }

  public int getSession() {
    return session;
  }
  
  public String getCommitte() {
    return "Congress:\t" + this.getCongress() + "\nSession:\t" + this.getSession();
  }
  
}
