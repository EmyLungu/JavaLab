package ro.uaic;

import ro.uaic.entities.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Renderer
 */
public class Renderer {
    private static Cell[][] cells;
    private static Canvas mazeCanvas;
    private static int gridWidth;
    private static int gridHeight;

    public static Cell lastHovered = null;
    public static Cell lastSelected = null;

    private static double CELL_SIZE = 30.0;
    private static double EMOJI_SIZE = 48.0;

    private static Image bunnyImg;
    private static Image robotImg;

    private static volatile boolean gameOver = false;

    public static void initialize(Canvas mazeCanvas_, Cell[][] cells_) {
        cells = cells_;
        mazeCanvas = mazeCanvas_;

        bunnyImg = new Image(App.class.getResource("bunny.png").toExternalForm());
        robotImg  = new Image(App.class.getResource("robot.png").toExternalForm());

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

        mazeCanvas.setOnMouseExited(event -> {
            if (lastHovered != null) {
                lastHovered.setHovered(false);
                lastHovered = null;
                drawOnCanvas();
            }
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

    public static void set(Cell[][] cells_, int gridWidth_, int gridHeight_) {
        cells = cells_;
        gridWidth = gridWidth_;
        gridHeight = gridHeight_;
    }

    public static void drawOnCanvas() {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        CELL_SIZE = 576 / Double.max(gridWidth, gridHeight);

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

                Entity occupant = cell.isOccupied();
                if (occupant != null) {
                    Image emoji = bunnyImg;
                    if (occupant instanceof Bunny) {
                        emoji = bunnyImg;
                    } else if (occupant instanceof Robot) {
                        emoji = robotImg;
                    }
                    gc.drawImage(
                        emoji,
                        x + CELL_SIZE / 2 - EMOJI_SIZE / 2,
                        y + CELL_SIZE / 2 - EMOJI_SIZE / 2,
                        EMOJI_SIZE, EMOJI_SIZE
                    );
                }

                drawWalls(cell, gc, x, y);
            }
        }

        if (lastSelected != null) {
            drawWalls(lastSelected, gc, lastSelected.getCol() * CELL_SIZE, lastSelected.getRow() * CELL_SIZE);
        }
    }

    private static void drawWalls(Cell cell, GraphicsContext gc, double x, double y) {
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

    private static void checkWallHover(double x, double y, Cell cell) {
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

    private static void checkWallClick(double x, double y, Cell cell) {
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

    private static Cell getCellFromCoords(double x, double y) {
        if (x >= gridWidth * CELL_SIZE || y >= gridHeight * CELL_SIZE)
            return null;

        int c = (int) (x / CELL_SIZE);
        int r = (int) (y / CELL_SIZE);
        return cells[r][c];
    }

    public static void toggleTopWall(int r, int c, boolean toggle) {
        if (r == 0) return;

        cells[r][c].setTop(toggle);
        if (r - 1 >= 0)
            cells[r - 1][c].setBottom(toggle);
    }

    public static void toggleRightWall(int r, int c, boolean toggle) {
        if (c == gridWidth - 1) return;
        cells[r][c].setRight(toggle);
        if (c + 1 < gridWidth)
            cells[r][c + 1].setLeft(toggle);
    }

    public static void toggleBottomWall(int r, int c, boolean toggle) {
        if (r == gridHeight - 1) return;

        cells[r][c].setBottom(toggle);
        if (r + 1 < gridHeight)
            cells[r + 1][c].setTop(toggle);
    }

    public static void toggleLeftWall(int r, int c, boolean toggle) {
        if (c == 0) return;

        cells[r][c].setLeft(toggle);
        if (c - 1 >= 0)
            cells[r][c - 1].setRight(toggle);
    }

    public static Cell[][] getCells() {
        return cells;
    }

    public static int getGridWidth() {
        return gridWidth;
    }

    public static int getGridHeight() {
        return gridHeight;
    }

    public static synchronized void setGameOver(boolean gameOver) {
        Renderer.gameOver = gameOver;
    }

    public static synchronized boolean isGameOver() {
        return gameOver;
    }
}
