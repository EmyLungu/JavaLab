package ro.uaic.compulsory;

/**
 * Server!
 */
public class App {
    public static void main(String[] args) {
        try {
            GameServer server = new GameServer();
        } catch (Exception e){
            System.err.println("Error inside the GameServer: " + e);
        }
    }
}
