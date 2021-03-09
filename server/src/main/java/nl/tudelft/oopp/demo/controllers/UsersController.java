package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("user")
    @ResponseBody
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("student")
    @ResponseBody
    public List<Users> getAllStudents() {
        return usersService.getAllStudents();
    }

    @GetMapping("moderator")
    @ResponseBody
    public List<Users> getAllModerators() {
        return usersService.getAllModerators();
    }

    @GetMapping("lecturer")
    @ResponseBody
    public List<Users> getAllLecturers() {
        return usersService.getAllLecturers();
    }


    @PostMapping
    @ResponseBody
    public String addUser(@RequestBody Users user) {
        return usersService.addUser(user);
    }

    @DeleteMapping
    @ResponseBody
    public String deleteUser(@RequestBody Users user) {
        return usersService.deleteUser(user);
    }

    @DeleteMapping("user")
    public void deleteAllUsers() {
        usersService.deleteAllUsers();
    }

    @DeleteMapping("student")
    public void deleteAllStudents() {
        usersService.deleteAllStudents();
    }

    @DeleteMapping("moderator")
    public void deleteAllModerators() {
        usersService.deleteAllModerators();
    }

    @DeleteMapping("lecturer")
    public void deleteAllLecturers() {
        usersService.deleteAllLecturers();
    }

}
