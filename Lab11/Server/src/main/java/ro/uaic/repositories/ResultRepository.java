package ro.uaic.repositories;

import ro.uaic.entities.Result;

import java.util.List;

import org.springframework.data.jpa.repository.QueryHints;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

/**
 * ResultRepository
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long>, JpaSpecificationExecutor<Result> {
    @Query("SELECT r FROM Result r WHERE r.player.name = :name")
    List<Result> findByPlayerName(@Param("name") String name);

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    List<Result> findAll(Specification<Result> spec);
}
