package ro.uaic.compulsory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * GameServer
 */
public class GameServer {
    public static final int PORT = 8100;

    public GameServer() throws IOException {
        try (
                ServerSocket serverSocket = new ServerSocket(PORT);
            ) {
            
            while (true) {
                System.out.println ("Waiting for a client ...");
                Socket socket = serverSocket.accept();

                new ClientThread(socket).start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
