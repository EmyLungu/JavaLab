package ro.uaic.entities;

import java.util.Stack;

import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Bunny
 */
public class Bunny extends Entity {
    private Stack<Cell> path;

    public Bunny(Cell cell) {
        super(cell);
        path = new Stack<>();
        path.push(cell);
        this.cell = cell;
    }

    protected final void onMove(int newRow, int newCol) {
        Cell[][] cells = Renderer.getCells();
        Cell newCell = cells[newRow][newCol];
        Entity newCellOccupant = newCell.isOccupied();

        synchronized (cells) {
            if (newCellOccupant == null) {
                cell.setOccupied(null);
                this.cell = newCell;
                this.cell.setOccupied(this);

                Renderer.getCells()[newRow][newCol].setHovered(true);
            }

            Cell end = cells[Renderer.getGridHeight() - 1][Renderer.getGridWidth() - 1];
            if (this.cell == end) {
                Renderer.setGameOver(true);
                System.out.println("Game over");
            }
        }
    }

    protected final int[] moveStrategy(int row, int col) {
        int[] next = bfsNextStep(row, col, Renderer.getGridHeight() - 1, Renderer.getGridWidth() - 1);
        if (next != null) {
            return next;
        }

        return moveRandom(row, col);
    }
}
