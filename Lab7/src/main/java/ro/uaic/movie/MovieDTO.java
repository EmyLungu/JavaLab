package ro.uaic.movie;

import java.sql.Date;

/**
 * MovieDTO
 */
public record MovieDTO(
    int id,
    String title,
    Date release_date,
    int duration,
    int score,
    int genre_id
){
    public MovieDTO(MovieEntity entity) {
        this(
            entity.getId(),
            entity.getTitle(),
            entity.getReleaseDate(),
            entity.getDuration(),
            entity.getScore(),
            entity.getGenreId()
        );
    }
}
