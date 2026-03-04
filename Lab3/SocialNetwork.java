import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * SocialNetwork
 */
public class SocialNetwork {
    List<Profile> profiles;

    SocialNetwork() {
        this.profiles = new ArrayList<>();
    }

    public void add(Profile newProfile) {
        this.profiles.add(newProfile);
    }

    public int calculateImportance(Profile p) {
        return p.getRelationships().size();
    }

    public void sortByImportance() {
        profiles.sort((a, b) -> {
            int importanceA = calculateImportance(a);
            int importanceB = calculateImportance(b);

            return Integer.compare(importanceB, importanceA);
        });
    }

    public void print() {
        System.out.println("Social Network:");
        for (Profile profile : this.profiles) {
            System.out.println(profile.toString());
        }
    }

    static class Edge {
        Profile u;
        Profile v;
        Edge(Profile u, Profile v) {
            this.u = u;
            this.v = v;
        }
    }
    /*  Helper function to perform DFS and find articulation points
     *  using Tarjan's algorithm.
     *  https://www.geeksforgeeks.org/dsa/articulation-points-or-cut-vertices-in-a-graph/
     */
    void findPoints(
            Profile u,
            int[] visited,
            int[] disc,
            int[] low,
            int[] time,
            Profile parent,
            int[] isAP,
            Stack<Edge> stack,
            List<Set<Profile>> maximal
    ) {
        // Mark vertex u as visited and assign discovery
        // time and low value
        visited[u.getID()] = 1;
        disc[u.getID()] = low[u.getID()] = ++time[0];
        int children = 0; 

        // Process all adjacent vertices of u
        for (Profile v : u.getRelationships().keySet()) {
            // If v is not visited, then recursively visit it
            if (visited[v.getID()] == 0) {
                children++;
                stack.add(new Edge(u, v));

                findPoints(v, visited, disc, low, time, u, isAP, stack, maximal);

                // Check if the subtree rooted at v has a 
                // connection to one of the ancestors of u
                low[u.getID()] = Math.min(low[u.getID()], low[v.getID()]);

                // If u is not a root and low[v] is greater 
                // than or equal to disc[u],
                // then u is an articulation point
                if (low[v.getID()] >= disc[u.getID()]) {
                    
                    if (parent != null)
                        isAP[u.getID()] = 1;

                    Set<Profile> currentComponent = new HashSet<>();

                    Edge top;
                    do {
                        top = stack.pop();
                        currentComponent.add(top.u);
                        currentComponent.add(top.v);
                    } while (top.u != u || top.v != v);

                    maximal.add(currentComponent);
                }
            } 

            // Update low value of u for back edge
            else if (v != parent) {
                low[u.getID()] = Math.min(low[u.getID()], disc[v.getID()]);

                if (disc[v.getID()] < disc[u.getID()]) {
                    stack.push(new Edge(u, v));
                }
            }
        }

        // If u is root of DFS tree and has more than 
        // one child, it is an articulation point
        if (parent == null && children > 1) {
            isAP[u.getID()] = 1;
        }
    }

    void articulationPoints(List<Profile> aPoints, List<Set<Profile>> maximal) {
        int V = this.profiles.size();
        int[] disc = new int[V], low = new int[V],
        visited = new int[V], isAP = new int[V];
        int[] time = {0}; 

        Stack<Edge> stack = new Stack<>();

        for (Profile u : this.profiles) {
            if (visited[u.getID()] == 0) {
                findPoints(u, visited, disc, low, time, null, isAP, stack, maximal);
            }
        }

        for (Profile u : this.profiles) {
            if (isAP[u.getID()] == 1) {
                aPoints.add(u);
            }
        }
    }
}
