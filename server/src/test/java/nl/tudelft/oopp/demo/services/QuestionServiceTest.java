package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;

    private Question question1 = new Question("question", "4812421dristic", "me");
    private Question question2 = new Question("question2", "1889221jsloof", "myself");


    @Test
    void updateScoreQuestion() {
        Mockito.when(questionRepository.existsByLecturePin("4812421dristic")).thenReturn(true);
        Mockito.when(questionRepository.getByLecturePin("4812421dristic")).thenReturn(question1);
        Mockito.when(questionRepository.save(question1)).thenReturn(question1);

        assertEquals(questionService.updateScoreQuestion(question1), "Updated score of Question");
    }

    @Test
    void updateScoreQuestionNeg() {
        Mockito.when(questionRepository.existsByLecturePin("4812421dristic")).thenReturn(false);
        assertEquals(questionService.updateScoreQuestion(question1), "Question does not yet exists");
    }

    @Test
    void updateQuestionAnsweredStatus() {
        question1.setAnswered(true);
        Mockito.when(questionRepository.existsByLecturePin("4812421dristic")).thenReturn(true);
        Mockito.when(questionRepository.getByLecturePin("4812421dristic")).thenReturn(question1);
        assertTrue(question1.isAnswered());
        Mockito.when(questionRepository.save(question1)).thenReturn(question1);
        assertEquals(questionService.updateQuestionAnsweredStatus(question1)
                , "The answered status of the question has been updated.");
    }

    @Test
    void updateQuestionAnsweredStatusNeg() {
        Mockito.when(questionRepository.existsByLecturePin("4812421dristic")).thenReturn(false);
        assertEquals(questionService.updateQuestionAnsweredStatus(question1)
                , "The question is not in the database.");
    }

    @Test
    void isQuestionAnswered() {
        question1.setAnswered(true);
        Mockito.when(questionRepository.getByLecturePin("4812421dristic")).thenReturn(question1);
        assertTrue(questionService.isQuestionAnswered(question1));
    }

    @Test
    void getAllQuestions() {
        Mockito.when(questionRepository.findAll()).thenReturn(Arrays.asList(question1, question2));
        assertArrayEquals(Arrays.asList(question1, question2).toArray(), questionService.getAllQuestions().toArray());
    }

    @Test
    void addQuestion() {
        Mockito.when(questionRepository.save(question1)).thenReturn(question1);
        assertEquals(question1.getQuestion(), questionService.addQuestion(question1));
    }

    @Test
    void deleteQuestion() {
        Mockito.when(questionRepository.existsById((long) 61)).thenReturn(true);
        assertTrue(questionService.deleteQuestion((long) 61));
    }

    @Test
    void deleteQuestionNeg() {
        Mockito.when(questionRepository.existsById((long) 61)).thenReturn(false);
        assertFalse(questionService.deleteQuestion((long) 61));
    }
}