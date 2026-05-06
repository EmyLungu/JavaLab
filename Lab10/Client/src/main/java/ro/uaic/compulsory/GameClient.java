package ro.uaic.compulsory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * GameClient
 */
public class GameClient {
    private boolean running;
    private boolean serverStopped;

    public GameClient() {
        this.running = false;
        this.serverStopped = true;
    }

    public void run() throws IOException {
        String serverAddress = "127.0.0.1";
        int PORT = 8100;
        try (
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader (
                    new InputStreamReader(socket.getInputStream())) ) {

            this.running = true;
            this.serverStopped = false;
            while (this.running) {
                mainLoop(stdin, in, out);
            }
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        }
    }

    private void mainLoop(BufferedReader stdin, BufferedReader in, PrintWriter out) throws IOException {
        String request = stdin.readLine();

        if (request.equals("exit")) {
            if (!this.serverStopped) {
                out.println("stop");
                String response = in.readLine();
                System.out.println(response);
                this.serverStopped = true;
            }

            this.running = false;
            return;
        }

        if (!this.serverStopped) {
            out.println(request);

            String response = in.readLine();
            System.out.println(response);

            if (response.equals("Server stopped")) {
                this.serverStopped = true;
            }
        }
    }
}
