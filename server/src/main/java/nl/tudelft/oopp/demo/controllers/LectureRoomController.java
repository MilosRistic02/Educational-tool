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

    @PostMapping("/{username}")
    @ResponseBody
    public String addLectureRoom(@RequestBody LectureRoom lectureRoom,
                                 @PathVariable String username) {
        return lectureRoomService.addLectureRoom(lectureRoom, username);
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

    @PutMapping("/{username}")
    @ResponseBody
    public String putLectureRoom(@RequestBody LectureRoom lectureRoom,
                                 @PathVariable String username) {
        return lectureRoomService.putLectureRoom(lectureRoom, username);
    }

    @PutMapping("/update-frequency/{username}")
    @ResponseBody
    public String updateFrequency(@RequestBody LectureRoom lectureRoom,
                                  @PathVariable String username) {
        return lectureRoomService.updateFrequency(lectureRoom, username);
    }

    @GetMapping("/getClosed")
    @ResponseBody
    public List<LectureRoom> getClosedLecturePins() {
        return lectureRoomService.getClosedLecturePins();
    }

}
