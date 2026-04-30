package ro.uaic.entities;

import java.util.Stack;

import ro.uaic.Cell;
import ro.uaic.Renderer;

/**
 * Robot
 */
public class Robot extends Entity {
    private static SharedMemory memory;
    private Stack<Cell> path;

    public Robot(Cell cell) {
        super(cell);
        this.path = new Stack<>();
        this.path.push(cell);
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
                visit(newRow, newCol);
            } else if (newCellOccupant instanceof Bunny) {
                newCellOccupant.kill();
                newCell.setOccupied(null);
                System.out.println("Bunny killed!");

                boolean allDead = true;
                for (Bunny b : Robot.memory.bunnies) {
                    if (!b.isDead()) {
                        allDead = false;
                        break;
                    }
                }
                if (allDead) {
                    Renderer.setGameOver(true);
                    System.out.println("Game over: all bunnies caught!");
                }
            }
        }
    }

    protected final int[] moveStrategy(int row, int col) {
        for (Bunny bunny : Robot.memory.bunnies) {
            int br = bunny.getCell().getRow();
            int bc = bunny.getCell().getCol();

            int sensorRange = Math.max(Renderer.getGridHeight(), Renderer.getGridWidth()) / 5;

            if (Math.abs(br - row) + Math.abs(bc - col) <= sensorRange) {
                Robot.memory.updateBunnyLocation(br, bc);
            }
        }

        int br = Robot.memory.bunnyRow;
        int bc = Robot.memory.bunnyCol;
        if (br != -1 && bc != -1) {
            int[] next = bfsNextStep(row, col, br, bc);
            if (next != null) {
                return next;
            }
        }

        for (int i = 0; i < 4; ++i) {
            int newRow = row + dy[i];
            int newCol = col + dx[i];

            if (isCellValid(newRow, newCol, dy[i], dx[i]) && !isVisited(newRow, newCol)) {
                if (path.isEmpty() || path.peek() != this.cell) {
                    path.push(this.cell);
                }
                return new int[] { newRow, newCol };
            }
        }

        if (this.path.size() > 1) {
            path.pop();
            Cell previous = path.peek();
            return new int[] { previous.getRow(), previous.getCol() };
        }

        return moveRandom(row, col);
    }

    public static void initMemory(int width, int height, Bunny[] bunnies) {
        Robot.memory = new SharedMemory(bunnies);
        Robot.memory.visited = new boolean[height][width];
    }

    private synchronized void visit(int row, int col) {
        Robot.memory.visited[row][col] = true;
        Renderer.getCells()[row][col].setHovered(true);
    }

    private synchronized boolean isVisited(int row, int col) {
        return Robot.memory.visited[row][col];
    }
}
