package ro.uaic.homework;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * GameServer
 */
public class GameServer {
    public static final int PORT = 8100;
    private static volatile boolean running;
    private static ServerSocket serverSocket;
    private static ExecutorService pool;

    private static Room room;

    public GameServer(int maxPlayers) throws Exception {
        pool = Executors.newFixedThreadPool(8);

        GameServer.room = new Room(maxPlayers);

        try {
            serverSocket = new ServerSocket(PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown signal received. Cleaning up...");
                stopServer();
            }));

            setRunning(true);
            
            while (running) {
                try {
                    System.out.println ("Waiting for a client ...");
                    Socket socket = serverSocket.accept();

                    pool.execute(new ClientThread(socket));

                } catch (SocketException e) {
                    if (!running) {
                        System.out.println("Server stopped accepting connections.");
                    } else {
                        System.err.println("Socket error: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            stopServer();
        }
    }

    public static void stopServer() {
        if (!isRunning()) return;

        setRunning(false);

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            System.out.println("Shutting down thread pool...");
            pool.shutdownNow();

            if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
            System.out.println("Server shutdown complete.");
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }

    public static synchronized boolean isRunning() {
        return running;
    }

    public static synchronized void setRunning(boolean running) {
        GameServer.running = running;
    }

    public static Room getRoom() {
        return room;
    }
}
