package nl.tudelft.oopp.demo.controllers;


import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.entities.Moderator;
import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @GetMapping
    @ResponseBody
    public List<Users> getAllUsers() { return usersService.getAllUsers(); }

    @GetMapping
    @ResponseBody
    public List<Student> getAllStudents() { return usersService.getAllStudents(); }

    @GetMapping
    @ResponseBody
    public List<Moderator> getAllModerators() { return usersService.getAllModerators(); }

    @GetMapping
    @ResponseBody
    public List<Lecturer> getAllLecturers() { return usersService.getAllLecturers(); }


    @PostMapping
    @ResponseBody
    public String addUser(@RequestBody Users user){
        return usersService.addUser(user);
    }
    @DeleteMapping
    @ResponseBody
    public String deleteUser(@RequestBody Users user){
        return usersService.deleteUser(user);
    }

    @DeleteMapping
    public void deleteAllUsers(){
         usersService.deleteAllUsers();
    }
    @DeleteMapping
    public void deleteAllStudents(){
        usersService.deleteAllStudents();
    }
    @DeleteMapping
    public void deleteAllModerators(){
        usersService.deleteAllModerators();
    }
    @DeleteMapping
    public void deleteAllLecturers(){
        usersService.deleteAllLecturers();
    }

}
