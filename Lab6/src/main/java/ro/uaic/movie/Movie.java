package ro.uaic.movie;

import java.sql.Date;

/**
 * Movie
 */
public class Movie {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private int score;
    private int genreId;
    private String genreName;

    public Movie(int id, String title, Date releaseDate, int duration, int score, int genreId) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genreId = genreId;
    }

    public Movie(String title, Date releaseDate, int duration, int score, int genreId) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genreId = genreId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public int getScore() {
        return score;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
