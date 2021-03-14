package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.services.SpeedLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("speedlog")
public class SpeedLogController {

    @Autowired
    private SpeedLogService speedLogService;

    @PostMapping("/speed-vote")
    @ResponseBody
    public String saveSpeedVote(@RequestBody SpeedLog speedLog) {
        return speedLogService.saveSpeedLog(speedLog);
    }

    @GetMapping("/get-speed-votes")
    @ResponseBody
    public List<SpeedLog> getSpeedVotes() {
        return speedLogService.getSpeedVotes();
    }
}
