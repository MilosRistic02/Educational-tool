package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class LectureRoomTest {

    LectureRoom lectureRoom;

    /**
     * Test for the different LectureRoom class.
     */
    @BeforeEach
    public void setup() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        lectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
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
    void getLectureName() {
        assertEquals(lectureRoom.getLectureName(), "Reasoning and Logic");
    }

    @Test
    void setLectureName() {
        lectureRoom.setLectureName("OOPP");
        assertEquals(lectureRoom.getLectureName(), "OOPP");
    }

    @Test
    void getCourseId() {
        assertEquals(lectureRoom.getCourseId(), "CSE4200");
    }

    @Test
    void setCourseId() {
        lectureRoom.setCourseId("CSE1105");
        assertEquals(lectureRoom.getCourseId(), "CSE1105");
    }

    @Test
    void setGetLecturePin() {
        lectureRoom.setLecturePin("myLecture");
        assertEquals("myLecture", lectureRoom.getLecturePin());
    }

    @Test
    void getLectureOpen() {
        assertEquals(true, lectureRoom.isOpen());
    }

    @Test
    void setLectureOpen() {
        lectureRoom.setOpen(false);
        assertEquals(false, lectureRoom.isOpen());
    }

    @Test
    void getStartingTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateString = "2021-04-01 12:34";
        Date date = format.parse(dateString);

        assertEquals(date, lectureRoom.getStartingTime());
    }

    @Test
    void setStartingTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String dateString = "2022-06-09 04:20";
        Date date = format.parse(dateString);
        lectureRoom.setStartingTime(date);

        assertEquals(date, lectureRoom.getStartingTime());
    }

    @Test
    void testEquals() {
        LectureRoom room = new LectureRoom("Stefan", "Reasoning and Logic","CSE4200", new Date());
        room.setLecturePin("2802202001Stefan");
        assertEquals(room, lectureRoom);
    }

    @Test
    void testSameEquals() {
        assertEquals(lectureRoom, lectureRoom);
    }

    @Test
    void testNotEquals() {
        LectureRoom room = new LectureRoom("Andy", "Reasoning and Logic", "CSE4200", new Date());
        room.setLecturePin("2020Andy");
        assertNotEquals(room, lectureRoom);
    }
}
