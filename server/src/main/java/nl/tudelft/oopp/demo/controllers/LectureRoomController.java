package nl.tudelft.oopp.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.services.LectureRoomService;
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
@RequestMapping("lecture")
public class LectureRoomController {

    private LectureRoomService lectureRoomService;

    @Autowired
    public LectureRoomController(LectureRoomService lectureRoomService) {
        this.lectureRoomService = lectureRoomService;
    }

    @PostMapping
    @ResponseBody
    public String addLectureRoom(@RequestBody LectureRoom lectureRoom) {
        return lectureRoomService.addLectureRoom(lectureRoom);
    }

    @PostMapping("/file/{pin}")
    @ResponseBody
    public File exportRoom(@RequestBody File file, @PathVariable String pin) throws IOException {
        return lectureRoomService.exportRoom(file, pin);
    }

    @GetMapping("/{pin}")
    @ResponseBody
    public LectureRoom getLectureRoom(@PathVariable String pin) {
        return lectureRoomService.getLectureRoom(pin);
    }

    @PutMapping()
    @ResponseBody
    public String putLectureRoom(@RequestBody LectureRoom lectureRoom) {
        return lectureRoomService.putLectureRoom(lectureRoom);
    }

    @PutMapping("/update-frequency")
    @ResponseBody
    public String updateFrequency(@RequestBody LectureRoom lectureRoom) {
        return lectureRoomService.updateFrequency(lectureRoom);
    }

    @GetMapping("/getClosed")
    @ResponseBody
    public List<LectureRoom> getClosedLecturePins() {
        return lectureRoomService.getClosedLecturePins();
    }

}
