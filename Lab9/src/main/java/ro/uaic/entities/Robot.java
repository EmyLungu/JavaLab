package ro.uaic.entities;

import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Robot
 */
public class Robot extends Entity {
    public Robot(Cell cell) {
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
        } else if (newCellOccupant instanceof Bunny) {
            Renderer.setGameOver(true);
            System.out.println("Game over");
        }
    }
}
