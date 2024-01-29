import java.util.Comparator;

public class firstNameCompartor implements Comparator<Member> {

  @Override
  public int compare(Member o1, Member o2) {
    String name1 = o1.getFirstName();
    String name2 = o2.getFirstName();
    return name1.compareTo(name2);
  }

}
