package ro.uaic.resource;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

/**
 * Resource
 */
public abstract class Resource implements Serializable {
    String id;
    String title;
    String location;
    int year;
    String author;
    Set<String> keywords;

    Resource(String id, String title, String location, int year, String author) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.author = author;
        this.keywords = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
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
