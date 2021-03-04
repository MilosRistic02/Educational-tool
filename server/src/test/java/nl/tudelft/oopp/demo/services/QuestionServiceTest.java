package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;

    private Question question1 = new Question("question", "4812421dristic", "me");


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
    }

    @Test
    void isQuestionAnswered() {
    }

    @Test
    void getAllQuestions() {
    }

    @Test
    void addQuestion() {
    }

    @Test
    void deleteQuestion() {
    }
}