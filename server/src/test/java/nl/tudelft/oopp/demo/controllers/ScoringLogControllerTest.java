package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.services.ScoringLogService;
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
class ScoringLogControllerTest {

    @Autowired
    private ScoringLogController scoringLogController;

    @MockBean
    private ScoringLogService scoringLogService;

    @Test
    void vote() {
        ScoringLog scoringLog = new ScoringLog();
        Mockito.when(scoringLogService.vote(scoringLog))
                .thenReturn("success");
        assertEquals("success", scoringLogController.vote(scoringLog));
    }

    @Test
    void getVotes() {
        Users users = new Users();
        Mockito.when(scoringLogService.getVotes(users))
                .thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(), scoringLogController.getVotes(users));
    }
}