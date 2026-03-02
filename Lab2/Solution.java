import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Solution
 */
public class Solution {
    public enum Criteria {
        DISTANCE,
        TIME,
    }

    /**
     * We solve the problem based on the @param criteria (DISTANCE / TIME) using the Dijksta's algorithm
     * https://www.geeksforgeeks.org/dsa/dijkstras-shortest-path-algorithm-greedy-algo-7/
     */
    List<Location> solve(Problem problem, Criteria criteria) {
        // Auxiliary class used for the PriorityQueue
        class Node {
            Location location;
            double distance;

            Node(Location location, double distance) {
                this.location = location;
                this.distance = distance;
            }
        }
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(a.distance, b.distance));

        HashMap<Location, Double> dist = new HashMap<>();       // The distances of all paths starting from the start Location
        HashMap<Location, Location> parent = new HashMap<>();   // A parent vector o be able to construct the shortest path

        dist.put(problem.start, 0.0);
        pq.offer(new Node(problem.start, 0.0));

        while (!pq.isEmpty()) {
            Node top = pq.poll();

            if (top.distance > dist.getOrDefault(top.location, Double.MAX_VALUE))
                continue;

            if (top.location.equals(problem.end))
                break;

            ArrayList<Road> roads = problem.locations.get(top.location);

            for (Road road : roads) {
                double weight = 0;
                if (criteria == Criteria.DISTANCE) {
                    weight = road.length;
                } else if (criteria == Criteria.TIME) {
                    weight = road.length / road.getSpeedLimit();
                }

                Location neighbour = road.target;

                // Add the default value if it was not reached yet
                dist.putIfAbsent(neighbour, Double.MAX_VALUE);

                // We add to the priority queue the neighbours for which we had found a shorther path
                if (top.distance + weight < dist.get(neighbour)) {
                    dist.put(neighbour, top.distance + weight);             // Update the dist to the new shorter path
                    parent.put(neighbour, top.location);                    // Because this is how we got the shortest path from
                                                                            // Location A to Location B, A will be the parent of B
                    pq.offer(new Node(neighbour, dist.get(neighbour)));     // Add the neighbour to search for new shorter paths
                }
            }
        }

        List<Location> result = new ArrayList<>();

        // Check if a path exists
        if (dist.getOrDefault(problem.end, Double.MAX_VALUE) == Double.MAX_VALUE) {
            System.err.println("Unsolvable problem");
            return result;
        }        

        // Creation of the shortest path
        for (Location i = problem.end; i != null; i = parent.get(i)) {
            result.add(i);
        }
        Collections.reverse(result);

        return result;
    }
}
