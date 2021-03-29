package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.ScoringLogRepository;
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
class ScoringLogServiceTest {

    @Autowired
    private ScoringLogService scoringLogService;

    @MockBean
    private ScoringLogRepository scoringLogRepository;

    @MockBean
    private QuestionRepository questionRepository;

    Question question = new Question("This is a question", "3040", "Rodrigo");
    Users users = new Users("rorro", "rorro@hotmail.es", "pass", "student");
    ScoringLog scoringLogUpvote = new ScoringLog(question, users, 1);
    ScoringLog scoringLogDownVote = new ScoringLog(question, users, -1);
    ScoringLog scoringLogDownVoteLow = new ScoringLog(question, users, -1);

    @Test
    void voteNonExistent() {
        Mockito.when(questionRepository
                        .existsById(question.getId()))
                .thenReturn(false);

        assertEquals("This question does not exist!",
                scoringLogService.vote(scoringLogDownVote));
    }

    @Test
    void vote() {
        Mockito.when(questionRepository.existsById(question.getId()))
                .thenReturn(true);
        Mockito.when(scoringLogRepository
                .existsByQuestionAndUsers(scoringLogDownVote.getQuestion(),
                        scoringLogDownVote.getUsers()))
                .thenReturn(true);
        Mockito.when(questionRepository.getByIdAndLecturePin(scoringLogDownVote.getQuestion()
                        .getId(), scoringLogDownVote.getQuestion().getLecturePin()))
                .thenReturn(question);
        Mockito.when(scoringLogRepository.getByQuestionAndUsers(scoringLogDownVote.getQuestion(),
                scoringLogDownVote.getUsers())).thenReturn(scoringLogDownVote);
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        Mockito.when(scoringLogRepository.save(scoringLogDownVote)).thenReturn(scoringLogDownVote);

        assertEquals("Success", scoringLogService.vote(scoringLogDownVote));
    }

    @Test
    void voteLowScore() {
        question.setScore(-6);
        Mockito.when(questionRepository.existsById(question.getId()))
                .thenReturn(true);
        Mockito.when(scoringLogRepository
                .existsByQuestionAndUsers(scoringLogDownVoteLow.getQuestion(),
                        scoringLogDownVote.getUsers()))
                .thenReturn(true);
        Mockito.when(questionRepository.getByIdAndLecturePin(scoringLogDownVoteLow.getQuestion()
                        .getId(), scoringLogDownVoteLow.getQuestion().getLecturePin()))
                .thenReturn(question);
        Mockito.when(scoringLogRepository.getByQuestionAndUsers(scoringLogDownVoteLow.getQuestion(),
                scoringLogDownVoteLow.getUsers()))
                .thenReturn(scoringLogDownVoteLow);
        Mockito.doNothing().when(questionRepository).delete(question);
        Mockito.when(scoringLogRepository.deleteByQuestion(question)).thenReturn(1);

        assertEquals("Question is deleted",
                scoringLogService.vote(scoringLogDownVoteLow));
    }

    @Test
    void voteNoLog() {
        Mockito.when(questionRepository.existsById(question.getId()))
                .thenReturn(true);
        Mockito.when(scoringLogRepository
                .existsByQuestionAndUsers(scoringLogDownVoteLow.getQuestion(),
                        scoringLogDownVote.getUsers()))
                .thenReturn(false);
        Mockito.when(questionRepository.getByIdAndLecturePin(scoringLogDownVoteLow.getQuestion()
                        .getId(), scoringLogDownVoteLow.getQuestion().getLecturePin()))
                .thenReturn(question);
        Mockito.when(scoringLogRepository.getByQuestionAndUsers(scoringLogDownVoteLow.getQuestion(),
                scoringLogDownVoteLow.getUsers()))
                .thenReturn(scoringLogDownVoteLow);
        Mockito.when(questionRepository.save(question)).thenReturn(question);
        Mockito.when(scoringLogRepository.save(scoringLogDownVote)).thenReturn(scoringLogDownVote);

        assertEquals("Success",
                scoringLogService.vote(scoringLogDownVoteLow));
    }

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