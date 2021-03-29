package nl.tudelft.oopp.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("student")
    @ResponseBody
    public List<Users> getAllStudents() {
        return usersService.getAllStudents();
    }


    @PostMapping("login")
    @ResponseBody
    public String authenticateLogin(@RequestBody String json) throws Exception {
        System.out.println(json);
        Map<String, String> info = new ObjectMapper().readValue(json, HashMap.class);
        return usersService.authenticateLogin(info.get("username"), info.get("password"));
    }

    @PostMapping("register")
    @ResponseBody
    public String addUser(@RequestBody Users user) {
        return usersService.addUser(user);
    }

    @PutMapping("ban")
    @ResponseBody
    public String banUser(@RequestBody String username) {
        return usersService.banUser(username);
    }

    @PutMapping("unban")
    @ResponseBody
    public String unbanUser(@RequestBody String username) {
        return usersService.unbanUser(username);
    }

    @GetMapping("search/{search}/{view}")
    @ResponseBody
    public List<Users> searchStudents(@PathVariable String search, @PathVariable boolean view) {
        return usersService.searchStudents(search, view);
    }

    @GetMapping("not-banned")
    @ResponseBody
    public List<Users> getNotBannedStudents() {
        return usersService.getNotBannedStudents();
    }

    @GetMapping("banned")
    @ResponseBody
    public List<Users> getBannedStudents() {
        return usersService.getBannedStudents();
    }

    @GetMapping("check-banned/{username}")
    @ResponseBody
    public boolean isUserBanned(@PathVariable String username) {
        return usersService.isUserBanned(username);
    }
}
