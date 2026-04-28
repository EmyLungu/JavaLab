package ro.uaic.entities;

import java.util.Stack;

import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Bunny
 */
public class Bunny extends Entity {
    private boolean[][] memory;
    private Stack<Cell> path;
    public Bunny(Cell cell) {
        super(cell);
        path = new Stack<>();
        path.push(cell);
    }

    protected final void onMove(int newRow, int newCol) {
        Cell[][] cells = Renderer.getCells();
        Cell newCell = cells[newRow][newCol];
        Entity newCellOccupant = newCell.isOccupied();

        boolean isBacktracking = (this.path.size() > 1 && 
                newCell == this.path.get(path.size() - 2));

        synchronized (cells) {
            if (newCellOccupant == null) {
                cell.setOccupied(null);
                this.cell = newCell;
                this.cell.setOccupied(this);

                visit(newRow, newCol);

                if (!isBacktracking)
                    this.path.push(newCell);
            }

            Cell end = cells[Renderer.getGridHeight() - 1][Renderer.getGridWidth() - 1];
            if (this.cell == end) {
                Renderer.setGameOver(true);
                System.out.println("Game over");
            }
        }
    }

    protected final int[] moveStrategy(int row, int col) {
        for (int i = 0; i < 4; ++i) {
            int newRow = row + dy[i];
            int newCol = col + dx[i];

            if (isCellValid(newRow, newCol, dy[i], dx[i])
                    && !isVisited(newRow, newCol)
               ) {
                return new int[] {newRow, newCol};
            }
        }

        if (this.path.size() > 1) {
            path.pop();
            Cell previous = path.peek();
            return new int[] {previous.getRow(), previous.getCol()};
        }

        return moveRandom(row, col);
    }

    public void initMemory(int width, int height) {
        this.memory = new boolean[height][width];
    }

    private void visit(int row, int col) {
        this.memory[row][col] = true;
        Renderer.getCells()[row][col].setHovered(true);
    }

    private boolean isVisited(int row, int col) {
        return this.memory[row][col];
    }
}
