import java.util.Comparator;

public class lastNameCompartor<T> implements Comparator<CongressPerson> {

  @Override
  public int compare(CongressPerson o1, CongressPerson o2) {
    String name1 = o1.getLastName();
    String name2 = o2.getLastName();
    return name1.compareTo(name2);
  }

}
