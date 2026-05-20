package ro.uaic.homework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Bot
 */
public abstract class Bot extends Thread {
    private static boolean running;
    private String last_question = "";

    public Bot() {
        Bot.running = false;
    }

    public void run() {
        String serverAddress = "127.0.0.1";
        int PORT = 8100;
        try (
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {

            out.println(Commands.JOIN_GAME.toString());
            waitToStart(in);

            Bot.setRunning(true);
            while (Bot.running) {
                mainLoop(in, out);
            }
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        } catch (IOException e) {
            System.err.println("IOException... " + e);
        }
    }

    private void waitToStart(BufferedReader in) {
        try {
            String response = in.readLine();
            while (!response.equals("The quiz has started!")) {
                response = in.readLine();
            }
        } catch (IOException e){
        }
    }

    private void mainLoop(BufferedReader in, PrintWriter out) throws IOException {
        String input = in.readLine();
        if (input == null) {
            setRunning(false);
            return;
        }

        if (input.endsWith("?")) {
            String answer = getAnswer(input);
            out.println(Commands.ANSWER.toString());
            out.println(answer);
            out.flush();

            last_question = input;

            System.out.println("My answer: " + answer);

        }

        Pattern pattern = Pattern.compile("The correct anwer was ([\\d,]+)!");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String realPopulation= matcher.group(1);

            this.processAnswer(last_question, realPopulation);
        }
    }

    protected abstract String getAnswer(String question);
    protected abstract void processAnswer(String question, String answer);

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Bot.running = running;
    }
}
