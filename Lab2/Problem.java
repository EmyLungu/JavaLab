import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Problem
 */
public class Problem {
    List<Location> locations;
    Location start;
    Location end;

    public Problem() {
        this.locations = new ArrayList<>();
        this.start = null;
        this.end = null;
    }
    
    /**
     * Adds location of to the list if it doesn't exist yet
     */
    public void addLocation(Location newLocation) {
        for (Location location : this.locations) {
            if (location.equals(newLocation)) {
                return;
            }
        }
        this.locations.add(newLocation);
        newLocation.setId(this.locations.size() - 1);
    }

    /**
     * Adds a Road to each of its endpoints
     */
    public void addRoad(Road newRoad) {
        for (Location loc : newRoad.getEnds()) { // Foreach endpoint of the new road
            // If there road deosnt exist yet -> add the new road the the endpoint
            for (Road road : loc.roads) {
                if (road.equals(newRoad)) {
                    return;
                }
            }

            loc.roads.add(newRoad);
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

        // BFS Algorithm
        boolean[] visited = new boolean[this.locations.size()];
        Queue<Location> q = new LinkedList<>();
        q.add(this.start);
        visited[this.start.getId()] = true;

        while (!q.isEmpty()) {
            Location loc = q.poll();
            visited[loc.getId()] = true;

            if (loc.equals(this.end))
                return true;

            for (Road road : loc.roads) {
                // The road has 2 ends, we get the one that is different from where we are
                Location[] ends = road.getEnds();
                Location neighbour = (ends[0].equals(loc) ? ends[1] : ends[0]);

                if (!visited[neighbour.getId()]) {
                    q.add(neighbour);
                }
            }
        }

        return false;
    }
}
