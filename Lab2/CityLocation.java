/**
 * CityLocation
 */
public final class CityLocation extends Location {
    int population;

    public CityLocation(String name, double x, double y, int population) {
        this.population = population;
        super(name, x, y);
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
