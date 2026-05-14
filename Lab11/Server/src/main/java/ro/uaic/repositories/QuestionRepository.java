package ro.uaic.repositories;

import ro.uaic.entities.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * QuestionRepository
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions ORDER BY RANDOM() LIMIT :size", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("size") int size);
}
