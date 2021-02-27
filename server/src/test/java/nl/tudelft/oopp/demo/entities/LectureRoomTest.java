package nl.tudelft.oopp.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LectureRoomTest {

    LectureRoom lectureRoom;

    @BeforeEach
    public void setup(){
        lectureRoom = new LectureRoom(1, 2);
    }

    @Test
    void getLecturerID() {
        assertEquals(lectureRoom.getLecturerID(), 1);
    }

    @Test
    void setLecturerID() {
        lectureRoom.setLecturerID(2);
        assertEquals(lectureRoom.getLecturerID(), 2);
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

//    @Test
//    void getLecturePin() {
//        //TODO
//    }

//    @Test
//    void setLecturePin() {
//        //TODO
//    }

//    @Test
//    void getTimestamp() {
//
//    }

//    @Test
//    void testEquals() {
//        LectureRoom lectureRoom1 = new LectureRoom(2, 3);
        //TODO
//    }
}