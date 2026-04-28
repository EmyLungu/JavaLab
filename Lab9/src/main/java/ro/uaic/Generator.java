package ro.uaic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generator 
 */
public class Generator {
    private static boolean[][] visited;
    private static int gridWidth;
    private static int gridHeight;
    public static boolean running = false;

    private static Cell lastHovered = null;

    public static void generate(Cell[][] cells, int gridWidth_, int gridHeight_) {
        running = true;
        gridWidth = gridWidth_;
        gridHeight = gridHeight_;
        visited = new boolean[gridHeight][gridWidth];
        
        Random rand = new Random();
        int startRow = rand.nextInt(gridHeight);
        int startCol = rand.nextInt(gridWidth);
        Cell startCell = cells[startRow][startCol];

        DFS(cells, startCell);

        System.out.println("Generation done!");
        running = false;
    }
    
    private static void DFS(Cell[][] cells, Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        if (row < 0 || row >= gridHeight || col < 0 || col >= gridWidth || visited[row][col])
            return;

        visited[row][col] = true;

        if (lastHovered != null)
            lastHovered.setHovered(false);

        int[] drow = {-1, 0, 1, 0};
        int[] dcol = {0, 1, 0, -1};

        List<Cell> neighbours = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            int nrow = row + drow[i];
            int ncol = col + dcol[i];
            if (isCellValid(nrow, ncol)) {
                neighbours.add(cells[nrow][ncol]);
            }
        }
        Collections.shuffle(neighbours);


        for (Cell neighbour : neighbours) {
            int nrow = neighbour.getRow();
            int ncol = neighbour.getCol();
            if (isCellValid(nrow, ncol)) {
                int diffrow = nrow - row;
                int diffcol = ncol - col;

                for (int i = 0; i < 4; ++i) {
                    if (diffrow == drow[i] && diffcol == dcol[i] && i == 0) Renderer.toggleTopWall(row, col, false);
                    if (diffrow == drow[i] && diffcol == dcol[i] && i == 1) Renderer.toggleRightWall(row, col, false);
                    if (diffrow == drow[i] && diffcol == dcol[i] && i == 2) Renderer.toggleBottomWall(row, col, false);
                    if (diffrow == drow[i] && diffcol == dcol[i] && i == 3) Renderer.toggleLeftWall(row, col, false);
                }

                neighbour.setHovered(true);
                lastHovered = neighbour;

                DFS(cells, neighbour);
            }
        }
    }

    private static boolean isCellValid(int row, int col) {
        if (row < 0 || row >= gridHeight || col < 0 || col >= gridWidth || visited[row][col])
            return false;
        return true;
    }

    public static boolean isRunning() {
        return running;
    }
}
