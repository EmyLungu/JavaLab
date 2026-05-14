package ro.uaic.repositories;

import ro.uaic.entities.Result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

/**
 * ResultSpecifications
 */
public class ResultSpecifications {
    public static Specification<Result> hasPlayerNameStartingWith(String prefix) {
        return (root, query, cb) -> cb.like(root.join("player").get("name"), prefix + "%");
    }

    public static Specification<Result> hasScoreGreaterThan(Double score) {
        return (root, query, cb) -> cb.gt(root.get("score"), score);
    }

    public static Specification<Result> hasGameProperty(LocalDate date) {
        return (root, query, cb) -> {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            return cb.between(root.join("game").get("startTime"), startOfDay, endOfDay);
        };
    }
}
