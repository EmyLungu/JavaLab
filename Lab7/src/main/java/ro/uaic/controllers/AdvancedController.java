package ro.uaic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.uaic.movie.MovieDTO;
import ro.uaic.services.SolverService;

/**
 * AdvancedController
 */
@RestController
@RequestMapping("/movies")
public class AdvancedController {
    @Autowired
    private SolverService solverService;

    @GetMapping("/solve")
    public List<MovieDTO> getIndependentMovies(@RequestParam int minCount) {
        System.out.println("GET independent-movies");
        return this.solverService.solve(minCount);
    }
}
