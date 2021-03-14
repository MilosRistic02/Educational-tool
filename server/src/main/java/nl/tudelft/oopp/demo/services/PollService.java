package nl.tudelft.oopp.demo.services;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {

    private PollRepository pollRepository;

    @Autowired
    public PollService(PollRepository pollRepository){
        this.pollRepository = pollRepository;
    }

    public List<Poll> getAllPollsByLecturePin(String pin){ return pollRepository.findAllByLecturePin(pin);}

    public String voteOnPoll(Character c, long id){
        if(!pollRepository.existsById(id)){
            return "This poll does not exist";
        }
        Poll retrievedPoll = pollRepository.getById(id);
        String message = retrievedPoll.vote(c);
        pollRepository.save(retrievedPoll);
        return message;
    }

    public Poll createPoll(Poll poll){
        return pollRepository.save(poll);
    }

    public Poll getPollById(long id){
        return pollRepository.getById(id);
    }
}
