package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import nl.tudelft.oopp.demo.services.LectureRoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LectureRoomControllerTest {

    @Autowired
    private LectureRoomController lectureRoomController;

    @MockBean
    private LectureRoomService lectureRoomService;

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
    void checkPin() {
        Mockito.when(lectureRoomService.existsByPin("1889221jsloof")).thenReturn(true);
        assertTrue(lectureRoomController.checkPin("1889221jsloof"));
    }

    @Test
    void checkPinNeg() {
        Mockito.when(lectureRoomService.existsByPin("1889221jsloof")).thenReturn(false);
        assertFalse(lectureRoomController.checkPin("1889221jsloof"));
    }
}