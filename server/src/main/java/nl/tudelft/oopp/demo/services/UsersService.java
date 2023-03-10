package nl.tudelft.oopp.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.logger.FileLogger;
import nl.tudelft.oopp.demo.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UsersService {

    private UsersRepository usersRepo;

    /**
     * UsersService constructor to utilize dependency injection.
     * @param usersRepo The repository that will be user in this UsersService
     */
    @Autowired
    public UsersService(UsersRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    /**
     * Service to get all students.
     * @return Spring query to find all students
     */
    public List<Users> getAllStudents() {
        List<Users> allUsers = usersRepo.findAll();
        List<Users> allStudents = new LinkedList<>();
        for (Users user : allUsers) {
            if (user.getRole().equals("student")) {
                allStudents.add(user);
            }
        }
        return allStudents;
    }

    /**
     * Method for getting a user using a username and password.
     * @param username String containing the username of the person we want to find
     * @param password String containing the password of the person we want to find
     * @return String that is representing a student iff the combination valid
     * @throws JsonProcessingException Thrown when something goes wrong while JsonProcessing
     */
    public String authenticateLogin(String username, String password)
            throws JsonProcessingException {
        if (!usersRepo.existsByUsername(username)) {
            return "User doesn't exist";
        }
        if (!usersRepo.existsByUsernameAndPassword(username, password)) {
            return "Wrong password";
        }
        Users user = usersRepo.getByUsernameAndPassword(username, password);

        if (!user.isBanned()) {
            FileLogger.addMessage(username + " logged in");
            return new ObjectMapper().writeValueAsString(user);
        } else {
            return "This user is banned!";
        }
    }

    /**
     * This methods adds a user to the database.
     * @param user User containing the user that needs to be added
     * @return Success/Error message
     */
    public String addUser(Users user) {
        if (usersRepo.existsByUsername(user.getUsername())) {
            return "This user already exists!";
        }
        if (usersRepo.existsByEmail(user.getEmail())) {
            return "This email address is already used!";
        }
        usersRepo.save(user);
        FileLogger.addMessage("A user of type: " + user.getRole()
                + " with username: " + user.getUsername()
                + " has been added.");
        return "User type: " + user.getRole()
                + ", username: " + user.getUsername()
                + " has been added successfully";
    }

    /**
     * Ban user if the user exists and not already banned.
     * @param bannedUser bannedUser of the user to be banned.
     * @return resulting string.
     */
    public String banUser(String bannedUser, String username) {
        if (!usersRepo.existsByUsername(bannedUser)) {
            return "User doesn't exist";
        }
        Users user = usersRepo.getByUsername(bannedUser);

        if (user.isBanned()) {
            return "User is already banned";
        }

        user.setBanned(true);
        usersRepo.save(user);
        FileLogger.addMessage(username + " banned " + bannedUser);
        return "User banned successfully";
    }

    /**
     * Unban user if the user exists and not already banned.
     * @param unbannedUser unbannedUser of the user to be banned.
     * @return resulting string.
     */
    public String unbanUser(String unbannedUser, String username) {
        if (!usersRepo.existsByUsername(unbannedUser)) {
            return "User doesn't exist";
        }

        Users user = usersRepo.getByUsername(unbannedUser);

        if (!user.isBanned()) {
            return "This user has not been banned";
        }

        user.setBanned(false);
        usersRepo.save(user);
        FileLogger.addMessage(username + " unbanned " + unbannedUser);
        return "User unbanned successfully";
    }

    /**
     * Search students that match the view.
     * if view = false -> show not-banned users
     * if view = true -> show banned students
     * @param search Query string
     * @param view banned students or unbanned students
     * @return resulting list of students
     */
    public List<Users> searchStudents(String search, boolean view) {
        if (search == null || search.length() == 0) {
            return new ArrayList<>();
        }
        return usersRepo.findStudents(search, view);
    }

    public List<Users> getNotBannedStudents() {
        return usersRepo.getNotBannedStudents();
    }

    public List<Users> getBannedStudents() {
        return usersRepo.getBannedStudents();
    }

    public boolean isUserBanned(String username) {
        return usersRepo.isUserBanned(username);
    }
}
