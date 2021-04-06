package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SpeedLogTest {

    private SpeedLog speedLog;
    private Users testUser;
    private LectureRoom lectureRoom;

    @BeforeEach
    void setup() throws ParseException {
        testUser = new Users("Geraldine", "G@Gmail.com", "1234", "student");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        lectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
        lectureRoom.setLecturePin("1234521Piet");

        speedLog = new SpeedLog(testUser, lectureRoom, 50);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(new SpeedLog());
    }

    @Test
    void testConstructor() {
        assertNotNull(speedLog);
    }

    @Test
    void setAndGetIdTest() {
        speedLog.setId(70L);
        assertEquals(70L, speedLog.getId());
    }

    @Test
    void getUsers() {
        assertEquals(testUser, speedLog.getUsers());
    }

    @Test
    void setUsers() {
        Users otherUsers = new Users("Hagrid", "H@Kaas.nl", "CheeseLover69", "student");
        speedLog.setUsers(otherUsers);
        assertEquals(otherUsers, speedLog.getUsers());
    }

    @Test
    void getLectureRoom() {
        assertEquals(lectureRoom, speedLog.getLectureRoom());
    }

    @Test
    void setLectureRoom() {
        LectureRoom lectureRoom1 = new LectureRoom();
        lectureRoom1.setLecturePin("420021Koen");

        speedLog.setLectureRoom(lectureRoom1);
        assertEquals(lectureRoom1, speedLog.getLectureRoom());
    }

    @Test
    void getSpeed() {
        assertEquals(50, speedLog.getSpeed());
    }

    @Test
    void setSpeed() {
        speedLog.setSpeed(68);
        assertEquals(68, speedLog.getSpeed());
    }

    @Test
    void testEquals() {
        SpeedLog sameSpeedLog = new SpeedLog(testUser, lectureRoom, 50);
        assertEquals(sameSpeedLog, speedLog);
    }

    @Test
    void testSameEquals() {
        assertEquals(speedLog, speedLog);
    }

    @Test
    void testNotEquals() {
        SpeedLog otherSpeedLog = new SpeedLog(testUser, lectureRoom, 100);
        assertNotEquals(otherSpeedLog, speedLog);
    }

    @Test
    void testEqualsOtherObject() {
        assertFalse(speedLog.equals("Wrong Object"));
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(speedLog.getId()), speedLog.hashCode());
    }
}