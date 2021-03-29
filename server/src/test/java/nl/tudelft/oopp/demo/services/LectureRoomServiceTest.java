package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LectureRoomServiceTest {

    @Autowired
    private LectureRoomService lectureRoomService;

    @MockBean
    private LectureRoomRepository lectureRoomRepository;

    @MockBean
    private QuestionRepository questionRepository;

    private LectureRoom lectureRoom;
    private SimpleDateFormat format;


    @BeforeEach
    public void setup() throws ParseException {
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
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
        Mockito.when(lectureRoomRepository.getAllByLecturerID(lectureRoom.getLecturerID()))
                .thenReturn(list);
        assertEquals("Too many rooms created under this host",
                lectureRoomService.addLectureRoom(lectureRoom));
    }

    @Test
    void exportRoomEmpty() throws IOException {
        List<Question> questions = new ArrayList<>();
        Mockito.when(questionRepository
                .getAllByLecturePinOrderByScoreDescCreationDateDesc(lectureRoom.getLecturePin()))
                .thenReturn(questions);

        File actual = new File("Actual");
        lectureRoomService.exportRoom(actual, lectureRoom.getLecturePin());

        Scanner actualReader = new Scanner(actual);
        StringBuilder actualString = new StringBuilder();
        while (actualReader.hasNextLine()) {
            actualString.append(actualReader.nextLine());
        }
        actualReader.close();
        assertEquals("This archive did not contain questions, therefore this file is empty.",
                actualString.toString());
        actual.deleteOnExit();
    }

    @Test
    void exportRoomSuccess() throws IOException {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Does this method work?",
                lectureRoom.getLecturePin(),
                "A curious adventurer"));
        Question question = new Question("yes?",
                lectureRoom.getLecturePin(),
                "A curious adventurer");
        question.setAnswer("ues");
        questions.add(question);
        lectureRoom.setLecturePin("4024221b");
        lectureRoom.setCreationDate(lectureRoom.getStartingTime());

        Mockito.when(questionRepository
                .getAllByLecturePinOrderByScoreDescCreationDateDesc(lectureRoom.getLecturePin()))
                .thenReturn(questions);
        Mockito.when(lectureRoomRepository.getByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(lectureRoom);

        File actual = new File("Actual");
        lectureRoomService.exportRoom(actual, lectureRoom.getLecturePin());

        Scanner actualReader = new Scanner(actual);
        StringBuilder actualString = new StringBuilder();
        while (actualReader.hasNextLine()) {
            actualString.append(actualReader.nextLine());
            actualString.append("\n");
        }
        actualReader.close();
        assertEquals("Reasoning and Logic (Thu Apr 01)"
                        + "\n\nQ: Does this method work?"
                        + "\nA: [Insert answer here]"
                        + "\n\nQ: yes?"
                        + "\nA: ues\n\n",
                actualString.toString());
        actual.deleteOnExit();
    }

    @Test
    void getLectureRoomTest() {
        Mockito.when(lectureRoomRepository
                    .getByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(lectureRoom);

        assertEquals(lectureRoom,
                lectureRoomService.getLectureRoom(lectureRoom.getLecturePin()));
    }

    @Test
    void putLectureRoomUpdateTest() {
        Mockito.when(lectureRoomRepository.existsByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(true);

        Mockito.when(lectureRoomRepository.getByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(lectureRoom);

        Mockito.when(lectureRoomRepository.save(lectureRoom))
                .thenReturn(lectureRoom);

        assertEquals("Updated room", lectureRoomService.putLectureRoom(lectureRoom));
    }

    @Test
    void putMissingLectureRoomTest() {
        Mockito.when(lectureRoomRepository
                    .existsByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(false);

        assertEquals("Room does not yet exist",
                    lectureRoomService.putLectureRoom(lectureRoom));
    }

    @Test
    void getClosedLecturePinsTest() throws ParseException {
        LectureRoom closedRoom1 =
                new LectureRoom("Koen", "Confusing",
                                "CSE1400", format.parse("2021-03-01 11:34"));
        LectureRoom closedRoom2 =
                new LectureRoom("Koen", "Confusing",
                        "CSE1400", format.parse("2022-04-01 13:34"));
        closedRoom1.setOpen(false);
        closedRoom2.setOpen(false);

        List<LectureRoom> closedRooms = Arrays.asList(closedRoom2, closedRoom1);

        Mockito.when(lectureRoomRepository.getAllByIsOpenIsFalseOrderByCreationDateDesc())
                .thenReturn(closedRooms);

        assertEquals(closedRooms, lectureRoomService.getClosedLecturePins());
    }

    @Test
    void updateFrequencyTest() throws ParseException {
        String date = "2021-04-01 12:34";
        LectureRoom oldLectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
        lectureRoom.setLecturePin("2431121b");
        Mockito.when(lectureRoomRepository.getByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(oldLectureRoom);
        Mockito.when(lectureRoomRepository.save(oldLectureRoom)).thenReturn(oldLectureRoom);
        lectureRoom.setQuestionFrequency(5);
        assertEquals("success", lectureRoomService.updateFrequency(lectureRoom));
        assertEquals(5, oldLectureRoom.getQuestionFrequency());
    }
}
