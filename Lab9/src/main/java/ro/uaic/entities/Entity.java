package ro.uaic.entities;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javafx.application.Platform;
import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Entity
 */
public abstract class Entity implements Runnable {
    protected static volatile boolean paused = false;
    public static Object pauseLock = new Object();

    protected Cell cell;
    private boolean isDead = false;

    protected int delay = 500;

    static int dx[] = { 0, 1, 0, -1 };
    static int dy[] = { -1, 0, 1, 0 };
    static boolean movingRandom = true;

    Random rand;

    public Entity(Cell cell) {
        this.cell = cell;
        this.cell.setOccupied(this);
        rand = new Random();
    }

    @Override
    public void run() {
        while (!Renderer.isGameOver() && !isDead) {
            checkPause();

            int row = this.cell.getRow();
            int col = this.cell.getCol();

            int[] newCoords;
            if (isMovingRandom()) {
                newCoords = moveRandom(row, col);
            } else {
                newCoords = moveStrategy(row, col);
            }
            int newRow = newCoords[0];
            int newCol = newCoords[1];

            if (!isCellValid(newRow, newCol, newRow - row, newCol - col))
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

    protected boolean isCellValid(int row, int col, int ry, int rx) {
        if (row < 0 || row >= Renderer.getGridHeight() || col < 0 || col >= Renderer.getGridWidth())
            return false;

        if (ry == -1 && rx == 0 && cell.getTop())
            return false;
        if (ry == 0 && rx == 1 && cell.getRight())
            return false;
        if (ry == 1 && rx == 0 && cell.getBottom())
            return false;
        if (ry == 0 && rx == -1 && cell.getLeft())
            return false;

        return true;
    }

    protected int[] moveRandom(int row, int col) {
        int r = this.rand.nextInt(4);
        return new int[] { row + dy[r], col + dx[r] };
    }

    protected abstract int[] moveStrategy(int row, int col);

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public static boolean isMovingRandom() {
        return Entity.movingRandom;
    }

    public static void setMovingRandom(boolean movingRandom) {
        Entity.movingRandom = movingRandom;
    }

    protected void checkPause() {
        synchronized (pauseLock) {
            while (paused) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static synchronized void setPaused(boolean paused) {
        Entity.paused = paused;
    }

    /*
     * Returns the next step towards a target
     */
    protected int[] bfsNextStep(int startRow, int startCol, int targetRow, int targetCol) {
        int h = Renderer.getGridHeight(), w = Renderer.getGridWidth();

        int[][][] prev = new int[h][w][2];
        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j) {
                prev[i][j][0] = -1;
                prev[i][j][1] = -1;
            }
        boolean[][] seen = new boolean[h][w];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startRow, startCol });
        seen[startRow][startCol] = true;
        prev[startRow][startCol] = new int[] { startRow, startCol };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            if (r == targetRow && c == targetCol) {
                int[] step = new int[] { r, c };
                while (prev[step[0]][step[1]][0] != startRow || prev[step[0]][step[1]][1] != startCol) {
                    step = prev[step[0]][step[1]];
                }
                return step;
            }

            Cell fromCell = Renderer.getCells()[r][c];
            for (int i = 0; i < 4; ++i) {
                int nr = r + dy[i], nc = c + dx[i];
                if (nr < 0 || nr >= h || nc < 0 || nc >= w)
                    continue;
                if (seen[nr][nc])
                    continue;
                if (i == 0 && fromCell.getTop())
                    continue;
                if (i == 1 && fromCell.getRight())
                    continue;
                if (i == 2 && fromCell.getBottom())
                    continue;
                if (i == 3 && fromCell.getLeft())
                    continue;
                seen[nr][nc] = true;
                prev[nr][nc] = new int[] { r, c };
                queue.add(new int[] { nr, nc });
            }
        }
        return null;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean isDead() {
        return isDead;
    }

    public void kill() {
        this.isDead = true;
    }
}
