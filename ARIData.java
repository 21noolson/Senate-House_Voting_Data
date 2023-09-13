import java.util.ArrayList;
import java.util.Hashtable;

public class ARIData {
  CongressMember member;
  Hashtable<CongressMember, Double> ARIValues;
  
  
  public ARIData(CongressMember member) {
    this.member=member; 
  }
  
  public void addData(CongressMember member, double ARI) {
    ARIValues.put(member, ARI);
  }
  
  public double getARI(CongressMember member) {
    return ARIValues.get(member);
  }
  
  public double getARIAverage(ArrayList<CongressMember> members) {
    // Sum the ARI values of the given members
    double ARITotal = 0;
    for(CongressMember member: members) {
      ARITotal += this.getARI(member);
    }
    // Returns the average of the sum of values
    return ARITotal/members.size();
  }
}
