package ro.uaic.homework;

/**
 * Server!
 */
public class App {
    public static void main(String[] args) {
        try {
            GameServer server = new GameServer(2);
        } catch (Exception e){
            System.err.println("Error inside the GameServer: " + e);
        }
    }
}
