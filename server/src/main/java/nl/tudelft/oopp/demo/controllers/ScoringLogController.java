package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.services.ScoringLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("scoringlog")
public class ScoringLogController {

    private ScoringLogService scoringLogService;

    public ScoringLogController(ScoringLogService scoringLogService) {
        this.scoringLogService = scoringLogService;
    }

    @PostMapping("/vote/{username}")
    @ResponseBody
    public String vote(@RequestBody ScoringLog scoringLog, @PathVariable String username) {
        return scoringLogService.vote(scoringLog, username);
    }

    @PostMapping("/get-votes")
    @ResponseBody
    public List<ScoringLog> getVotes(@RequestBody Users users) {
        return scoringLogService.getVotes(users);
    }

}
