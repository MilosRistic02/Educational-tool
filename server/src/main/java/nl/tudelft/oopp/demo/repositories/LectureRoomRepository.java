package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




public interface LectureRoomRepository extends JpaRepository<LectureRoom, String> {

    LectureRoom getLectureRoomByLecturePin(String lecturePin);

    boolean existsByLecturePin(String lecturePin);

    List<LectureRoom> getAllByIsOpenIsFalse();

    @Query(value = "SELECT * FROM lecture_room", nativeQuery = true)
    List<LectureRoom> getAll();

    List<LectureRoom> getAllByLecturerID(String lecturerID);

    LectureRoom getByLecturePin(String lectureRoomPin);
}
