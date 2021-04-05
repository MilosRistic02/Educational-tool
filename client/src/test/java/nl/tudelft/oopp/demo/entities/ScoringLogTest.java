package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoringLogTest {
    private ScoringLog scoringLog;
    private ScoringLog scoringLog2;
    private Question question;
    private Question question2;
    private Users users;
    private Users users2;

    @BeforeEach
    void setUp() {
        question = new Question("Question", "piet", "klaas");
        question2 = new Question("Other Question", "klaas", "piet");
        users = new Users("piet", "piet@gmail.com", "pw", "student");
        users2 = new Users("klaas", "klaas@gmail.com", "pw", "student");
        scoringLog = new ScoringLog(question, users,2);
        scoringLog2 = new ScoringLog();
    }

    @Test
    void constructorTest() {
        assertNotNull(scoringLog);
        assertNotNull(scoringLog2);
    }

    @Test
    void getSetId() {
        scoringLog.setId((long) 4);
        assertEquals(4, scoringLog.getId());
    }

    @Test
    void getQuestion() {
        assertEquals(question, scoringLog.getQuestion());
    }

    @Test
    void setQuestion() {
        scoringLog.setQuestion(question2);
        assertEquals(question2, scoringLog.getQuestion());
    }

    @Test
    void getUsers() {
        assertEquals(users, scoringLog.getUsers());
    }

    @Test
    void setUsers() {
        scoringLog.setUsers(users2);
        assertEquals(users2, scoringLog.getUsers());
    }

    @Test
    void getScore() {
        assertEquals(2, scoringLog.getScore());
    }

    @Test
    void setScore() {
        scoringLog.setScore(3);
        assertEquals(3, scoringLog.getScore());
    }

    @Test
    void testEquals() {
        assertEquals(scoringLog, scoringLog);
    }

    @Test
    void testEqualsDiff() {
        scoringLog2.setQuestion(question);
        scoringLog2.setUsers(users);
        scoringLog2.setScore(2);
        scoringLog2.setId(scoringLog.getId());
        assertEquals(scoringLog, scoringLog2);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(scoringLog, scoringLog2);
    }

    @Test
    void testNull() {
        assertNotEquals(scoringLog, null);
    }

    @Test
    void testNotEqualsOtherObject() {
        assertNotEquals(scoringLog, 2);
    }

    @Test
    void testHash() {
        assertEquals(107587548, scoringLog.hashCode());
    }

}