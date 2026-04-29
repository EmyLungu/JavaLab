package ro.uaic.services;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.uaic.movie.MovieDTO;
import ro.uaic.movie.MovieEntity;
import ro.uaic.movie.MovieRepository;

/**
 * SolverService
 */
@Service
public class SolverService {
    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDTO> solve(int minCount) {
        List<MovieEntity> allMovies = movieRepository.findAll();
        int n = allMovies.size();

        Model model = new Model("Movie Solver");
        BoolVar[] selection = model.boolVarArray("selection", n);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (areRelated(allMovies.get(i), allMovies.get(j))) {
                    model.arithm(selection[i], "+", selection[j], "<=", 1).post();
                    // Daca unul din ei e deja in lista
                    // Daca ambii sunt in lista => 1 + 1 <= 1 : fals
                }
            }
        }

        IntVar totalSelected = model.intVar("totalSelected", minCount, n);
        model.sum(selection, "=", totalSelected).post();
        Solution solution = model.getSolver().findSolution();

        if (solution != null) {
            return IntStream.range(0, allMovies.size())
                .filter(i -> selection[i].getValue() == 1)
                .mapToObj(allMovies::get)
                .map(e -> new MovieDTO(e))
                .toList();
        }
        return Collections.emptyList();
    }

    private boolean areRelated(MovieEntity m1, MovieEntity m2) {
        Set<Integer> set1 = m1.getActors();
        Set<Integer> set2 = m2.getActors();
        if (set1 == null || set2 == null) return false;

        for (Integer id : set1) {
            if (set2.contains(id)) {
                return true;
            }
        }
        return false;
    }
}
