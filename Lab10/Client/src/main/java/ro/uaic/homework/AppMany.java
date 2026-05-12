package ro.uaic.homework;

/**
 * Client
 */
public class AppMany {
    public static void main(String[] args) {
        try {
            int numBots = 12_000;
            Thread[] threads = new Thread[numBots];

            for (int i = 0; i < numBots; ++i) {
                threads[i] = new Thread(new RandomBot());
                threads[i].start();
            }

            for (int i = 0; i < numBots; ++i) {
                threads[i].join();
            }
        } catch (Exception e){
            System.err.println("Error inside the GameClient: " + e);
        }
    }
}
