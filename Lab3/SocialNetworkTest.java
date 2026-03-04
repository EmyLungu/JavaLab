import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SocialNetworkTest {

    @Test
    void testStarNetwork() {
        SocialNetwork network = new SocialNetwork();

        Company google = new Company("Google", 0);
        Person p1 = new Person("Alice", 1);
        Person p2 = new Person( "Bob", 2);
        
        p1.addRelationship(google, RelationType.JUNIOR_DEV);
        p2.addRelationship(google, RelationType.LEAD_DESIGNER);

        google.addRelationship(p1, RelationType.EMPLOYER);
        google.addRelationship(p2, RelationType.EMPLOYER);

        network.add(google);
        network.add(p1);
        network.add(p2);

        List<Profile> points = new ArrayList<>();
        List<Set<Profile>> maximal = new ArrayList<>();
        network.articulationPoints(points, maximal);

        assertEquals(1, points.size());
        assertTrue(points.contains(google));
    }

    @Test
    void testSimpleMaximalParts() {
        SocialNetwork network = new SocialNetwork();

        Person bob = new Person("Bob", 0);
        Person alice = new Person("Alice", 1);
        Person charlie = new Person("Charlie", 2);

        bob.addRelationship(alice, RelationType.FRIEND);
        alice.addRelationship(bob, RelationType.FRIEND);

        alice.addRelationship(charlie, RelationType.COLLEAGUE);
        charlie.addRelationship(alice, RelationType.COLLEAGUE);

        network.add(bob);
        network.add(alice);
        network.add(charlie);

        List<Profile> points = new ArrayList<>();
        List<Set<Profile>> parts = new ArrayList<>();
        network.articulationPoints(points, parts);

        assertEquals(2, parts.size(), "There should be two separate connections (parts)");
        assertTrue(parts.get(0).contains(alice));
        assertTrue(parts.get(1).contains(alice));
    }
}
