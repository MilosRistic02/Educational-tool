package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByLecturePin(String lecturePin);

    Question getByLecturePin(String lecturePin);

}
