package ro.uaic.homework;

/**
 * Player
 */
public class Player {
    private double score;

    public Player() {
        this.score = 0.0;
    }

    public void addScore(double score) {
        this.score += score;
    }

    public double getScore() {
        return score;
    }
}
