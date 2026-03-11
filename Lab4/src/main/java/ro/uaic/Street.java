package ro.uaic;

import java.util.Objects;

/**
 * Street
 */
public class Street implements Comparable<Street> {
    private String name;
    private int length;

    private Intersection a;
    private Intersection b;

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public Intersection getA() {
        return a;
    }

    public Intersection getB() {
        return b;
    }

    public Street(String name, int length, Intersection a, Intersection b) {
        this.name = name;
        this.length = length;
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length, a, b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || this.getClass() != obj.getClass())
            return false;

        Street other = (Street) obj;

        return (
            getName().equals(other.getName()) &&
            getLength() == other.getLength()  &&
            getA().equals(other.getA()) &&
            getB().equals(other.getB())
        );
    }

    @Override
    public int compareTo(Street other) {
        return Integer.compare(this.length, other.length);
    }

    @Override
    public String toString() {
        return this.name + " (" + a.toString() + ", " + b.toString() + "; " + this.length + ")";
    }
}
