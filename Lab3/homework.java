import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * homework
 */
public class homework {
    public static void insertExample(SocialNetwork network) {
        Programmer emy = new Programmer("Emy", 0);
        Programmer edy = new Programmer("Edward", 1);
        Company intel  = new Company("Intel", 2);
        Designer dani  = new Designer("Dani", 3);
        Company amd    = new Company("AMD", 4);

        network.add(emy);
        network.add(edy);
        network.add(intel);
        network.add(dani);
        network.add(amd);

        emy.addRelationship(intel, RelationType.SENIOR_DEV);
        emy.addRelationship(edy, RelationType.COLLEAGUE);
        emy.addRelationship(dani,RelationType.FRIEND);

        edy.addRelationship(intel, RelationType.JUNIOR_DEV);
        edy.addRelationship(emy, RelationType.COLLEAGUE);

        dani.addRelationship(amd, RelationType.LEAD_DESIGNER);
        dani.addRelationship(emy, RelationType.FRIEND);

        intel.addRelationship(emy, RelationType.EMPLOYER);
        intel.addRelationship(edy, RelationType.EMPLOYER);

        amd.addRelationship(dani, RelationType.EMPLOYER);
    }

    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();
        insertExample(network);

        network.sortByImportance();
        network.print();

        // Advanced
        
        List<Profile> points = new ArrayList<>();
        List<Set<Profile>> maximal = new ArrayList<>();
        network.articulationPoints(points, maximal);

        System.out.println("\n\nArticulation Points:");
        for (Profile p : points) {
            System.out.println(p);
        }

        System.out.println("Maximal parts:");
        for (Set<Profile> part : maximal) {
            for (Profile p : part) {
                System.out.println(p);
            }
            System.out.println("---");
        }
    }
}
