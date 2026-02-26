/**
 * GasStationLocation
 */
public final class GasStationLocation extends Location {
    int gasPrice;

    public GasStationLocation(String name, double x, double y, int gasPrice) {
        this.gasPrice = gasPrice;
        super(name, x, y);
    }

    public int getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
    }
}
