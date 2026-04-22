package ro.uaic;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * Compulsory Controller
 */
public class Compulsory {
    @FXML private Spinner<Integer> widthSpinner; 
    @FXML private Spinner<Integer> heightSpinner; 
    @FXML private Canvas mazeCanvas;

    private int gridWidth = 10;
    private int gridHeight = 10;
    private Cell[][] cells;
    private double CELL_SIZE = 30.0;

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
        drawOnCanvas();
    }

    private void drawOnCanvas() {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());
        gc.setStroke(Color.web("#f35b04"));
        gc.setLineWidth(5.0);
        
        CELL_SIZE = 640 / Double.max(gridWidth, gridHeight);

        gc.setFill(Color.web("#F7B801"));
        for (int r = 0; r < gridHeight; r++) {
            for (int c = 0; c < gridWidth; c++) {
                double x = c * CELL_SIZE;
                double y = r * CELL_SIZE;
                Cell cell = cells[r][c];

                gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                if (cell.getTop()) gc.strokeLine(x, y, x + CELL_SIZE, y);
                if (cell.getBottom()) gc.strokeLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
                if (cell.getLeft()) gc.strokeLine(x, y, x, y + CELL_SIZE);
                if (cell.getRight()) gc.strokeLine(x + CELL_SIZE, y, x + CELL_SIZE, y + CELL_SIZE);
            }
        }
    }

    @FXML
    private void createMaze() {
        if (cells == null)
            return;

        Random rand = new Random();
        for (int r = 0; r < gridHeight; r++) {
            for (int c = 0; c < gridWidth; c++) {
                if (rand.nextBoolean()) toggleTopWall(r, c, false); 
                if (rand.nextBoolean()) toggleRightWall(r, c, false); 
                if (rand.nextBoolean()) toggleBottomWall(r, c, false); 
                if (rand.nextBoolean()) toggleLeftWall(r, c, false); 
            }
        }
        drawOnCanvas();
    }

    @FXML 
    private void resetMaze() { 
        render(); 
    }
    
    @FXML 
    private void exitApp() { 
        System.exit(0); 
    }

    private void toggleTopWall(int r, int c, boolean toggle) {
        if (r == 0) return;

        cells[r][c].setTop(toggle);
        if (r - 1 >= 0)
            cells[r - 1][c].setBottom(toggle);
    }

    private void toggleRightWall(int r, int c, boolean toggle) {
        if (c == gridWidth - 1) return;
        cells[r][c].setRight(toggle);
        if (c + 1 < gridWidth)
            cells[r][c + 1].setLeft(toggle);
    }

    private void toggleBottomWall(int r, int c, boolean toggle) {
        if (r == gridHeight - 1) return;

        cells[r][c].setBottom(toggle);
        if (r + 1 < gridHeight)
            cells[r + 1][c].setTop(toggle);
    }

    private void toggleLeftWall(int r, int c, boolean toggle) {
        if (c == 0) return;

        cells[r][c].setLeft(toggle);
        if (c - 1 >= 0)
            cells[r][c - 1].setRight(toggle);
    }
}
