import java.util.Map;

enum RelationType {
    FRIEND,
    COLLEAGUE,

    EMPLOYER,
    JUNIOR_DEV,
    SENIOR_DEV,
    LEAD_DESIGNER,
}

/**
 * Profile
 */
public interface Profile extends Comparable<Profile> {
    String getName();
    public int getID();

    Map<Profile, RelationType> getRelationships();
    void addRelationship(Profile who, RelationType type);
}
