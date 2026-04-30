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
    private static Random rand = new Random();
    @FXML
    private Spinner<Integer> widthSpinner;
    @FXML
    private Spinner<Integer> heightSpinner;
    @FXML
    private Spinner<Integer> numBunniesSpinner;
    @FXML
    private Spinner<Integer> numRobotsSpinner;
    @FXML
    private Canvas mazeCanvas;

    private Cell[][] cells;
    private int gridWidth;
    private int gridHeight;

    private Bunny[] bunnies;
    private Robot[] robots;
    private int numBunnies;
    private int numRobots;
    private boolean isRunning = false;

    public void initialize() {
        Renderer.initialize(mazeCanvas, cells);
    }

    @FXML
    public void generate() {
        this.gridWidth = widthSpinner.getValue();
        this.gridHeight = heightSpinner.getValue();
        this.bunnies = null;
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
        if (this.cells == null || this.bunnies != null || this.robots != null)
            return;

        this.numBunnies = numBunniesSpinner.getValue();
        this.bunnies = new Bunny[this.numBunnies];
        for (int i = 0; i < numBunnies; ++i) {
            Cell cell = getRandomEmptyCell();
            this.bunnies[i] = new Bunny(cell);
        }

        this.numRobots = numRobotsSpinner.getValue();
        this.robots = new Robot[this.numRobots];

        for (int i = 0; i < numRobots; ++i) {
            Cell cell = getRandomEmptyCell();
            this.robots[i] = new Robot(cell);
        }

        Renderer.drawOnCanvas();
    }

    private Cell getRandomEmptyCell() {
        int row = Problem.rand.nextInt(gridHeight);
        int col = Problem.rand.nextInt(gridWidth);
        Cell cell = cells[row][col];

        if (cell.isOccupied() == null) {
            return cell;
        } else {
            return getRandomEmptyCell();
        }
    }

    @FXML
    public void toggleRandom() {
        Entity.setMovingRandom(!Entity.isMovingRandom());
    }

    @FXML
    public void start() {
        if (isRunning || this.cells == null || this.bunnies == null || this.robots == null)
            return;

        isRunning = true;
        Renderer.setGameOver(false);
        Robot.initMemory(gridWidth, gridHeight, bunnies);

        DaemonManager.start();

        Thread[] bunnyThreads = new Thread[this.numBunnies];
        for (int i = 0; i < this.numBunnies; ++i) {
            bunnyThreads[i] = new Thread(bunnies[i]);
            bunnyThreads[i].start();
        }

        Thread[] robotThreads = new Thread[this.numRobots];
        for (int i = 0; i < this.numRobots; ++i) {
            robotThreads[i] = new Thread(robots[i]);
            robotThreads[i].start();
        }

        KeyboardListener.start(this.bunnies[0], this.robots);

        isRunning = false;
    }
}
