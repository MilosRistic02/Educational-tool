package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRoomRepository extends JpaRepository<LectureRoom, String> {
}
