/**
 * AirportLocation
 */
public final class AirportLocation extends Location {
    int numTerminals;

    public AirportLocation(String name, double x, double y, int numTerminals) {
        this.numTerminals = numTerminals;
        super(name, x, y);
    }

    public int getNumTerminals() {
        return numTerminals;
    }

    public void setNumTerminals(int numTerminals) {
        this.numTerminals = numTerminals;
    }
}
