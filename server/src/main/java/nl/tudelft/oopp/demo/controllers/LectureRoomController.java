package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.services.LectureRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("{lectureHost}")
    @ResponseBody
    public boolean deleteLectureRoom(@PathVariable String lectureHost) {
        return lectureRoomService.deleteLectureRoom(lectureHost);
    }

    //Made these for development, may get deleted later on if we think we dont need it
    @DeleteMapping
    @ResponseBody
    public boolean deleteAllLectureRooms() {
        return lectureRoomService.deleteAllLectureRooms();
    }

    @GetMapping
    @ResponseBody
    public List<LectureRoom> getAllLectureRooms() {
        return lectureRoomService.getAllLectureRooms();
    }
}
