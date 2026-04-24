package ro.uaic;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Advanced Controller
 */
public class Advanced {
    @FXML private Spinner<Integer> widthSpinner; 
    @FXML private Spinner<Integer> heightSpinner; 
    @FXML private Canvas mazeCanvas;
    @FXML private Spinner<Integer> delaySpinner; 

    private int gridWidth = 10;
    private int gridHeight = 10;
    private Cell[][] cells;

    private Cell startCell = null;
    private Cell endCell = null;

    public void initialize() {
        Renderer.initialize(mazeCanvas, cells);

        delaySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            Generator.setDelay(newVal);
        });
    }

    @FXML
    public void render() {
        gridWidth = widthSpinner.getValue();
        gridHeight = heightSpinner.getValue();
        cells = new Cell[gridHeight][gridWidth];

        for (int r = 0; r < gridHeight; r++) {
            for (int c = 0; c < gridWidth; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }

        startCell = cells[0][0];
        endCell = cells[gridHeight - 1][gridWidth - 1];

        Renderer.set(cells, gridWidth, gridHeight);
        Renderer.drawOnCanvas();
    }

    @FXML
    private void validate() {
        if (cells == null || startCell == null || endCell == null) {
            System.err.println("There are no cells");
            return;
        }

        if (Validator.validate(cells, gridWidth, gridHeight, startCell, endCell)) {
            System.out.println("The maze is valid");
        } else {
            System.out.println("The maze is not valid");
        }
    }

    @FXML
    private void exportToPNG() {
        WritableImage image = mazeCanvas.snapshot(null, null);

        File file = new File("maze_export.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("Maze exported to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save image: " + e.getMessage());
        }
    }

    // @FXML
    // private void createMaze() {
    //     if (cells == null)
    //         return;
    //
    //     Random rand = new Random();
    //     for (int r = 0; r < gridHeight; r++) {
    //         for (int c = 0; c < gridWidth; c++) {
    //             if (rand.nextBoolean()) toggleTopWall(r, c, false); 
    //             if (rand.nextBoolean()) toggleRightWall(r, c, false); 
    //             if (rand.nextBoolean()) toggleBottomWall(r, c, false); 
    //             if (rand.nextBoolean()) toggleLeftWall(r, c, false); 
    //         }
    //     }
    //     drawOnCanvas();
    // }

    @FXML 
    private void resetMaze() { 
        render(); 
    }
    
    @FXML 
    private void exitApp() { 
        System.exit(0); 
    }


    @FXML
    private void handleSave() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files", "*.ser"));
        File file = fc.showSaveDialog(null);
        if (file != null) saveMaze(file);
    }

    @FXML
    private void handleLoad() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) loadMaze(file);
    }

    private void saveMaze(File file) {
        MazeData data = new MazeData(cells, gridWidth, gridHeight, startCell, endCell);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(data);
            System.out.println("Maze saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMaze(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            MazeData data = (MazeData) in.readObject();

            this.cells = data.getCells();
            this.gridWidth = data.getGridWidth();
            this.gridHeight = data.getGridHeight();
            this.startCell = data.getStartCell();
            this.endCell = data.getEndCell();

            Renderer.drawOnCanvas();
            System.out.println("Maze restored successfully!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generate() {
        if (cells == null)
            return;

        if (Generator.isRunning())
            return;

        System.out.println("Generating...");
        Thread genThread = new Thread(() -> Generator.generate(cells, gridWidth, gridHeight));
        genThread.start();
    }
}
