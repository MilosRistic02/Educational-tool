package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.oopp.demo.services.LectureRoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    }

    @Test
    void checkPinNeg() {
    }
}