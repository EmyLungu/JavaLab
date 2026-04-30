package ro.uaic.entities;

/**
 * SharedMemory
 */
public class SharedMemory {
    public boolean[][] visited;
    public boolean knowLocation;
    public Bunny[] bunnies;
    public int bunnyRow;
    public int bunnyCol;

    public SharedMemory(Bunny[] bunnies) {
        this.bunnies = bunnies;
        this.knowLocation = false;
        this.bunnyRow = -1;
        this.bunnyCol = -1;
    }

    public synchronized void updateBunnyLocation(int row, int col) {
        this.bunnyRow = row;
        this.bunnyCol = col;
        this.knowLocation = true;
    }
}
