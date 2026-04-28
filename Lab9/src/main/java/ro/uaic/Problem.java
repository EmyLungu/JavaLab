package ro.uaic;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Spinner;
import ro.uaic.entities.Bunny;
import ro.uaic.entities.Entity;
import ro.uaic.entities.Robot;

/**
 * Problem
 */
public class Problem {
    @FXML private Spinner<Integer> widthSpinner;
    @FXML private Spinner<Integer> heightSpinner;
    @FXML private Spinner<Integer> numRobotsSpinner;
    @FXML private Canvas mazeCanvas;

    private Cell[][] cells;
    private int gridWidth;
    private int gridHeight;

    Bunny bunny;
    Robot[] robots;
    private int numRobots;
    private boolean isRunning = false;

    public void initialize() {
        Renderer.initialize(mazeCanvas, cells);
    }

    @FXML
    public void generate() {
        this.gridWidth  = widthSpinner.getValue();
        this.gridHeight = heightSpinner.getValue();
        this.bunny  = null;
        this.robots = null;

        this.cells = new Cell[this.gridHeight][this.gridWidth];
        for (int i = 0; i < gridHeight; ++i) {
            for (int j = 0; j < gridWidth; ++j) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
        Renderer.set(cells, gridWidth, gridHeight);
        Generator.generate(this.cells, this.gridWidth, this.gridHeight);

        Renderer.drawOnCanvas();
    }

    @FXML
    public void spawn() {
        if (this.cells == null || this.bunny != null || this.robots != null)
            return;

        Random rand = new Random();
        int row = rand.nextInt(gridHeight);
        int col = rand.nextInt(gridWidth);
        Cell cell = cells[row][col];
        this.bunny = new Bunny(cell);

        this.numRobots = numRobotsSpinner.getValue();
        this.robots = new Robot[this.numRobots];

        for (int i = 0; i < numRobots; ++i) {
            row = rand.nextInt(gridHeight);
            col = rand.nextInt(gridWidth);
            cell = cells[row][col];

            if (cell.isOccupied() == null) {
                this.robots[i] = new Robot(cell);
            } else {
                --i;
            }
        }

        Renderer.drawOnCanvas();
    }

    @FXML
    public void toggleRandom() {
        Entity.setMovingRandom(!Entity.isMovingRandom());
    }

    @FXML
    public void start() {
        if (isRunning || this.cells == null || this.bunny ==  null || this.robots == null)
            return;

        isRunning = true;
        Renderer.setGameOver(false);
        Robot.initMemory(gridWidth, gridHeight);
        bunny.initMemory(gridWidth, gridHeight);

        DaemonManager.start();

        Thread bunnyThread = new Thread(bunny);
        Thread[] robotThreads = new Thread[this.numRobots];

        bunnyThread.start();
        for (int i = 0; i < this.numRobots; ++i) {
            robotThreads[i] = new Thread(robots[i]);
            robotThreads[i].start();
        }

        KeyboardListener.start(this.bunny, this.robots);

        isRunning = false;
    }
}

