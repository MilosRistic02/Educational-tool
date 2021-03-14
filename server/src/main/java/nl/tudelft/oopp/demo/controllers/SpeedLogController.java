package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.services.SpeedLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("speedlog")
public class SpeedLogController {

    @Autowired
    private SpeedLogService speedLogService;
}
