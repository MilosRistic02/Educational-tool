package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.repositories.SpeedLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpeedLogServiceTest {

    @Autowired
    private SpeedLogService speedLogService;

    @MockBean
    private SpeedLogRepository speedLogRepository;

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
    void saveExistingSpeedLogTest() {
        SpeedLog newSpeedLog = speedLog;
        newSpeedLog.setSpeed(60);

        Mockito.when(speedLogRepository
                    .existsByUsersAndLectureRoom(testUser, lectureRoom))
                .thenReturn(true);

        Mockito.when(speedLogRepository
                        .getByUsersAndLectureRoom(testUser, lectureRoom))
                .thenReturn(speedLog);

        Mockito.when(speedLogRepository.save(newSpeedLog))
                .thenReturn(newSpeedLog);

        String response = speedLogService.saveSpeedLog(newSpeedLog);
        assertEquals(60, speedLog.getSpeed());
        assertEquals("succes", response);
    }

    @Test
    void saveNewSpeedLogTest() throws ParseException {
        Users testUser1 = new Users("Sigrid", "S@Gmail.com", "1237", "student");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        LectureRoom lectureRoom1 = new LectureRoom("Koen", "Reasoning and Logic",
                "CSE2200", format.parse(date));
        lectureRoom1.setLecturePin("1234521Piet");

        SpeedLog newSpeedLog = new SpeedLog(testUser1, lectureRoom1, 30);

        Mockito.when(speedLogRepository
                .existsByUsersAndLectureRoom(testUser, lectureRoom))
                .thenReturn(false);

        Mockito.when(speedLogRepository.save(newSpeedLog))
                .thenReturn(newSpeedLog);

        String response = speedLogService.saveSpeedLog(newSpeedLog);
        assertEquals("succes", response);
    }

    @Test
    void getSpeedVotesTest() {
        Mockito.when(speedLogRepository
                    .getSpeedAverageByLecturePin(lectureRoom.getLecturePin()))
                .thenReturn(50.0);

        assertEquals(50.0, speedLogService.getSpeedVotes(lectureRoom.getLecturePin()));
    }
}