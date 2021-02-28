package nl.tudelft.oopp.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class LectureRoomTest {

    LectureRoom lectureRoom;

    @BeforeEach
    public void setup(){
        lectureRoom = new LectureRoom("Stefan", 2);
        lectureRoom.setLecturePin("2802202001Stefan");
    }

    @Test
    public void constructorTest() {
        assertNotNull(lectureRoom);
    }

    @Test
    void getLecturerID() {
        assertEquals(lectureRoom.getLecturerID(), "Stefan");
    }

    @Test
    void setLecturerID() {
        lectureRoom.setLecturerID("Koen");
        assertEquals(lectureRoom.getLecturerID(), "Koen");
    }

    @Test
    void getCourseId() {
        assertEquals(lectureRoom.getCourseId(), 2);
    }

    @Test
    void setCourseId() {
        lectureRoom.setCourseId(5);
        assertEquals(lectureRoom.getCourseId(), 5);
    }

    @Test
    void setGetLecturePin() {
        lectureRoom.setLecturePin("myLecture");
        assertEquals("myLecture", lectureRoom.getLecturePin());
    }

    @Test
    void testEquals() {
        LectureRoom room = new LectureRoom("Stefan", 2);
        room.setLecturePin("2802202001Stefan");
        assertEquals(room, lectureRoom);
    }

    @Test
    void testSameEquals() {
        assertEquals(lectureRoom, lectureRoom);
    }

    @Test
    void testNotEquals() {
        LectureRoom room = new LectureRoom("Andy", 2);
        room.setLecturePin("2020Andy");
        assertNotEquals(room, lectureRoom);
    }
}
