/**
 * Road
 */
public class Road {
    public enum RoadType {
        HIGHWAY,
        NATIONAL,
        COUNTY,
        CITY,
        INTERCITY,
    }

    RoadType type;
    double length;
    int speedLimit;

    Road(RoadType type, double length, int speedLimit) {
        this.type = type;
        this.length = length;
        this.speedLimit = speedLimit;
    }

    Road(RoadType type, Location a, Location b, int speedLimit) {
        this.type = type;
        this.length = Math.sqrt(a.getX() * b.getX() + a.getY() + b.getY());
        this.speedLimit = speedLimit;
    }

    public RoadType getType() {
        return type;
    }
    
    public void setType(RoadType type) {
        this.type = type;
    }

    public double getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public String toString() {
        return this.type + ", Length = " + this.length + "km, Speed limit = " + this.speedLimit;
    }
}
