package ro.uaic.movielist;

import java.sql.Timestamp;
import java.util.List;

import ro.uaic.movie.Movie;

/**
 * MovieList
 */
public class MovieList {
    private int id;
    private String name;
    private Timestamp createdAt;
    private List<Movie> movies;
    
    public MovieList() {}
    
    public MovieList(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
