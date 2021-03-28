package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.services.SpeedLogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class SpeedLogControllerTest {

    @Autowired
    private SpeedLogController speedLogController;

    @MockBean
    private SpeedLogService speedLogService;

    @Test
    void saveSpeedVote() {
        SpeedLog speedLog = new SpeedLog();
        Mockito.when(speedLogService.saveSpeedLog(speedLog))
                .thenReturn("succes");

        assertEquals("succes", speedLogController.saveSpeedVote(speedLog));
    }

    @Test
    void getSpeedVotes() {
        String pin = "123421Karel";
        Mockito.when(speedLogService.getSpeedVotes(pin))
                .thenReturn(50.0);

        assertEquals(50.0, speedLogController.getSpeedVotes(pin));
    }
}