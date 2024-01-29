import java.util.ArrayList;
import java.util.Hashtable;

public class ARIData {
  Member member;
  Hashtable<Member, Double> ARIValues;
  
  
  public ARIData(Member member) {
    this.member=member; 
  }
  
  public void addData(Member member, double ARI) {
    ARIValues.put(member, ARI);
  }
  
  public double getARI(Member member) {
    return ARIValues.get(member);
  }
  
  public double getARIAverage(ArrayList<Member> members) {
    // Sum the ARI values of the given members
    double ARITotal = 0;
    for(Member member: members) {
      ARITotal += this.getARI(member);
    }
    // Returns the average of the sum of values
    return ARITotal/members.size();
  }
}
