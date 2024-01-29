
public class HouseAction extends Action<Member> {
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
  public HouseAction(int yayVotes, int nayVotes, int absent,
      int year, String date, String voteQuestion, Member[] voters, Boolean[] votes) {
    super(yayVotes, nayVotes, absent, year, date, voteQuestion, voters, votes);
  }
}
