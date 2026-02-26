/**
 * Generator
 */
public class Generator {
    /**
     * Generates @param numCities CityLocations
     */
    public void generateCities(Problem problem, int numCities) {
        for (int i = 0; i < numCities; ++i) {
            problem.addLocation(new CityLocation("City " + i, i, i, 0));
        }
    }

    /**
     * Generates @param numRoads Roads
     */
    public void generateRoads(Problem problem, int numRoads) {
        Road.RoadType[] types = Road.RoadType.values();
        int numLocations = problem.locations.size();
        numRoads = Math.min(numRoads, numLocations * (numLocations - 1) / 2);

        for (int i = 0; i < numRoads; ++i) {
            Road.RoadType newType = types[(int)(Math.random() * types.length)];
            Location a = problem.locations.get((int)(Math.random() * numLocations));
            Location b = problem.locations.get((int)(Math.random() * numLocations));
            // A road cannot lead to itself
            if (a.equals(b)) {
                --i;
                continue;
            }
            double distance = Math.random();

            problem.addRoad(new Road(newType, a, b, distance));
        }
    }
}
