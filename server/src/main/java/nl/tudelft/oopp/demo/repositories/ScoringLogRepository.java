package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoringLogRepository extends JpaRepository<ScoringLog, Long> {
    boolean existsByQuestionAndUsers(Question question, Users users);
    ScoringLog getByQuestionAndUsers(Question question, Users users);
}
