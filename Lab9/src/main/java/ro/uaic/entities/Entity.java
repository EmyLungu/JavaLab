package ro.uaic.entities;

import java.util.Random;

import javafx.application.Platform;
import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Entity
 */
public abstract class Entity implements Runnable {
    protected Cell cell;
    protected int delay = 500;

    static int dx[] = { 0, 1, 0, -1};
    static int dy[] = {-1, 0, 1,  0};

    public Entity(Cell cell) {
        this.cell = cell;
        this.cell.setOccupied(this);
    }

    @Override
    public void run() {
        Random rand = new Random();

        while (!Renderer.isGameOver()) {
            int row = this.cell.getRow();
            int col = this.cell.getCol();

            int r = rand.nextInt(4);
            int rx = dx[r];
            int ry = dy[r];

            int newRow = row + ry;
            int newCol = col + rx;

            if (!isCellValid(row, col, rx, ry))
                continue;

            onMove(newRow, newCol);

            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                System.err.println("Thread sleep error: " + e);
                Thread.currentThread().interrupt();
                break;
            }

            renderThread();
        }
    }

    protected abstract void onMove(int newRow, int newCol);

    protected synchronized void renderThread() {    
        Platform.runLater(() -> {
            Renderer.drawOnCanvas();
        });
    }

    private boolean isCellValid(int row, int col, int rx, int ry) {
        if (row < 0 || row >= Renderer.getGridHeight() || col < 0 || col >= Renderer.getGridWidth())
            return false;

        if (ry == -1 && rx ==  0 && cell.getTop()) return false;
        if (ry ==  0 && rx ==  1 && cell.getRight()) return false;
        if (ry ==  1 && rx ==  0 && cell.getBottom()) return false;
        if (ry ==  0 && rx == -1 && cell.getLeft()) return false;

        return true;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
