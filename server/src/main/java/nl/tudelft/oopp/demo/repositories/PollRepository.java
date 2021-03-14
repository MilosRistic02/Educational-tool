package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findAllByLecturePin(String lecturePin);

    Poll getById(long id);

    boolean existsById(long id);
}
