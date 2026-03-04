import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Person
 */
public class Person implements Profile {
    int profileID;
    String name;
    Date birthDate;

    Map<Profile, RelationType> relationships;

    Person(String name, int id) {
        this.name = name;
        this.profileID = id;
        this.relationships = new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getID() {
        return this.profileID;
    }

    @Override
    public Map<Profile, RelationType> getRelationships() {
        return relationships;
    }

    @Override
    public void addRelationship(Profile who, RelationType type) {
        this.relationships.put(who, type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName());
    };

    @Override
    public int compareTo(Profile other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public String toString() {
        return "Person [" + getName() + ", ID=" + getID() + ", " + this.relationships.size() + " conns]";
    }
}
