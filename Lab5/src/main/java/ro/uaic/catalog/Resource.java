package ro.uaic.catalog;

/**
 * Resource
 */
public abstract class Resource {
    String id;
    String title;
    String location;
    int year;
    String author;

    Resource(String id, String title, String location, int year, String author) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Resource [" +
            this.id + " | " +
            this.title + " | " +
            this.location + " | " +
            this.year + " | " +
            this.author +
            "]";
    }

    public abstract void view() throws ResourceException;
}
