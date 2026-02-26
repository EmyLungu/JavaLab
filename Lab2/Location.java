import java.util.ArrayList;
import java.util.List;

/**
 * Location
 */

public abstract sealed class Location permits CityLocation, AirportLocation, GasStationLocation {
    int id;             // Index inside the problem.locations
    String name;
    double x, y;
    List<Road> roads; // The indices of the roads this location connects to

    /**
     * Creates a new Location
     */
    public Location(String name, double x, double y) {
        this.id = -1;
        this.name = name;
        this.x = x;
        this.y = y;
        this.roads = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return this.name + " (" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Location otherLocation = (Location) obj;
        return (
            this.getName().equals(otherLocation.getName()) &&
            this.getX() == otherLocation.getX() &&
            this.getY() == otherLocation.getY()
        );
    }
}
