package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.services.LectureRoomService;
import org.junit.jupiter.api.BeforeEach;
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

    private LectureRoom lectureRoom;
    private LectureRoom lectureRoom1;

    @BeforeEach
    public void setup() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        lectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
        lectureRoom.setLecturePin("2802202001Stefan");

        lectureRoom1 = new LectureRoom("Koen", "Confusion Island",
                "CSE1400", format.parse(date));
    }

    @Test
    void addLectureRoomTest() {
        Mockito.when(lectureRoomService.addLectureRoom(lectureRoom))
                .thenReturn("Too many rooms created under this host");

        assertEquals("Too many rooms created under this host",
                        lectureRoomController.addLectureRoom(lectureRoom));
    }

    @Test
    void exportRoomTest() throws IOException {
        File testFile = new File("testFile");

        Mockito.when(lectureRoomService.exportRoom(testFile, lectureRoom.getLecturePin()))
                .thenReturn(testFile);

        assertEquals(testFile,
                lectureRoomController.exportRoom(testFile, lectureRoom.getLecturePin()));

        testFile.deleteOnExit();
    }

    @Test
    void getLectureRoomTest() {
        Mockito.when(lectureRoomService.getLectureRoom(lectureRoom.getLecturePin()))
                .thenReturn(lectureRoom);

        assertEquals(lectureRoom,
                lectureRoomController.getLectureRoom(lectureRoom.getLecturePin()));
    }

    @Test
    void putLectureRoomTest() {
        lectureRoom.setOpen(false);

        Mockito.when(lectureRoomService.putLectureRoom(lectureRoom))
                .thenReturn("succes");

        assertEquals("succes", lectureRoomController.putLectureRoom(lectureRoom));
    }

    @Test
    void updateFrequencyTest() {
        lectureRoom.setQuestionFrequency(50);

        Mockito.when(lectureRoomService.updateFrequency(lectureRoom))
                .thenReturn("succes");

        assertEquals("succes", lectureRoomController.updateFrequency(lectureRoom));
    }

    @Test
    void getClosedLecturePinsTest() {
        List<LectureRoom> rooms = new ArrayList<>();
        lectureRoom.setOpen(false);
        rooms.add(lectureRoom);

        Mockito.when(lectureRoomService.getClosedLecturePins())
                .thenReturn(rooms);

        assertEquals(rooms, lectureRoomController.getClosedLecturePins());
    }
}