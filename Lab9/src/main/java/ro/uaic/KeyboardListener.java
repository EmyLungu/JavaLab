package ro.uaic;

import java.util.Scanner;
import ro.uaic.entities.Entity;
import ro.uaic.entities.Bunny;
import ro.uaic.entities.Robot;

public class KeyboardListener {
    public static void start(Bunny bunny, Robot[] robots) {
        Thread commandThread = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            System.out.println("Commands: [pause, resume, delay b <ms>, delay r <all/i> <ms>, stop]");

            while (!Renderer.isGameOver()) {
                if (!sc.hasNextLine()) break;

                String line = sc.nextLine().trim();
                String[] parts = line.split("\\s+");
                String cmd = parts[0].toLowerCase();

                try {
                    switch (cmd) {
                        case "pause":
                            Entity.setPaused(true);
                            System.out.println("Game Paused");
                            break;

                        case "resume":
                            Entity.setPaused(false);
                            synchronized (Entity.pauseLock) {
                                Entity.pauseLock.notifyAll();
                            }
                            System.out.println("Game Resumed");
                            break;

                        case "delay":
                            if (parts.length == 4 && parts[1].equals("r")) {
                                int ms = Integer.parseInt(parts[3]);

                                if (parts[2].equals("all")) {
                                    for (Robot r : robots) {
                                        r.setDelay(ms);
                                    }
                                } else {
                                    int id = Integer.parseInt(parts[2]);
                                    if (id >= 0 && id < robots.length) {
                                        robots[id].setDelay(ms);
                                        System.out.println("Robot " + id + " speed set to " + ms + "ms");
                                    } else {
                                        System.out.println("Invalid Robot ID");
                                    }
                                }
                            } else if (parts.length == 3 && parts[1].equals("b")) {
                                int ms = Integer.parseInt(parts[2]);
                                bunny.setDelay(ms);
                            }
                            break;
                        case "stop":
                            Renderer.setGameOver(true);
                            break;

                        default:
                            System.out.println("Unknown command: " + cmd);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing command.");
                }
            }
            sc.close();
        });
        
        // commandThread.setPriority(Thread.MIN_PRIORITY);
        commandThread.start();
    }
}
