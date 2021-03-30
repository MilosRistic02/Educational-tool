package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Date;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.repositories.PollRepository;
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
class PollControllerTest {

    @Autowired
    private PollController pollController;

    @MockBean
    private PollRepository pollRepository;

    private Poll poll = new Poll("lecturepin", 4, Arrays.asList('A', 'B'), "New poll");
    private Poll poll2 = new Poll("lecturepin", 4,Arrays.asList('C', 'D'), "This is a question");

    @Test
    void getPollById() {
        poll.setCreationDate(new Date(2021, 1, 1));
        poll2.setCreationDate(new Date(2021, 1, 2));
        Mockito.when(pollRepository.findAllByLecturePin("lecturepin"))
                .thenReturn(Arrays.asList(poll, poll2));
        assertEquals(pollController.getPollById("lecturepin"), poll2);
    }

    @Test
    void getPollsByLecturePin() {
        Mockito.when(pollRepository.findAllByLecturePin("lecturepin"))
                .thenReturn(Arrays.asList(poll, poll2));
        assertArrayEquals(pollController.getPollsByLecturePin("lecturepin").toArray(),
                Arrays.asList(poll, poll2).toArray());
    }

    @Test
    void createPoll() {
        Mockito.when(pollRepository.save(poll)).thenReturn(poll);
        assertEquals(pollController.createPoll(poll, "me"), poll);
    }

    @Test
    void voteOnPoll() {
        Mockito.when(pollRepository.existsById(0)).thenReturn(true);
        Mockito.when(pollRepository.getById(0)).thenReturn(poll);
        assertEquals("Success", pollController.voteOnPoll('A', 0, "me"));
    }

    @Test
    void closePoll() {
        Mockito.when(pollRepository.save(poll)).thenReturn(poll);
        poll.setOpen(false);
        assertEquals(poll, pollController.closePoll(poll, "me"));
    }
}