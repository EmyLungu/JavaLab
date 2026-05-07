package ro.uaic.homework;

import java.util.Random;

/**
 * RandomBot
 */
public class RandomBot extends Bot {
    protected String getAnswer(String question) {
        Random rand = new Random();
        Integer value = rand.nextInt(30_000, 100_000_000);

        return value.toString();
    }

    public static void main(String[] args) {
        try {
            Bot bot = new RandomBot();
            bot.run();
        } catch (Exception e){
            System.err.println("Error inside the Bot: " + e);
        }
    }
}
