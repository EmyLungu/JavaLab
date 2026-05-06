package ro.uaic.homework;

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
    private static boolean running;
    private boolean isAnswer;

    public GameClient() {
        GameClient.running = false;
    }

    public void run() throws IOException {
        String serverAddress = "127.0.0.1";
        int PORT = 8100;
        try (
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {


            GameClient.setRunning(true);

            Thread listener = new Thread(new Listener(in, out));
            listener.start();

            System.out.println("Commands: [join, stop, exit, ans, stop ans]");

            while (GameClient.running) {
                mainLoop(stdin, in, out);
            }

            listener.join();
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (InterruptedException e) {
            System.err.println("Error joining listener thread: " + e);
        }
    }

    private void mainLoop(BufferedReader stdin, BufferedReader in, PrintWriter out) throws IOException {
        String request = stdin.readLine();

        if (request.equals("stop")) {
            out.println(Commands.STOP_CLIENT.toString());
        }
        else if (request.equals("exit")) {
            out.println(Commands.STOP_CLIENT.toString());
            setRunning(false);
        }
        else if (request.equals("join")) {
            out.println(Commands.JOIN_GAME.toString());
        }
        else if (request.equals("ans")) {
            this.isAnswer = true;
        }
        else if (request.equals("stop ans")) {
            this.isAnswer = false;
        }
        else {
            if (this.isAnswer) {
                out.println(Commands.ANSWER.toString());
            }

            out.println(request);
        }
        out.flush();
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        GameClient.running = running;
    }
}
