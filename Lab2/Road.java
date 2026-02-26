/**
 * Road
 */
public class Road {
    public enum RoadType {
        HIGHWAY(150),
        NATIONAL(90),
        COUNTY(70),
        CITY(50);

        private final int speed;
        RoadType(int speed) {
            this.speed = speed;
        }
        public int getSpeed() { return speed; }
    }

    Location[] ends;

    public RoadType type;
    double length;

    /**
     * Creates a new road from Location a to Location b of the respective type
     * The length is calculated as the eucledian distance between the 2 locations
     */
    Road(RoadType type, Location a, Location b, double length) {
        this.setType(type);

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        this.setLength(Math.max(length, dist));
        this.ends = new Location[2];
        ends[0] = a;
        ends[1] = b;
    }

    Road(RoadType type, Location a, Location b) {
        this.setType(type);
        
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        this.setLength(dist);
        
        this.ends = new Location[2];
        ends[0] = a;
        ends[1] = b;
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

    public void setLength(double length) {
        this.length = length;
    }

    public double getSpeedLimit() {
        return this.getType().getSpeed();
    }

    public Location[] getEnds() {
        return ends;
    }

    @Override
    public String toString() {
        return this.type + ", Length = " +
               this.length + "km, (" +
               this.ends[0].getName() + " | " + this.ends[1].getName() +
               "), Speed limit = " + this.getType().getSpeed();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Road otherRoad = (Road) obj;
        
        boolean sameEnds =
            (this.ends[0].equals(otherRoad.ends[0]) && this.ends[1].equals(otherRoad.ends[1])) ||
            (this.ends[0].equals(otherRoad.ends[1]) && this.ends[1].equals(otherRoad.ends[0]));

        return (
            sameEnds &&
            this.getType() == otherRoad.getType()
        );
    }
}
