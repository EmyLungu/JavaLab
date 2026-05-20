package ro.uaic.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * RandomBot
 */
public class CustomBot extends Bot {
    private Map<String, String> memory;

    public CustomBot() {
        this.memory = new HashMap<>();
    }

    protected String getAnswer(String question) {
        String value = this.memory.get(question);
        if (value != null) {
            return value;
        }

        Random rand = new Random();
        Integer valueRandom = rand.nextInt(30_000, 100_000_000);
        return valueRandom.toString();
    }

    @Override
    protected void processAnswer(String question, String answer) {
        this.memory.put(question, answer);
    }

    public static void main(String[] args) {
        try {
            Bot bot = new CustomBot();
            bot.run();
        } catch (Exception e){
            System.err.println("Error inside the Bot: " + e);
        }
    }
}
