package ro.uaic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.spanning.WeightedSpanningTreeIterator;
import org.graph4j.spanning.KruskalMinimumSpanningTree;

import com.github.javafaker.Faker;

/**
 * City
 */
public class City {
    private String name;
    private List<Intersection> intersections;
    private List<Street> streets;

    public City(String name) {
        this.name = name;

        this.intersections = new LinkedList<>();
        this.streets = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void addIntersection(Intersection newIntersection) {
        this.intersections.add(newIntersection);
    }

    public void addStreet(Street newStreet) {
        this.streets.add(newStreet);
    }

    public void generate(int numIntersections, int numStreets) {
        Faker faker = new Faker();

        this.intersections = IntStream.range(0, numIntersections)
            .mapToObj(i -> new Intersection(faker.address().city()))
            .collect(Collectors.toList());

        for (int i = 0; i < numStreets; i++) {
            int u = (int) (Math.random() * numIntersections);
            int v = (int) (Math.random() * numIntersections);

            if (u != v) {
                Intersection a = intersections.get(u); 
                Intersection b = intersections.get(v); 

                // int dist = (int) (Math.random() * 100);
                int dist = (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));

                addStreet(
                    new Street(
                    faker.address().streetName(), 
                    dist, 
                    a,
                    b
                ));
            }
        }
    }

    public void query(int value) {
        Map<Intersection, Long> degree = this.streets.stream()
            .flatMap(s -> Stream.of(s.getA(), s.getB()))
            .collect(Collectors.groupingBy(
                intersection -> intersection,
                Collectors.counting()
            ));

        this.streets.stream()
            .filter(s -> s.getLength() > value)
            .filter(s -> degree.get(s.getA()) >= 3 ||
                         degree.get(s.getB()) >= 3)
            .forEach(System.out::println);
    }

    public void get_k_msts(int numSolutions) {
        Graph graph = GraphBuilder
            .numVertices(this.intersections.size())
            .buildGraph();

        for (Street s : this.streets) {
            int u = intersections.indexOf(s.getA());
            int v = intersections.indexOf(s.getB());
            graph.addEdge(u, v, s.getLength());
        }

        WeightedSpanningTreeIterator it = new WeightedSpanningTreeIterator(graph);

        int count = 0;
        while (it.hasNext() && count < numSolutions) {
            Collection<Edge> solutionTree = it.next();
            count++;

            double totalWeight = 0.0;
            for (Edge e : solutionTree) {
                totalWeight += e.weight();

                String uName = intersections.get(e.source()).getName();
                String vName = intersections.get(e.target()).getName();
                System.out.println("\t" + uName + " <--> " + vName + " (" + e.weight() + ")");
            }

            System.out.println("Total weight: " + totalWeight);
        }
    }

    public void tsp() {
        Graph graph = GraphBuilder
            .numVertices(this.intersections.size())
            .buildGraph();

        for (int i = 0; i < intersections.size(); ++i) {
            for (int j = i + 1; j < intersections.size(); ++j) {
                Intersection a = intersections.get(i);
                Intersection b = intersections.get(j);
                double dist = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
                graph.addEdge(i, j, dist);
            }
        }

        KruskalMinimumSpanningTree mstAlg = new KruskalMinimumSpanningTree(graph);
        Collection<Edge> mstEdges = mstAlg.getEdges();

        List<Integer>[] adj = new List[this.intersections.size()];
        for (int i = 0; i < this.intersections.size(); ++i) {
            adj[i] = new ArrayList<>();
        }

        for (Edge e : mstEdges) {
            adj[e.source()].add(e.target());
            adj[e.target()].add(e.source());
        }

        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[this.intersections.size()];
        dfs(0, adj, visited, tour);

        System.out.println("Route (2-Approximation):");
        double totalDist = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int u = tour.get(i);
            int v = tour.get(i + 1);
            System.out.print(intersections.get(u).getName() + " -> ");
            totalDist += graph.getEdgeWeight(u, v);
        }

        int last = tour.get(tour.size() - 1);
        int first = tour.get(0);
        System.out.println(intersections.get(first).getName());
        totalDist += graph.getEdgeWeight(last, first);

        System.out.println("Total Tour Length: " + totalDist);
    }

    private void dfs(int u, List<Integer>[] adj, boolean[] visited, List<Integer> tour) {
        visited[u] = true;
        tour.add(u);

        for (int v : adj[u]) {
            if (!visited[v]) {
                dfs(v, adj, visited, tour);
            }
        }
    }
}
