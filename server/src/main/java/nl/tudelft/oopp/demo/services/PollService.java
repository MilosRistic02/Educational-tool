package nl.tudelft.oopp.demo.services;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollService {

    private PollRepository pollRepository;

    @Autowired
    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public List<Poll> getAllPollsByLecturePin(String pin) {
        return pollRepository.findAllByLecturePin(pin);
    }

    /**
     * Method used for voting on a poll.
     * @param c Character with the vote
     * @param id Long with the id of the poll
     * @return String with whether the vote was valid
     */
    public String voteOnPoll(Character c, long id) {
        if (!pollRepository.existsById(id)) {
            return "This poll does not exist";
        }
        Poll retrievedPoll = pollRepository.getById(id);
        String message = retrievedPoll.vote(c);
        pollRepository.save(retrievedPoll);
        return message;
    }

    public Poll createPoll(Poll poll) {
        return pollRepository.save(poll);
    }

    public Poll getPollById(long id) {
        return pollRepository.getById(id);
    }
}
