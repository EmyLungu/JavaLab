package ro.uaic.app;

import ro.uaic.com.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * LocaleExplore
 */
public class LocaleExplore {
    private Locale locale;
    private ResourceBundle messages;
    private Map<String, Command> commands;
    boolean running = false;

    public LocaleExplore() {
        this.setLocale(Locale.getDefault());

        this.commands = registerCommands();
    }

    private static Map<String, Command> registerCommands() {
        Map<String, Command> commands = new HashMap<>();
        commands.put("info", new Info());
        commands.put("set", new SetLocale());
        commands.put("locales", new DisplayLocales());

        return commands;
    }

    private void run() throws IOException {
        Scanner scanner = new Scanner(System.in);

        this.running = true;
        while (this.running) {
            System.out.print(messages.getString("prompt") + "\t");

            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) {
                this.running = false;
                break;
            }

            String[] tokens = line.split("\\s+");
            String commandName = tokens[0];
            String[] args = Arrays.stream(tokens)
                .skip(1)
                .toArray(String[]::new);

            Command command = commands.get(commandName);
            if (command != null) {
                String result = command.execute(this, args);
                System.out.println(result);
            } else {
                System.out.println(messages.getString("invalid"));
            }
        }
        scanner.close();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;

        this.messages = ResourceBundle.getBundle("res.Messages", this.locale);
    }

    public ResourceBundle getMessages() {
        return messages;
    }

    public static void main(String[] args) {
        try {
            LocaleExplore app = new LocaleExplore();
            app.run();
        } catch (IOException e) {
            System.err.println("IO Error: " + e);
        }
    }
}
