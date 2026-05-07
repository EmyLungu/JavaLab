package ro.uaic.homework;

/**
 * Server!
 */
public class App {
    public static void main(String[] args) {
        try {
            Room.QUESTION_TIME = 3_000;
            Room.QUIZ_SIZE = 3;
            GameServer server = new GameServer(50);
        } catch (Exception e){
            System.err.println("Error inside the GameServer: " + e);
        }
    }
}
