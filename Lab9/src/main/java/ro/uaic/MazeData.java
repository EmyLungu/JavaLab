package ro.uaic;

import java.io.Serializable;

/**
 * MazeData
 */
public class MazeData implements Serializable {
    private int gridWidth = 10;
    private int gridHeight = 10;
    private Cell[][] cells;

    private Cell startCell = null;
    private Cell endCell = null;

    public MazeData(Cell[][] cells, int gridWidth, int gridHeight, Cell startCell, Cell endCell) {
        this.cells = cells;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.startCell = startCell;
        this.endCell = endCell;

        for (int r = 0; r < gridHeight; ++r) {
            for (int c = 0; c < gridWidth; ++c) {
                Cell cell = cells[r][c];

                cell.setSelected(false);
                cell.setHovered(false);
            }
        }
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public Cell getEndCell() {
        return endCell;
    }
}
