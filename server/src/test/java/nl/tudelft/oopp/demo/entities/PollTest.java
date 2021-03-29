package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PollTest {

    Poll poll;
    Poll poll2;

    @BeforeEach
    void setup() {
        poll = new Poll("lecturepin", 4, Arrays.asList('A', 'B'), "New poll");
        poll2 = new Poll();
    }

    @Test
    void constructorTest() {
        assertNotNull(poll);
        assertNotNull(poll2);
    }

    @Test
    void setId() {
        poll.setId(5);
        assertEquals(poll.getId(), 5);
    }

    @Test
    void setLecturePin() {
        poll.setLecturePin("new lecture pin");
        assertEquals(poll.getLecturePin(), "new lecture pin");
    }

    @Test
    void setSize() {
        poll.setSize(10);
        assertEquals(poll.getSize(), 10);
    }

    @Test
    void setOpen() {
        poll.setOpen(false);
        assertFalse(poll.isOpen);
    }

    @Test
    void setRightAnswer() {
        poll.setRightAnswer(Arrays.asList('C', 'D'));
        assertEquals(poll.getRightAnswer(), Arrays.asList('C', 'D'));
    }

    @Test
    void setQuestion() {
        poll.setQuestion("This is a question");
        assertEquals(poll.getQuestion(), "This is a question");
    }

    @Test
    void setVotes() {
        int[] votes = new int[10];
        votes[5] = 13;
        poll.setVotes(votes);
        assertEquals(poll.getVotes(), votes);
    }

    @Test
    void setCreationDate() {
        Date date = new Date();
        poll.setCreationDate(date);
        assertEquals(poll.getCreationDate(), date);
    }

    @Test
    void getId() {
        assertEquals(poll.getId(), 0);
    }

    @Test
    void getLecturePin() {
        assertEquals(poll.getLecturePin(), "lecturepin");
    }

    @Test
    void getSize() {
        assertEquals(poll.getSize(), 4);
    }

    @Test
    void isOpen() {
        assertTrue(poll.isOpen());
    }

    @Test
    void getRightAnswer() {
        assertEquals(poll.getRightAnswer(), Arrays.asList('A', 'B'));
    }

    @Test
    void getQuestion() {
        assertEquals(poll.getQuestion(), "New poll");
    }

    @Test
    void getVotes() {
        int[] votes = new int[10];
        assertTrue(Arrays.equals(votes, poll.getVotes()));
    }

    @Test
    void getCreationDate() {
        Date date = new Date();
        poll.setCreationDate(date);
        assertEquals(poll.getCreationDate(), date);
    }

    @Test
    void voteNegative() {
        assertEquals(poll.vote('1'), "Vote invalid");
    }

    @Test
    void voteTooBig() {
        assertEquals(poll.vote('F'), "Vote invalid");
    }

    @Test
    void voteClosedPoll() {
        poll.setOpen(false);
        assertEquals(poll.vote('A'), "Poll closed");
    }

    @Test
    void voteSuccess() {
        poll.vote('A');

        assertTrue(Arrays.equals(poll.getVotes(),
                new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    }

    @Test
    void testEquals() {
        Poll poll2 = new Poll();
        poll2.setId(2);
        poll.setId(2);
        assertEquals(poll2, poll);
    }

    @Test
    void testSameEquals() {
        assertEquals(poll, poll);
    }

    @Test
    void testNotEquals() {
        Poll poll2 = new Poll();
        poll2.setId(3);
        assertNotEquals(poll, poll2);
    }

    @Test
    void testEqualsOtherObject() {
        assertNotEquals("Hello", poll);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(null, poll);
    }


}