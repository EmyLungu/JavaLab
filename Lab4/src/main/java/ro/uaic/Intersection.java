package ro.uaic;

/**
 * Intersection
 */
public class Intersection implements Comparable<Intersection> {
    private String name;
    private double x, y;

    public Intersection(String name) {
        this.name = name;
        this.x = Math.random() * 1000;
        this.y = Math.random() * 1000;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int hashCode() {
         return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Intersection other = (Intersection) obj;

        return this.name.equals(other.name);
    }

    @Override
    public int compareTo(Intersection other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
