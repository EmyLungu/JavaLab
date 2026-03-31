package ro.uaic.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MovieRepository
 */
@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
}

