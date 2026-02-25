public class lab {
    public static void main(String[] args) {
        Location iasi = new Location("Iasi", Location.LocationType.CITY, 45.72, 25.17);
        Location aeroport_iasi = new Location("Aeroport Iasi", Location.LocationType.GAS_STATION, 50.11, 22.5);
        aeroport_iasi.setType(Location.LocationType.AIRPORT);

        Road drum_autobuz_50 = new Road(Road.RoadType.HIGHWAY, 523.2, 100);
        drum_autobuz_50 = new Road(Road.RoadType.CITY, iasi, aeroport_iasi, 50);

        System.out.println(iasi);
        System.out.println(aeroport_iasi);
        System.out.println(drum_autobuz_50);
    }
}
