package ro.uaic.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ro.uaic.movie.MovieDTO;
import ro.uaic.movie.MovieRequest;
import ro.uaic.services.MovieService;

/**
 * MovieController
 */
@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> getAllMovies() {
        System.out.println("GET all movies");
        return this.movieService.findAllMovies();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDTO add(@RequestBody MovieRequest request) {
        System.out.println("POST new movie " + request);
        return this.movieService.create(request);
    }

    @PutMapping("/{id}")
    public MovieDTO modify(@PathVariable Integer id, @RequestBody MovieRequest request) {
        System.out.println("PUT " + id);
        return this.movieService.update(id, request);
    }

    @PatchMapping("/{id}/score")
    public MovieDTO modifyScore(@PathVariable Integer id, @RequestParam int score) {
        System.out.println("PATCH " + id + " score");
        return this.movieService.updateScore(id, score);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        System.out.println("DELETE " + id);
        this.movieService.delete(id);
    }
}
