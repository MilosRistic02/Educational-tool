package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class LectureRoomServiceTest {

    @Autowired
    private LectureRoomService lectureRoomService;

    @MockBean
    private LectureRoomRepository lectureRoomRepository;

    private LectureRoom lectureRoom;

    @BeforeEach
    public void setup() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        lectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
    }

    @Test
    void createPinTest() {
        assertTrue(LectureRoomService.createPin(lectureRoom.getLecturerID())
                .contains(lectureRoom.getLecturerID()));
    }

    @Test
    void addLectureRoomTestSuccess() {
        Mockito.when(lectureRoomRepository.getAllByLecturerID(lectureRoom.getLecturerID()))
                .thenReturn(Collections.singletonList(lectureRoom));
        Mockito.when(lectureRoomRepository.existsByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(false);
        Mockito.when(lectureRoomRepository.save(lectureRoom))
                .thenReturn(lectureRoom);
        assertNotEquals("Too many rooms created under this host",
                lectureRoomService.addLectureRoom(lectureRoom));
    }

    @Test
    void addLectureRoomTestFail() {
        List<LectureRoom> list = new ArrayList<>(50001);
        for (int i = 0; i < 50001; i++) {
            list.add(lectureRoom);
        }
        System.out.println(list.size());
        Mockito.when(lectureRoomRepository.getAllByLecturerID(lectureRoom.getLecturerID()))
                .thenReturn(list);
        assertEquals("Too many rooms created under this host",
                lectureRoomService.addLectureRoom(lectureRoom));
    }
}