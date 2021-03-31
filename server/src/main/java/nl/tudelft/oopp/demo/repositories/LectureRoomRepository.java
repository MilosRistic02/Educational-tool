package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface LectureRoomRepository extends JpaRepository<LectureRoom, String> {
    boolean existsByLecturePin(String lecturePin);

    List<LectureRoom> getAllByIsOpenIsFalseOrderByCreationDateDesc();

    List<LectureRoom> getAllByLecturerID(String lecturerID);

    LectureRoom getByLecturePin(String lectureRoomPin);
}
