package ro.uaic.homework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Listener
 */
public class Listener extends Thread {
    BufferedReader in;
    PrintWriter out;
    
    public Listener(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }
    @Override
    public void run() {
        while (GameClient.isRunning()) {
            try {
                String message = in.readLine();
                if (message == null)
                    continue;

                System.out.println(message);
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e);
            }
        }
    }
}
