package nl.tudelft.oopp.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LectureRoomTest {

    LectureRoom lectureRoom;

    @BeforeEach
    public void setup(){
        lectureRoom = new LectureRoom("Stefan", 2);
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
}
