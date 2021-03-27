package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByLecturePin(String lecturePin);

    boolean existsByIdAndAndLecturePin(long id, String lecturePin);

    Question getByIdAndLecturePin(long id, String lecturePin);

    Question getByLecturePin(String lecturePin);

    List<Question> getAllByLecturePinOrderByScoreDescCreationDateDesc(String lecturePin);

    List<Question> getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(
            int answered, String lecturePin);

    Question findTopByLecturePinAndAuthorOrderByCreationDateDesc(String lecturePin, String author);
}
