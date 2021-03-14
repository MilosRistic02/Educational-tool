package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/poll")
public class PollController {

    private PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Poll getPollById(@PathVariable long id) {
        return pollService.getPollById(id);
    }

    @GetMapping("/lecture-polls/{pin}")
    @ResponseBody
    public List<Poll> getPollsByLecturePin(@PathVariable String pin) {
        return pollService.getAllPollsByLecturePin(pin);
    }

    @PostMapping("/create")
    @ResponseBody
    public Poll createPoll(@RequestBody Poll poll) {
        return pollService.createPoll(poll);
    }

    @PutMapping("/vote/{id}")
    @ResponseBody
    public String voteOnPoll(@RequestBody Character c, @PathVariable long id) {
        return pollService.voteOnPoll(c, id);
    }
}
