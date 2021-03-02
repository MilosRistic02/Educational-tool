package nl.tudelft.oopp.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class QuestionTest {
    Question question;

    Question question1;

    Question question2;

    String questionString = "Is this a good example question?";

    String lecturePin = "2802202001Stefan";

    String author = "Stefan";

    @BeforeEach
    public void setUp() {
        question = new Question();
        question1 = new Question(questionString, lecturePin, author);
        question2 = new Question(questionString, lecturePin, author);
    }

    @Test
    void constructorTest() {
        assertNotNull(question);
        assertNotNull(question1);
    }

    @Test
    void getId() {
        question1.setId(12345);
        assertEquals(12345, question1.getId());
    }

    @Test
    void getQuestion() {
        assertEquals(questionString, question1.getQuestion());
    }

    @Test
    void getAnswer() {
        assertEquals(null, question1.getAnswer());
    }

    @Test
    void getScore() {
        assertEquals(0, question1.getScore());
    }

    @Test
    void isAnswered() {
        assertFalse(question1.isAnswered());
    }

    @Test
    void getLecturePin() {
        assertEquals(lecturePin, question1.getLecturePin());
    }

    @Test
    void getAuthor() {
        assertEquals(author, question1.getAuthor());
    }

    @Test
    void setId() {
        question.setId(1234);
        assertEquals(1234, question.getId());
    }

    @Test
    void setQuestion() {
        question.setQuestion("Whats the quesion?");
        assertEquals("Whats the quesion?", question.getQuestion());
    }

    @Test
    void setScore() {
        question.setScore(5);
        assertEquals(5, question.getScore());
    }

    @Test
    void setAnswered() {
        question.setAnswered(true);
        assertTrue(question.isAnswered());
    }

    @Test
    void setAnswer() {
        question.setAnswer("Yes");
        assertEquals("Yes", question.getAnswer());
    }

    @Test
    void setLecturePin() {
        question.setLecturePin(lecturePin);
        assertEquals(lecturePin, question.getLecturePin());
    }

    @Test
    void testEquals() {
        question1.setId(1111);
        question2.setId(1111);
        assertEquals(question1, question2);
    }

    @Test
    void testSameEquals() {
        assertEquals(question1, question1);
    }

    @Test
    void testNotEquals() {
        question1.setId(1111);
        assertNotEquals(question1, question2);
    }

}