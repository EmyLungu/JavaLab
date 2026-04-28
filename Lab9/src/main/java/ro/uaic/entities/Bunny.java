package ro.uaic.entities;

import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Bunny
 */
public class Bunny extends Entity {
    public Bunny(Cell cell) {
        super(cell);
    }

    protected final void onMove(int newRow, int newCol) {
        Cell[][] cells = Renderer.getCells();
        Cell newCell = cells[newRow][newCol];
        Entity newCellOccupant = newCell.isOccupied();

        if (newCellOccupant == null) {
            synchronized (cells) {
                cell.setOccupied(null);
                this.cell = newCell;
                this.cell.setOccupied(this);
            }
        }

        Cell end = cells[Renderer.getGridHeight() - 1][Renderer.getGridWidth() - 1];
        if (this.cell == end) {
            Renderer.setGameOver(true);
            System.out.println("Game over");
        }
    }
}
