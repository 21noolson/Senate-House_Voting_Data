import java.util.Comparator;

public class firstNameCompartor<T> implements Comparator<CongressPerson> {

  @Override
  public int compare(CongressPerson o1, CongressPerson o2) {
    String name1 = o1.getFirstName();
    String name2 = o2.getFirstName();
    return name1.compareTo(name2);
  }

}
