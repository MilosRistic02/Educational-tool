package nl.tudelft.oopp.demo.services;

import java.util.Comparator;
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

    public Poll savePoll(Poll poll) {
        return pollRepository.save(poll);
    }

    /**
     * Method for getting the most recent poll in a lecture.
     * @param lecturePin String with the lecturePin
     * @return The most recent poll
     */
    public Poll getMostRecent(String lecturePin) {
        List<Poll> polls = pollRepository.findAllByLecturePin(lecturePin);
        return polls.size() == 0
                ? null
                : polls.stream()
                .max(Comparator.comparing(Poll::getCreationDate))
                .get();
    }
}
