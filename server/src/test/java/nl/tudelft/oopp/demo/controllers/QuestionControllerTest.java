package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.services.QuestionService;
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
class QuestionControllerTest {

    @Autowired
    private QuestionController questionController;

    @MockBean
    private QuestionService questionService;

    private Question question1 = new Question("question", "4812421dristic", "me");
    private Question question2 = new Question("question2", "4812421dristic", "myself");


    @Test
    void getAllQuestions() {
        Mockito.when(questionService.getAllQuestions("4812421dristic"))
                .thenReturn(Arrays.asList(question1, question2));

        assertArrayEquals(Arrays.asList(question1, question2).toArray(),
                questionController.getAllQuestions("4812421dristic").toArray());
    }

    @Test
    void getAllAnsweredQuestions() {
        Mockito.when(questionService.getAllAnsweredQuestions("4812421dristic"))
                .thenReturn(Arrays.asList(question1, question2));

        assertArrayEquals(Arrays.asList(question1, question2).toArray(),
                questionController.getAllAnsweredQuestions("4812421dristic").toArray());
    }

    @Test
    void getAllNonAnsweredQuestions() {
        Mockito.when(questionService.getAllNonAnsweredQuestions("4812421dristic"))
                .thenReturn(Arrays.asList(question1, question2));

        assertArrayEquals(Arrays.asList(question1, question2).toArray(),
                questionController.getAllNonAnsweredQuestions("4812421dristic").toArray());
    }

    @Test
    void addQuestion() {
        Mockito.when(questionService.addQuestion(question1, "me"))
                .thenReturn(question1.getQuestion());
        assertEquals(question1.getQuestion(),
                questionController.addQuestion(question1, "me"));
    }

    @Test
    void deleteQuestion() {
        Mockito.when(questionService.deleteQuestion(61, "me")).thenReturn(true);
        assertTrue(questionController.deleteQuestion(61, "me"));
    }

    @Test
    void updateAnswerQuestion() {
        Mockito.when(questionService.updateAnswerQuestion(question1, "me"))
                .thenReturn("Success");
        assertEquals("Success", questionController.updateAnswerQuestion(question1, "me"));
    }

    @Test
    void updateContentQuestion() {
        Mockito.when(questionService.updateContentQuestion(question1, "me"))
                .thenReturn("Success");
        assertEquals("Success", questionController.updateContentQuestion(question1, "me"));
    }
}