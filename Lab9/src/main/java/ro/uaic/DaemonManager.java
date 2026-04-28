package ro.uaic;

/**
 * DaemonManager
 */
public class DaemonManager {
    public static void start() {
        Thread manager = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (!Renderer.isGameOver()) {
                try {
                    Thread.sleep(10_000);
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    System.out.println("Time: " + elapsed + "s");

                    if (elapsed > 60) {
                        System.out.println("Time limit reached!");
                        Renderer.setGameOver(true);
                    }
                } catch (InterruptedException e) { break; }
            }
        });
        manager.setDaemon(true); 
        manager.start();
    }
}
