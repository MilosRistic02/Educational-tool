package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.services.SpeedLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("speedlog")
public class SpeedLogController {

    @Autowired
    private SpeedLogService speedLogService;

    @PostMapping("/speed-vote/{username}")
    @ResponseBody
    public String saveSpeedVote(@RequestBody SpeedLog speedLog, @PathVariable String username) {
        return speedLogService.saveSpeedLog(speedLog, username);
    }

    @GetMapping("/get-speed-votes/{lecturePin}")
    @ResponseBody
    public double getSpeedVotes(@PathVariable String lecturePin) {
        return speedLogService.getSpeedVotes(lecturePin);
    }
}
