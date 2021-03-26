package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired
    private QuestionController questionController;

    @MockBean
    private QuestionService questionService;

    private Question question1 = new Question("question", "4812421dristic", "me");
    private Question question2 = new Question("question2", "1889221jsloof", "myself");


    @Test
    void getAllQuestions() {
        Mockito.when(questionService.getAllQuestions("4812421dristic")).thenReturn(Arrays.asList(question1, question2));
        assertArrayEquals(Arrays.asList(question1, question2).toArray(), questionController.getAllQuestions("4812421dristic").toArray());
    }

    @Test
    void addQuestion() {
        Mockito.when(questionService.addQuestion(question1)).thenReturn(question1.getQuestion());
        assertEquals(question1.getQuestion(), questionController.addQuestion(question1));
    }

    @Test
    void deleteQuestion() {
        Mockito.when(questionService.deleteQuestion(61)).thenReturn(true);
        assertTrue(questionController.deleteQuestion(61));
    }
}