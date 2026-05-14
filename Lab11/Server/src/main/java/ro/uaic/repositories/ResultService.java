package ro.uaic.repositories;

import ro.uaic.entities.Result;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * ResultService
 */
@Service
public class ResultService {
    ResultRepository resultRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ResultService(ResultRepository resultRepo) {
        this.resultRepository = resultRepo;
    }

    public List<Result> resultQuery(String prefixName, String minScore, String property) {
        Specification<Result> spec = (root, query, cb) -> cb.conjunction();

        if (prefixName != null && !prefixName.equals("_") && !prefixName.isEmpty()) {
            spec = spec.and(ResultSpecifications.hasPlayerNameStartingWith(prefixName));
        }

        if (minScore != null && !minScore.equals("_")) {
            try {
                Double minScoreValue = Double.parseDouble(minScore);
                spec = spec.and(ResultSpecifications.hasScoreGreaterThan(minScoreValue));
            } catch (NumberFormatException e) {
                System.err.println("Skipping score filter: Invalid number format.");
            }
        }

        if (property != null && !property.equals("_") && !property.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(property);
                spec = spec.and(ResultSpecifications.hasGameProperty(date));
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format received: " + property);
            }
        }

        return this.resultRepository.findAll(spec);
    }
}
