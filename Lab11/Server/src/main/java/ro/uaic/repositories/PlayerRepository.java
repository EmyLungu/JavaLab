package ro.uaic.repositories;

import ro.uaic.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PlayerRepository
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
