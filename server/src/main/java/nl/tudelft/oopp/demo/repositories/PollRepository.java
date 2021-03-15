package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findAllByLecturePin(String lecturePin);

    Poll getById(long id);

    boolean existsById(long id);
}
