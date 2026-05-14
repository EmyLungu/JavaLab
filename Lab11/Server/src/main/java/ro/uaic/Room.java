package ro.uaic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ro.uaic.entities.Player;
import ro.uaic.entities.Game;
import ro.uaic.entities.Question;
import ro.uaic.entities.Result;
import ro.uaic.repositories.PlayerRepository;
import ro.uaic.repositories.GameRepository;
import ro.uaic.repositories.ResultRepository;
import ro.uaic.repositories.ResultService;
import ro.uaic.repositories.QuestionRepository;

/**
 * Room
 */
public class Room {
    private Map<Socket, Player> players;
    private int maxPlayers;

    private PlayerRepository playerRepository;
    private GameRepository gameRepository;
    private ResultRepository resultRepository;
    private QuestionRepository questionRepository;
    private ResultService resultService;

    public static int QUESTION_TIME = 15_000;
    public static int QUIZ_SIZE = 3;

    public Room(int maxPlayers, GameRepository gameRepo, PlayerRepository playerRepo, ResultRepository resultRepo,
            QuestionRepository questionRepo, ResultService resultService) {
        this.maxPlayers = maxPlayers;
        this.playerRepository = playerRepo;
        this.gameRepository = gameRepo;
        this.resultRepository = resultRepo;
        this.questionRepository = questionRepo;
        this.resultService = resultService;

        this.players = new HashMap<>();

        loadQuestions();
    }

    public synchronized void addPlayer(Socket newSocket, String name) {
        if (this.players.size() == this.maxPlayers)
            return;

        Player newPlayer = new Player(name);
        this.playerRepository.save(newPlayer);
        this.players.put(newSocket, newPlayer);

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

    private void loadQuestions() {
        if (this.questionRepository.count() > 0)
            return;

        try {
            List<List<String>> data = Files.readAllLines(Paths.get("src/main/resources/world_population.csv"))
                    .stream()
                    .map(line -> Arrays.asList(line.split(",")))
                    .collect(Collectors.toList());

            for (int r = 1; r < data.size(); ++r) {
                try {

                    String name = data.get(r).get(0);
                    Double pop = Double.parseDouble(data.get(r).get(2).replaceAll("[^0-9]", ""));
                    Question question = new Question(name, pop);

                    this.questionRepository.save(question);
                } catch (Exception e) {
                    // Skip
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading the questions: " + e);
        }
    }

    private void quiz(int size) {
        Game game = new Game();
        game.setStartTime(LocalDateTime.now());
        gameRepository.save(game);

        List<Question> quizQuestions = null;
        long startTime = System.currentTimeMillis();
        try {
            quizQuestions = questionRepository.findRandomQuestions(size);
            long duration = System.currentTimeMillis() - startTime;

            System.out.println("Query 'findRandomQuestions' executed in: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("Database Error: " + e.getMessage());
            GameServer.stopServer();
            return;
        }

        String msg = "The quiz has started!";
        broadcast(msg);
        System.out.println(msg);

        int numQuestion = 1;
        for (Question q : quizQuestions) {
            for (Map.Entry<Socket, Player> p : this.players.entrySet()) {
                p.getValue().response = 0.0;
            }

            msg = "[" + numQuestion + "/" + QUIZ_SIZE + "] What is the population of " + q.getCountryName() + "?";
            broadcast(msg);
            System.out.println(msg);

            try {
                Thread.sleep(Room.QUESTION_TIME);
            } catch (InterruptedException e) {
                System.err.println("Error thread sleeping");
            }

            double realPopulation = q.getPopulation();
            msg = "The correct anwer was " + String.format("%,.0f", realPopulation) + "!";
            broadcast(msg);

            updateScores(game, q.getPopulation());

            ++numQuestion;
        }

        broadcast("Game over");
        GameServer.stopServer();
    }

    public double getScore(double userValue, double answerValue) {
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

    private void updateScores(Game game, double answerValue) {
        for (Player p : this.players.values()) {
            p.lastScore = getScore(p.response, answerValue);
        }
        List<Map.Entry<Socket, Player>> sortedPlayers = new ArrayList<>(this.players.entrySet());

        sortedPlayers.sort((e1, e2) -> {
            Player p1 = e1.getValue();
            Player p2 = e2.getValue();

            int scoreCompare = Double.compare(p2.lastScore, p1.lastScore);
            if (scoreCompare != 0) {
                return scoreCompare;
            }

            return Long.compare(p1.responseTime, p2.responseTime);
        });

        for (Map.Entry<Socket, Player> p : sortedPlayers) {
            Player player = p.getValue();
            player.addScore(player.lastScore);
            this.playerRepository.save(player);

            Result result = new Result(game, player, player.lastScore);
            this.resultRepository.save(result);

            try {
                PrintWriter out = new PrintWriter(p.getKey().getOutputStream(), true);
                out.printf("You got a score of %.2f\n", player.lastScore);
                out.flush();
            } catch (IOException e) {
                System.out.println("Could not write to player!");
            }
        }

        broadcast(getAllScores());
        System.out.println(getAllScores());
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

    public ResultService getResultService() {
        return this.resultService;
    }
}
