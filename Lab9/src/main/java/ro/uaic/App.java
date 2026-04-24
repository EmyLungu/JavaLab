package ro.uaic;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader compulsory = new FXMLLoader(getClass().getResource("compulsory.fxml"));
            Parent compulsoryP = FXMLLoader.load(getClass().getResource("compulsory.fxml"));
            Parent homework = FXMLLoader.load(getClass().getResource("homework.fxml"));
            Parent advanced = FXMLLoader.load(getClass().getResource("advanced.fxml"));
            stage.setTitle("Lab 9 - Emy Lungu");

            Scene scene = new Scene(compulsory.load(), 768, 768);
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DIGIT1) {
                    stage.getScene().setRoot(compulsoryP);
                } else if (event.getCode() == KeyCode.DIGIT2) {
                    stage.getScene().setRoot(homework);
                } else if (event.getCode() == KeyCode.DIGIT3) {
                    stage.getScene().setRoot(advanced);
                }
            });

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
