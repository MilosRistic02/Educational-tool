package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
class PollServiceTest {

    @Autowired
    private PollService pollService;

    @MockBean
    private PollRepository pollRepository;

    private Poll poll = new Poll("lecturepin", 4,Arrays.asList('A', 'B'), "New poll");
    private Poll poll2 = new Poll("lecturepin", 4,Arrays.asList('C', 'D'), "This is a question");

    @Test
    void getAllPollsByLecturePin() {
        Mockito.when(pollRepository.findAllByLecturePin("lecturepin"))
                .thenReturn(Arrays.asList(poll, poll2));

        assertArrayEquals(pollService.getAllPollsByLecturePin("lecturepin").toArray(),
                Arrays.asList(poll, poll2).toArray());
    }

    @Test
    void voteOnNonExistentPoll() {
        Mockito.when(pollRepository.existsById(5)).thenReturn(false);
        assertEquals(pollService.voteOnPoll('A', 5, "me"), "This poll does not exist");
    }

    @Test
    void voteOnPoll() {
        Mockito.when(pollRepository.existsById(0)).thenReturn(true);
        Mockito.when(pollRepository.getById(0)).thenReturn(poll);
        assertEquals("Success", pollService.voteOnPoll('A', 0, "me"));
    }

    @Test
    void savePoll() {
        Mockito.when(pollRepository.save(poll)).thenReturn(poll);
        assertEquals(pollService.savePoll(poll, "me"), poll);
    }

    @Test
    void getMostRecent() {
        poll.setCreationDate(new Date(2021, 1, 1));
        poll2.setCreationDate(new Date(2021, 1, 2));
        Mockito.when(pollRepository.findAllByLecturePin("lecturepin"))
                .thenReturn(Arrays.asList(poll, poll2));
        assertEquals(poll2, pollService.getMostRecent("lecturepin"));
    }

    @Test
    void getMostRecentNoLecturePin() {
        Mockito.when(pollRepository.findAllByLecturePin("lecturepin"))
                .thenReturn(new ArrayList<>());
        assertEquals(null, pollService.getMostRecent("lecturepin"));
    }
}