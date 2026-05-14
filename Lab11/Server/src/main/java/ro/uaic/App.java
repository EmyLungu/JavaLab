package ro.uaic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Server!
 */
@SpringBootApplication
@EnableJpaAuditing
public class App {
    public static void main(String[] args) {
        try {
            Room.QUESTION_TIME = 16_000;
            Room.QUIZ_SIZE = 3;
            int SERVER_SIZE = 2;

            ApplicationContext context = SpringApplication.run(App.class, args);
            GameServer server = context.getBean(GameServer.class);
            server.start(SERVER_SIZE);
        } catch (Exception e) {
            System.err.println("Error inside the GameServer: " + e);
        }
    }
}
