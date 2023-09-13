import java.util.Comparator;

public class lastNameCompartor<T> implements Comparator<Member> {

  @Override
  public int compare(Member o1, Member o2) {
    String name1 = o1.getLastName();
    String name2 = o2.getLastName();
    return name1.compareTo(name2);
  }

}
