package ro.uaic;

/**
 * Validator
 */
public class Validator {
    private static boolean[][] visited;
    private static Cell endCell;
    private static int gridWidth;
    private static int gridHeight;
    public static boolean validate(Cell[][] cells, int gridWidth_, int gridHeight_, Cell startCell, Cell endCell_) {
        endCell = endCell_;
        gridWidth = gridWidth_;
        gridHeight = gridHeight_;
        visited = new boolean[gridHeight][gridWidth];

        return DFS(cells, startCell.getRow(), startCell.getCol());
    }
    
    private static boolean DFS(Cell[][] cells, int row, int col) {
        if (row < 0 || row >= gridHeight || col < 0 || col >= gridWidth || visited[row][col])
            return false;
        if (row == endCell.getRow() && col == endCell.getCol())
            return true;

        visited[row][col] = true;
        Cell current = cells[row][col];

        if (!current.getTop()    && DFS(cells, row - 1, col)) return true;
        if (!current.getRight()  && DFS(cells, row, col + 1)) return true;
        if (!current.getBottom() && DFS(cells, row + 1, col)) return true;
        if (!current.getLeft()   && DFS(cells, row, col - 1)) return true;
        return false;
    }
}
