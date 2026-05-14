package ro.uaic.repositories;

import ro.uaic.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GameRepository
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
