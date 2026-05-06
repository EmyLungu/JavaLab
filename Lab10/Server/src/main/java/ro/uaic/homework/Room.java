package ro.uaic.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Room
 */
public class Room {
    private Map<Socket, Player> players;
    private int maxPlayers;

    private Map.Entry<String, String> currentQuestion;

    private static int QUESTION_TIME = 15_000;
    private static int QUIZ_SIZE = 3;

    public Room(int maxPlayers) {
        this.maxPlayers = maxPlayers;

        this.players = new HashMap<>();

        currentQuestion = null;
    }

    public synchronized void addPlayer(Socket newSocket) {
        if (this.players.size() == this.maxPlayers)
            return;

        this.players.put(newSocket, new Player());

        try {
            PrintWriter out = new PrintWriter(newSocket.getOutputStream(), true);
            out.println("You joined the game " + this.players.size() + "/" + this.maxPlayers);
            out.flush();

            System.out.println(newSocket.getRemoteSocketAddress() + " joined the game!");
        } catch (IOException e) {
            System.err.println("Could not write to player [JOIN]");
        }

        if (canStart()) {
            Thread quizThread = new Thread(() -> {
                quiz(Room.QUIZ_SIZE);
            });

            quizThread.start();
        }
    }

    public synchronized boolean canStart() {
        return (this.players.size() == this.maxPlayers);
    }

    public synchronized void broadcast(String message) {
        for (Socket socket : this.players.keySet()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("Error notifying player: " + e.getMessage());
            }
        }
    }

    private Map<String, String> getAllQuestions() {
        Map<String, String> questions = new HashMap<>();

        try {
            List<List<String>> data = Files.readAllLines(Paths.get("src/resources/world_population.csv"))
                    .stream()
                    .map(line -> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());

            for (int r = 1; r < data.size(); ++r) {
                questions.put(data.get(r).get(0), data.get(r).get(2));
            }
        } catch (Exception e) {
            System.err.println("Error loading the questions");
        }

        return questions;
    }

    public Map<String, String> getQuestionList(Map<String, String> questions, int size) {
        Map<String, String> questionList = new HashMap<>();

        List<String> keys = new ArrayList<>(questions.keySet());
        Collections.shuffle(keys);

        keys.stream()
                .limit(size)
                .forEach(key -> {
                    questionList.put(key, questions.get(key));
                });

        return questionList;
    }

    private void quiz(int size) {
        Map<String, String> questions = getAllQuestions();
        Iterator<Map.Entry<String, String>> questionsIter = getQuestionList(questions, size)
                .entrySet().iterator();

        String msg = "The quiz has started!";
        broadcast(msg);
        System.out.println(msg);

        int numQuestion = 1;
        while (questionsIter.hasNext()) {
            Map.Entry<String, String> entry = questionsIter.next();
            this.currentQuestion = entry;

            msg = "[" + numQuestion + "/" + 5 + "] What is the population of " + entry.getKey() + "?";
            broadcast(msg);
            System.out.println(msg);

            try {
                Thread.sleep(Room.QUESTION_TIME);
            } catch (InterruptedException e) {
                System.err.println("Error thread sleeping");
            }


            long realPopulation = (long) Integer.parseInt(entry.getValue());
            msg = "The correct anwer was " + String.format("%,d", realPopulation) + "!";
            broadcast(msg);
            broadcast(getAllScores());

            ++numQuestion;
        }

        broadcast("Game over");
        GameServer.stopServer();
    }

    public double getScore(String response) {
        double userValue = parsePopulation(response);

        String answerString = this.currentQuestion.getValue();
        double answerValue = (double) Integer.parseInt(answerString);

        double log = Math.log10(userValue / answerValue);
        double val = 1 - Math.abs(log);

        return Double.max(0.0, 100 * val);
    }

    public static double parsePopulation(String input) {
        String cleanInput = input.toLowerCase().trim().replaceAll("[\\s,]", "");

        long multiplier = 1;
        String numericPart = cleanInput;

        if (cleanInput.endsWith("k")) {
            multiplier = 1_000L;
            numericPart = cleanInput.substring(0, cleanInput.length() - 1);
        } else if (cleanInput.endsWith("mil") || cleanInput.endsWith("m")) {
            multiplier = 1_000_000L;
            int offset = cleanInput.endsWith("mil") ? 3 : 1;
            numericPart = cleanInput.substring(0, cleanInput.length() - offset);
        } else if (cleanInput.endsWith("bil") || cleanInput.endsWith("b")) {
            multiplier = 1_000_000_000L;
            int offset = cleanInput.endsWith("bil") ? 3 : 1;
            numericPart = cleanInput.substring(0, cleanInput.length() - offset);
        }

        try {
            double baseValue = Double.parseDouble(numericPart);
            return (baseValue * multiplier);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + input);
            return 0.0;
        }
    }

    public Player getPlayer(Socket socket) {
        return players.get(socket);
    }

    public String getAllScores() {
        String output = "Scores:\n";

        int idx = 1;
        for (Player p : this.players.values()) {
            output += String.format("\tPlayer %d [%.2f points]\n", idx, p.getScore());
            ++idx;
        }

        return output;
    }
}
