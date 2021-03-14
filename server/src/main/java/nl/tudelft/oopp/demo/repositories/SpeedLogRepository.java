package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeedLogRepository  extends JpaRepository<SpeedLog, Long> {

    boolean existsByUsersAndLectureRoom(Users users, LectureRoom lectureRoom);

    SpeedLog getByUsersAndLectureRoom(Users users, LectureRoom lectureRoom);
}
