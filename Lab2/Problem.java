import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem
 */
public class Problem {
    HashMap<Location, ArrayList<Road>> locations;

    Location start;
    Location end;

    /**
     * Initializes the problem
     */
    public Problem() {
        this.locations = new HashMap<>();
        this.start = null;
        this.end = null;
    }
    
    /**
     * Adds location of to the list if it doesn't exist yet
     */
    public void addLocation(Location newLocation) {
        if (!this.locations.containsKey(newLocation)) {
            this.locations.put(newLocation, new ArrayList<>());
        }
    }

    /**
     * Adds a Road @param newRoad to a Location @param location
     */
    public void addRoad(Location location, Road newRoad) {
        ArrayList<Road> loc1 = this.locations.get(location);

        if (loc1 != null) {
            loc1.add(newRoad);
        }
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    /**
     * Checks the there exists a path from start to end using the plain BFS Algorithm
     */
    public boolean isValid() {
        if (this.start == null || this.end == null)
            return false;

        HashSet<Location> visited = new HashSet<>();
        Queue<Location> q = new LinkedList<>();
        q.add(this.start);
        visited.add(this.start);

        while (!q.isEmpty()) {
            Location loc = q.poll();
            visited.add(loc);

            if (loc.equals(this.end))
                return true;

            // Add the unvisited neighbours
            for (Road road : locations.get(loc)) {
                if (!visited.contains(road.target)) {
                    q.add(road.target);
                }
            }
        }

        return false;
    }
}
