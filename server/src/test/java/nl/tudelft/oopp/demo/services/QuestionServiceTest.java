package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.LectureRoomRepository;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
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
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private LectureRoomRepository lectureRoomRepository;

    private Question question1 = new Question("question", "4812421dristic", "me");
    private Question question2 = new Question("question2", "1889221jsloof", "myself");
    private Question question3 = new Question("question3", "1889221jsloof", "I");
    private Question question4 = new Question("question4", "4812421dristic", "me");
    private LectureRoom lectureRoom1 = new LectureRoom();


    @Test
    void updateAnswerQuestion() {
        Mockito.when(questionRepository.existsByIdAndAndLecturePin(
                question1.getId(), question1.getLecturePin())).thenReturn(true);
        Mockito.when(questionRepository.getByIdAndLecturePin(
                question1.getId(), question1.getLecturePin())).thenReturn(question1);
        Mockito.when(questionRepository.save(question1)).thenReturn(question1);
        assertEquals("Updated answer of Question",
                questionService.updateAnswerQuestion(question1, "me"));
    }

    @Test
    void updateAnswerQuestionNeg() {
        Mockito.when(questionRepository.existsByIdAndAndLecturePin(
                question1.getId(), question1.getLecturePin())).thenReturn(false);
        assertEquals("Question does not yet exists",
                questionService.updateAnswerQuestion(question1, "me"));
    }

    @Test
    void updateContentQuestion() {
        Mockito.when(questionRepository.existsByIdAndAndLecturePin(
                question1.getId(), question1.getLecturePin())).thenReturn(true);
        Mockito.when(questionRepository.getByIdAndLecturePin(
                question1.getId(), question1.getLecturePin())).thenReturn(question1);
        Mockito.when(questionRepository.save(question1)).thenReturn(question1);
        assertEquals("Updated content of Question",
                questionService.updateContentQuestion(question1, "me"));
    }

    @Test
    void updateContentQuestionNeg() {
        Mockito.when(questionRepository.existsByIdAndAndLecturePin(
                question1.getId(), question1.getLecturePin())).thenReturn(false);
        assertEquals("Question does not yet exists",
                questionService.updateContentQuestion(question1, "me"));
    }

    @Test
    void isQuestionAnswered() {
        question1.setAnswered(1);
        Mockito.when(questionRepository.getByLecturePin("4812421dristic")).thenReturn(question1);
        assertEquals(1, questionService.isQuestionAnswered(question1));
    }

    @Test
    void getAllQuestions() {
        Mockito.when(questionRepository
                        .getAllByLecturePinOrderByScoreDescCreationDateDesc("4812421dristic"))
                .thenReturn(Arrays.asList(question1, question2));

        assertArrayEquals(Arrays.asList(question1, question2).toArray(),
                questionService.getAllQuestions("4812421dristic").toArray());
    }

    @Test
    void getAllAnsweredQuestions() {
        Mockito.when(questionRepository
                .getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(1, "1889221jsloof"))
                .thenReturn(Arrays.asList(question2));
        Mockito.when(questionRepository
                .getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(2, "1889221jsloof"))
                .thenReturn(Arrays.asList(question3));
        question2.setAnswered(1);
        question3.setAnswered(2);
        assertArrayEquals(Arrays.asList(question2,question3).toArray(),
                questionService.getAllAnsweredQuestions("1889221jsloof").toArray());
    }

    @Test
    void getAllNonAnsweredQuestions() {
        Mockito.when(questionRepository
                .getAllByAnsweredAndLecturePinOrderByScoreDescCreationDateDesc(0, "4812421dristic"))
                .thenReturn(Arrays.asList(question1));
        question1.setAnswered(0);
        assertArrayEquals(Arrays.asList(question1).toArray(),
                questionService.getAllNonAnsweredQuestions("4812421dristic").toArray());
    }

    @Test
    void addQuestionNullTest() {
        Mockito.when(questionRepository
                .findTopByLecturePinAndAuthorOrderByCreationDateDesc("4812421dristic", "me"))
                .thenReturn(null);
        Mockito.when(questionRepository.save(question1)).thenReturn(question1);
        assertEquals("Success", questionService.addQuestion(question1, "me"));
    }

    @Test
    void addQuestionPosTest() {
        question1.setCreationDate(new Date(System.currentTimeMillis()));
        lectureRoom1.setQuestionFrequency(0);
        Mockito.when(questionRepository
                .findTopByLecturePinAndAuthorOrderByCreationDateDesc("4812421dristic", "me"))
                .thenReturn(question1);
        Mockito.when(lectureRoomRepository
                .getByLecturePin("4812421dristic"))
                .thenReturn(lectureRoom1);
        Mockito.when(questionRepository.save(question4)).thenReturn(question4);
        assertEquals("Success", questionService.addQuestion(question1, "me"));
    }

    @Test
    void addQuestionNegTest() {
        question1.setCreationDate(new Date(System.currentTimeMillis()));
        lectureRoom1.setQuestionFrequency(10);
        Mockito.when(questionRepository
                .findTopByLecturePinAndAuthorOrderByCreationDateDesc("4812421dristic", "me"))
                .thenReturn(question1);
        Mockito.when(lectureRoomRepository
                .getByLecturePin("4812421dristic"))
                .thenReturn(lectureRoom1);
        Mockito.when(questionRepository.save(question4)).thenReturn(question4);
        assertEquals("Need to wait 10 seconds per question",
                questionService.addQuestion(question1, "me"));
    }

    @Test
    void deleteQuestion() {
        Mockito.when(questionRepository.existsById((long) 61)).thenReturn(true);
        assertTrue(questionService.deleteQuestion((long) 61, "me"));
    }

    @Test
    void deleteQuestionNeg() {
        Mockito.when(questionRepository.existsById((long) 61)).thenReturn(false);
        assertFalse(questionService.deleteQuestion((long) 61, "me"));
    }
}