import java.util.List;

public class lab {
    public static void example() {
        Problem p = new Problem();

        Location iasi       = new CityLocation("Iasi",      14.6, 11.14, 316);
        Location vaslui     = new CityLocation("Vaslui",    9.67, 10.15, 37);
        Location bacau      = new CityLocation("Bacau",     7.68, 9.14,  180);
        Location brasov     = new CityLocation("Brasov",    10.2, 8.390, 237);
        Location bucuresti  = new CityLocation("Bucuresti", 8.12, 9.122, 1900);
        p.addLocation(iasi);
        p.addLocation(vaslui);
        p.addLocation(bacau);
        p.addLocation(brasov);
        p.addLocation(bucuresti);

        Road drumIasiVaslui      = new Road(Road.RoadType.CITY, iasi,      vaslui,    70.0);
        Road drumVasluiBucuresti = new Road(Road.RoadType.CITY, vaslui,    bucuresti, 300.0);
        Road drumIasiBacau       = new Road(Road.RoadType.HIGHWAY, iasi,   bacau,     130.0);
        Road drumBacauBrasov     = new Road(Road.RoadType.HIGHWAY, bacau,  brasov,    170.0);
        Road drumBrasovBucuresti = new Road(Road.RoadType.HIGHWAY, brasov, bucuresti, 180.0);
        p.addRoad(drumIasiVaslui);
        p.addRoad(drumVasluiBucuresti);
        p.addRoad(drumIasiBacau);
        p.addRoad(drumBacauBrasov);
        p.addRoad(drumBrasovBucuresti);

        p.setStart(iasi);
        p.setEnd(bucuresti);

        System.out.println("Validity: " + p.isValid());

        if (p.isValid()) {
            Solution solution = new Solution();
            List<Location> path;

            System.out.println("\nShortest path: ");
            path = solution.solve(p, Solution.Criteria.DISTANCE);
            for (Location loc : path) {
                System.out.println(loc.toString() + " -> ");
            }

            System.out.println("\nFastest path: ");
            path = solution.solve(p, Solution.Criteria.TIME);
            for (Location loc : path) {
                System.out.println(loc.toString() + " -> ");
            }
        }

        // Path 1: (Iasi -> Vaslui -> Bucuresti):
        // Path 2: (Iasi -> Bacau -> Brasov → Bucuresti):
        //
        // Distance:
        // Path 1: 70 + 300 = 370km (Shortest distance)
        // Path 2: 130 + 170 + 180 = 480km
        //
        // Speed:
        // Path 1 (70/50) + (300/50) = 7.4 hours
        // Path 2 (130/150) + (170/150) + (180/150) = 3.19 hours
    }

    public static void largeTest(boolean toPrint) {
        double start = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();

        Problem p = new Problem();
        Generator gen = new Generator();
        gen.generateCities(p, 100_000);
        gen.generateRoads(p, 4_500_000);

        p.setStart(p.locations.get(0));
        p.setEnd(p.locations.get(p.locations.size() - 1));

        Solution solution = new Solution();
        List<Location> path;

        System.out.println("\nShortest path: ");
        path = solution.solve(p, Solution.Criteria.DISTANCE);
        if (toPrint)
            for (Location loc : path) {
                System.out.println(loc.toString() + " -> ");
            }

        System.out.println("\nFastest path: ");
        path = solution.solve(p, Solution.Criteria.TIME);

        if (toPrint)
            for (Location loc : path) {
                System.out.println(loc.toString() + " -> ");
            }

        double end = System.currentTimeMillis();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Execution time: " + (end - start) + "ms");
        System.out.println("Memory used: " + memoryUsed / (1024 * 1024) + " MB");
    }

    public static void main(String[] args) {
        example();
        // largeTest(true);
    }
}
