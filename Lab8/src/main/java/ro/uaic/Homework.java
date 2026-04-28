package ro.uaic;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;
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
 * Homework Controller
 */
public class Homework {
    @FXML private Spinner<Integer> widthSpinner; 
    @FXML private Spinner<Integer> heightSpinner; 
    @FXML private Canvas mazeCanvas;

    private int gridWidth = 10;
    private int gridHeight = 10;
    private Cell[][] cells;
    private double CELL_SIZE = 30.0;

    private Cell lastHovered = null;
    private Cell lastSelected = null;

    private Cell startCell = null;
    private Cell endCell = null;

    public void initialize() {
        mazeCanvas.setOnMouseMoved(event -> {
            if (cells == null)
                return;
            double x = event.getX();
            double y = event.getY();
            Cell cell = getCellFromCoords(x, y);
            if (cell == null)
                return;

            if (lastSelected != null)
                if (lastSelected == cell) {
                    checkWallHover(x, y, cell);
                } else {
                    boolean[] wallHovered = lastSelected.getWallsHovered();
                    for (int i = 0; i < 4; ++i) {
                        wallHovered[i] = false;
                }
            }

            if (lastHovered != null && lastHovered != cell) {
                lastHovered.setHovered(false);
            }

            cell.setHovered(true);
            lastHovered = cell;
            drawOnCanvas();
        });

        mazeCanvas.setOnMouseClicked(event -> {
            if (cells == null)
                return;
            double x = event.getX();
            double y = event.getY();
            Cell cell = getCellFromCoords(x, y);
            if (cell == null)
                return;

            if (lastSelected != null && lastSelected == cell) {
                checkWallClick(x, y, cell);
            }

            if (lastSelected != null && lastSelected != cell) {
                lastSelected.setSelected(false);
            }

            cell.setSelected(!cell.isSelected());
            lastSelected = cell;
            drawOnCanvas();
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
        drawOnCanvas();
    }

    private void drawOnCanvas() {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        CELL_SIZE = 640 / Double.max(gridWidth, gridHeight);

        for (int r = 0; r < gridHeight; r++) {
            for (int c = 0; c < gridWidth; c++) {
                double x = c * CELL_SIZE;
                double y = r * CELL_SIZE;
                Cell cell = cells[r][c];

                if (cell.isHovered()) {
                    gc.setFill(Color.web("#F18701"));
                } else {
                    gc.setFill(Color.web("#F7B801"));
                }

                if (cell.isSelected()) {
                    gc.setFill(Color.web("#F35B04"));
                    gc.setLineWidth(10.0);
                } else {
                    gc.setLineWidth(5.0);
                }

                gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                drawWalls(cell, gc, x, y);
            }
        }

        if (lastSelected != null) {
            drawWalls(lastSelected, gc, lastSelected.getCol() * CELL_SIZE, lastSelected.getRow() * CELL_SIZE);
        }
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

    private void drawWalls(Cell cell, GraphicsContext gc, double x, double y) {
        boolean[] walls = cell.getWalls();
        boolean[] wallHovered = cell.getWallsHovered();
        int hovWallIdx = -1;

        gc.setStroke(Color.web("#f35b04"));
        for (int i = 0; i < 4; ++i) {
            if (walls[i])
                cell.drawWall(i, gc, x, y, CELL_SIZE);

            if (wallHovered[i]) {
                hovWallIdx = i;
            }
        }

        if (hovWallIdx != -1 && walls[hovWallIdx]) {
            gc.setStroke(Color.web("#3d348b"));
            cell.drawWall(hovWallIdx, gc, x, y, CELL_SIZE);
        }
    }

    private Cell getCellFromCoords(double x, double y) {
        if (x >= gridWidth * CELL_SIZE || y >= gridHeight * CELL_SIZE)
            return null;

        int c = (int) (x / CELL_SIZE);
        int r = (int) (y / CELL_SIZE);
        return cells[r][c];
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

    private void checkWallHover(double x, double y, Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        double topLeftX = col * CELL_SIZE;
        double topLeftY = row * CELL_SIZE;
        boolean[] wallHovered = cell.getWallsHovered();

        int wallWidth = 5;

        for (int i = 0; i < 4; ++i) {
            wallHovered[i] = false;
        }

        if (x > topLeftX && x < topLeftX + CELL_SIZE && y > topLeftY && y < topLeftY + wallWidth) {
            wallHovered[0] = true;
        } else if (x > topLeftX + CELL_SIZE - wallWidth && x < topLeftX + CELL_SIZE && y > topLeftY && y < topLeftY + CELL_SIZE) {
            wallHovered[1] = true;
        } else if (x > topLeftX && x < topLeftX + CELL_SIZE && y > topLeftY + CELL_SIZE - wallWidth  && y < topLeftY + CELL_SIZE) {
            wallHovered[2] = true;
        } else if (x > topLeftX && x < topLeftX + wallWidth && y > topLeftY && y < topLeftY + CELL_SIZE) {
            wallHovered[3] = true;
        }
    }

    private void checkWallClick(double x, double y, Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        double topLeftX = col * CELL_SIZE;
        double topLeftY = row * CELL_SIZE;

        int wallWidth = 5;
        boolean[] walls = cell.getWalls();

        if (x > topLeftX && x < topLeftX + CELL_SIZE && y > topLeftY && y < topLeftY + wallWidth) {
            toggleTopWall(row, col, !walls[0]);
            walls[0] = !walls[0];
        } else if (x > topLeftX + CELL_SIZE - wallWidth && x < topLeftX + CELL_SIZE && y > topLeftY && y < topLeftY + CELL_SIZE) {
            toggleRightWall(row, col, !walls[1]);
            walls[1] = !walls[1];
        } else if (x > topLeftX && x < topLeftX + CELL_SIZE && y > topLeftY + CELL_SIZE - wallWidth  && y < topLeftY + CELL_SIZE) {
            toggleBottomWall(row, col, !walls[2]);
            walls[2] = !walls[2];
        } else if (x > topLeftX && x < topLeftX + wallWidth && y > topLeftY && y < topLeftY + CELL_SIZE) {
            toggleLeftWall(row, col, !walls[3]);
            walls[3] = !walls[3];
        }
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

            drawOnCanvas();
            System.out.println("Maze restored successfully!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
