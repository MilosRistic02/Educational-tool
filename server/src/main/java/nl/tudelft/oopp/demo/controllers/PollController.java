package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/poll")
public class PollController {

    private PollService pollService;

    @Autowired
    public PollController(PollService pollService){
        this.pollService = pollService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Poll getPollById(@PathVariable long id){
        return pollService.getPollById(id);
    }

    @GetMapping("/lecture-polls/{pin}")
    @ResponseBody
    public List<Poll> getPollsByLecturePin(@PathVariable String lecturePin){
            return pollService.getAllPollsByLecturePin(lecturePin);
    }
    @PostMapping("/create")
    @ResponseBody
    public Poll createPoll(@RequestBody Poll poll){
        return pollService.createPoll(poll);
    }
    @PutMapping("/vote/{id}")
    @ResponseBody
    public String voteOnPoll(@RequestBody Character c, @PathVariable long id){
            return pollService.voteOnPoll(c, id);
    }
}
