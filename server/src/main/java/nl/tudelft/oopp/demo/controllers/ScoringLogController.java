package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.services.ScoringLogService;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/vote")
    @ResponseBody
    public String vote(@RequestBody ScoringLog scoringLog) {
        return scoringLogService.vote(scoringLog);
    }

}
