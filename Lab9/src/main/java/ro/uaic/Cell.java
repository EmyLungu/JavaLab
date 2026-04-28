package ro.uaic;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

import ro.uaic.entities.Entity;

public class Cell implements Serializable {
    private int row, col;
    private boolean top, right, bottom, left;
    private boolean[] wallHovered = {false, false, false, false};
    private boolean hovered;
    private boolean selected;

    private Entity occupied;

    public Cell(int row, int col) {
        setRow(row);
        setCol(col);

        setTop(true);
        setRight(true);
        setBottom(true);
        setLeft(true);
        
        setOccupied(null);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean getTop() {
        return this.top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean getRight() {
        return this.right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean getBottom() {
        return this.bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean getLeft() {
        return this.left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isHovered() {
        return this.hovered;
    }
    
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean[] getWalls() {
        boolean[] walls = {getTop(), getRight(), getBottom(), getLeft()};
        return walls;
    }

    public boolean[] getWallsHovered() {
        return wallHovered;
    }

    public Entity isOccupied() {
        return occupied;
    }

    public synchronized void setOccupied(Entity occupied) {
        this.occupied = occupied;
    }

    public void drawWall(int idx, GraphicsContext gc, double x, double y, double CELL_SIZE) {
        if (idx == 0) gc.strokeLine(x, y, x + CELL_SIZE, y);
        if (idx == 1) gc.strokeLine(x + CELL_SIZE, y, x + CELL_SIZE, y + CELL_SIZE);
        if (idx == 2) gc.strokeLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
        if (idx == 3) gc.strokeLine(x, y, x, y + CELL_SIZE);
    }
}
