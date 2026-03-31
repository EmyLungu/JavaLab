package ro.uaic.movie;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.*;

/**
 * MovieEntity
 */
@Entity
@Table(name = "movies")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String title;

    @Column(name = "release_date")
    private Date releaseDate;

    private Integer duration;
    private Integer score;

    @Column(name = "genre_id")
    private Integer genreId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "movie_cast",
        joinColumns = @JoinColumn(name = "id_movie"))
    @Column(name = "id_actor")
    private Set<Integer> actors;

    public Set<Integer> getActors() {
        return actors;
    }

    public void setActors(Set<Integer> actors) {
        this.actors = actors;
    }
}

