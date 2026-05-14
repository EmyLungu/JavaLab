package ro.uaic;

import ro.uaic.entities.Player;
import ro.uaic.entities.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

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
            if (GameServer.isRunning()) {
                System.err.println("Communication error... " + e);
            }
        }
    }

    private void handleCommands(BufferedReader in, PrintWriter out, Socket socket) throws IOException {
        String request = in.readLine();
        if (request == null) return;

        Room room = GameServer.getRoom();

        if (request.equals(Commands.STOP_CLIENT.toString())) {
            out.println("Server stopped");
            out.flush();
            this.running = false;
        } else if (request.equals(Commands.JOIN_GAME.toString())) {
            String name = in.readLine();

            room.addPlayer(socket, name);

        } else if (request.equals(Commands.ANSWER.toString())) {
            Player currentPlayer = room.getPlayer(socket);

            if (currentPlayer != null) {
                String response = in.readLine();
                double value = Room.parsePopulation(response);
                currentPlayer.response = value;
                currentPlayer.responseTime = System.currentTimeMillis();
            }
        } else if (request.equals(Commands.QUERY.toString())) {
            String prefix = in.readLine();
            String minScore = in.readLine();
            String date = in.readLine();
            
            List<Result> results = room.getResultService().resultQuery(prefix, minScore, date);

            if (results.isEmpty()) {
                out.println("No data found");
            }
            for (Result r : results) {
                out.printf("%s got a score of: %.2f at the game [%d] at (%s)\n",
                        r.getPlayer().getName(),
                        r.getScore(),
                        r.getGame().getId(),
                        r.getGame().getStartTime());
            }
            out.flush();
        } else {
            out.println("Server received the request [" + request + "]");
            out.flush();
            System.out.println("Got request [" + request + "] from [" + socket.getInetAddress() + "]");
        }
    }
}
