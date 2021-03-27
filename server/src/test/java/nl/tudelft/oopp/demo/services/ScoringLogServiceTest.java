package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.repositories.ScoringLogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
@AutoConfigureMockMvc
class ScoringLogServiceTest {

    @Autowired
    private ScoringLogService scoringLogService;

    @MockBean
    private ScoringLogRepository scoringLogRepository;

    Question question = new Question("This is a question", "3040", "Rodrigo");
    Users users = new Users("rorro", "rorro@hotmail.es", "pass", "student");
    ScoringLog scoringLogUpvote = new ScoringLog(question, users, 1);
    ScoringLog scoringLogDownVote = new ScoringLog(question, users, -1);

    //    @Test
    //    void vote() {
    //        Mockito
    //                .when(scoringLogRepository
    //                        .existsByQuestionAndUsers(question, users))
    //                .thenReturn(true);
    //        Mockito
    //                .when(scoringLogRepository
    //                        .getByQuestionAndUsers(question, users))
    //                .thenReturn(scoringLogUpvote);
    //        Mockito
    //                .when(scoringLogRepository
    //                        .save(scoringLogDownVote))
    //                .thenReturn(scoringLogDownVote);
    //
    //        assertEquals(scoringLogService.vote(scoringLogDownVote), "Success");
    //    }

    @Test
    void getVotes() {
        Mockito
                .when(scoringLogRepository
                        .findAllByUsers(users))
                .thenReturn(Arrays.asList(scoringLogDownVote, scoringLogUpvote));
        assertEquals(Arrays.asList(scoringLogDownVote, scoringLogUpvote),
                scoringLogService.getVotes(users));
    }
}