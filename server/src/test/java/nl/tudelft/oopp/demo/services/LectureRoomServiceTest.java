package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LectureRoomServiceTest {

    @Autowired
    private LectureRoomService lectureRoomService;

    @MockBean
    private LectureRoomRepository lectureRoomRepository;

    private LectureRoom lectureRoom;

    @BeforeEach
    void setUp() {
        lectureRoom = new LectureRoom("jsloof", 1200);
        lectureRoom.setLecturePin("1889221jsloof");
    }


    @Test
    void createPin() {
    }

    @Test
    void addLectureRoom() {
    }

    @Test
    void deleteLectureRoom() {
    }

    @Test
    void deleteAllLectureRooms() {
    }

    @Test
    void getAllLectureRooms() {
    }

    @Test
    void existsByPin() {
        Mockito.when(lectureRoomRepository.existsByLecturePin("1889221jsloof")).thenReturn(true);
        assertTrue(lectureRoomService.existsByPin("1889221jsloof"));
    }

    @Test
    void existsByPinNeg() {
        Mockito.when(lectureRoomRepository.existsByLecturePin("1889221jsloof")).thenReturn(false);
        assertFalse(lectureRoomService.existsByPin("1889221jsloof"));
    }
}