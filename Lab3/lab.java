import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * lab
 */
public class lab {
    public static void main(String[] args) {
        List<Profile> entities = new ArrayList<>();

        entities.add(new Person("Emy", 1));
        entities.add(new Person("Edward", 2));
        entities.add(new Company("Intel", 3));
        entities.add(new Person("Dani", 4));
        entities.add(new Company("AMD", 5));

        entities.sort(Comparator.naturalOrder());

        for (Profile entity : entities) {
            System.out.println(entity.toString());
        }
    }
}
