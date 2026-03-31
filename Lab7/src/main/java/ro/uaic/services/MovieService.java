package ro.uaic.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ro.uaic.MovieNotFoundException;
import ro.uaic.movie.MovieDTO;
import ro.uaic.movie.MovieRequest;
import ro.uaic.movie.MovieEntity;
import ro.uaic.movie.MovieRepository;

/**
 * MovieService
 */
@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<MovieDTO> findAllMovies() {
        return this.repository.findAll().stream()
                .map(e -> new MovieDTO(e))
                .collect(Collectors.toList());
    }

    public MovieDTO create(MovieRequest request) {
        MovieEntity entity = new MovieEntity();
        updateEntityFromReq(entity, request);
        return new MovieDTO(this.repository.save(entity));
    }

    public MovieDTO update(Integer id, MovieRequest request) {
        MovieEntity entity = this.repository.findById(id)
            .orElseThrow(() -> new MovieNotFoundException(id));

        updateEntityFromReq(entity, request);
        return new MovieDTO(this.repository.save(entity));
    }

    public MovieDTO updateScore(Integer id, int score) {
        MovieEntity entity = this.repository.findById(id)
            .orElseThrow(() -> new MovieNotFoundException(id));

        entity.setScore(score);
        return new MovieDTO(this.repository.save(entity));
    }


    public void delete(Integer id) {
        this.repository.deleteById(id);
    }

    private void updateEntityFromReq(MovieEntity entity, MovieRequest request) {
        entity.setTitle(request.title());
        entity.setReleaseDate(request.release_date());
        entity.setDuration(request.duration());
        entity.setScore(request.score());
        entity.setGenreId(request.genre_id());
    }
}
