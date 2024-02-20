import java.util.ArrayList;
import java.util.Hashtable;

public class StatsTable extends Hashtable<Member, Double> {
  private static final long serialVersionUID = 1L;
  Member member;
  
  
  public StatsTable(Member member) {
    this.member=member; 
  }

  public double getARIAverage(ArrayList<Member> members) {
    // Sum the ARI values of the given members
    double ARITotal = 0;
    for(Member member: members) {
      ARITotal += this.get(member);
    }
    // Returns the average of the sum of values
    return ARITotal/members.size();
  }
}
