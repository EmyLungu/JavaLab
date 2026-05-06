package ro.uaic.compulsory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientThread
 */
public class ClientThread extends Thread {
    private final Socket socket;
    private boolean running;

    public ClientThread (Socket socket) {
        this.socket = socket;
        this.running = false;
    }

    public void run () {
        try (socket) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            this.running = true;
            while (this.running) {
                mainLoop(in, out, socket);
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        }
    }

    private void mainLoop(BufferedReader in, PrintWriter out, Socket socket) throws IOException {
        String request = in.readLine();
        if (request == null) return;

        if (request.equals("stop")) {
            out.println("Server stopped");
            this.running = false;
        } else if (request.equals("Hello")) {
            out.println("World!");
        } else {
            out.println("Server received the request [" + request + "]");
        }
        System.out.println("Got request [" + request + "] from [" + socket.getInetAddress() + "]");
        out.flush();
    }
}
