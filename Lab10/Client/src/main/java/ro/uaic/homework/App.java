package ro.uaic.homework;

/**
 * Client
 */
public class App {
    public static void main(String[] args) {
        try {
            GameClient client = new GameClient();
            client.run();
        } catch (Exception e){
            System.err.println("Error inside the GameClient: " + e);
        }
    }
}
