package ro.uaic;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ro.uaic.Problem;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader lab = new FXMLLoader(getClass().getResource("lab.fxml"));
            stage.setTitle("Lab 9 - Emy Lungu");

            Scene scene = new Scene(lab.load(), 1080, 768);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Could not load FXML file! " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
