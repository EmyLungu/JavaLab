package ro.uaic.homework;

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
    private volatile boolean running;

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
                handleCommands(in, out, socket);
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        }
    }

    private void handleCommands(BufferedReader in, PrintWriter out, Socket socket) throws IOException {
        String request = in.readLine();
        if (request == null) return;

        Room room = GameServer.getRoom();

        if (request.equals(Commands.STOP_CLIENT.toString())) {
            out.println("Server stopped");
            this.running = false;
        } else if (request.equals(Commands.JOIN_GAME.toString())) {
            room.addPlayer(socket);
        } else if (request.equals(Commands.ANSWER.toString())) {
            Player currentPlayer = room.getPlayer(socket);

            if (currentPlayer != null) {
                String response = in.readLine();
                double score = room.getScore(response);

                currentPlayer.addScore(score);
                out.printf("You got a score of %.2f\n", score);
            }
        } else {
            out.println("Server received the request [" + request + "]");
            System.out.println("Got request [" + request + "] from [" + socket.getInetAddress() + "]");
        }
    }
}
