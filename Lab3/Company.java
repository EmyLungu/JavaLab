import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Company
 */
public class Company implements Profile {
    int profileID;
    String name;
    int stockPrice;

    Map<Profile, RelationType> relationships;

    Company(String name, int id) {
        this.name = name;
        this.profileID = id;
        this.relationships = new HashMap<>();
    }

    public int getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(int stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return this.profileID;
    }

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
        return "Company [" + getName() + ", ID=" + getID() + ", " + this.relationships.size() + " conns]";
    }
}
