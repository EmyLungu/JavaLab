import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Solution
 */
public class Solution {
    public enum Criteria {
        DISTANCE,
        TIME,
    }

    // Auxiliary class used for the PriorityQueue
    private static class Node {
        double distance;
        int id;

        Node(double distance, int id) {
            this.distance = distance;
            this.id = id;
        }
    }

    /**
     * We solve the problem based on the @param criteria (DISTANCE / TIME) using the Dijksta's algorithm
     * https://www.geeksforgeeks.org/dsa/dijkstras-shortest-path-algorithm-greedy-algo-7/
     */
    List<Location> solve(Problem problem, Criteria criteria) {
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(a.distance, b.distance));

        double[] dist = new double[problem.locations.size()];
        int[] parent = new int[problem.locations.size()];       // To be able to construct the shortest path
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(parent, -1);

        dist[problem.start.getId()] = 0;
        pq.offer(new Node(0.0, problem.start.getId()));

        while (!pq.isEmpty()) {
            Node top = pq.poll();

            if (top.distance > dist[top.id])
                continue;

            Location topLoc = problem.locations.get(top.id);

            for (Road road : topLoc.roads) {
                // We get the neighbour of the top Location
                Location[] ends = road.getEnds();
                Location neighbour = (ends[0].equals(topLoc) ? ends[1] : ends[0]);

                double weight = 0;
                if (criteria == Criteria.DISTANCE) {
                    weight = road.length;
                } else if (criteria == Criteria.TIME) {
                    weight = road.length / road.getSpeedLimit();
                }

                // We add to the priority queue the neighbours for which we had found a shorther path
                if (dist[top.id] + weight < dist[neighbour.id]) {
                    dist[neighbour.id] = dist[top.id] + weight;
                    pq.offer(new Node(dist[neighbour.id], neighbour.id));

                    parent[neighbour.id] = top.id;
                }
            }
        }

        List<Location> result = new ArrayList<>();

        // Check if a path exists
        if (dist[problem.end.getId()] == Double.MAX_VALUE) {
            System.err.println("Unsolvable problem");
            return result;
        }        

        // Creation of the shortest path
        for (int i = problem.end.getId(); i != -1; i = parent[i]) {
            result.add(problem.locations.get(i));
        }
        Collections.reverse(result);

        return result;
    }
}
