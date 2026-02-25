/**
 * Location
 */

public class Location {
    public enum LocationType {
        CITY,
        AIRPORT,
        GAS_STATION,
    }

    String name;
    LocationType type;
    double x, y;

    Location(String name, LocationType type, double x, double y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return this.name + " of type " + this.type + " is at (" + this.x + "," + this.y + ")";
    }
}
