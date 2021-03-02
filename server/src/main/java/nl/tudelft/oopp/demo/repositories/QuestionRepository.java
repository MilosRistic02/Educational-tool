package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByQuestionAndAuthorAndLecturePin(
            String question, String author, String lecturePin);

    Question getByQuestionAndAuthorAndLecturePin(
            String question, String author, String lecturePin);
}
