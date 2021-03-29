package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import nl.tudelft.oopp.demo.entities.Users;
import nl.tudelft.oopp.demo.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;





@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private UsersController usersController;

    @MockBean
    private UsersRepository usersRepository;

    Users testAdmin;
    Users testModerator;
    Users testLecturer;
    Users testStudent;
    Users testStudentBanned;
    String username;
    String usernameBanned;
    String password;
    String email;

    @BeforeEach
    void setup() {
        testStudent = new Users("stu", "stu@email.com", "password", "student");
        testLecturer = new Users("lec", "lec@email.com", "password", "lecturer");
        testModerator = new Users("mod", "mod@email.com", "password", "moderator");
        testAdmin = new Users("adm", "adm@email.com", "password", "admin");
        testStudentBanned = new Users("stuBanned", "stu@email.com", "password", "student");
        testStudentBanned.setBanned(true);
        username = "stu";
        usernameBanned = "stuBanned";
        password = "password";
        email = "stu@email.com";
    }

    @Test
    void getAllStudents() {
        Mockito.when(usersRepository.findAll())
                .thenReturn(Arrays.asList(testStudent, testModerator, testLecturer, testAdmin));
        assertEquals(usersController.getAllStudents(), Arrays.asList(testStudent));
    }

    @Test
    void authenticateLogin() throws Exception {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository
                .existsByUsernameAndPassword(username, password)).thenReturn(true);
        Mockito.when(usersRepository
                .getByUsernameAndPassword(username,password)).thenReturn(testStudent);
        assertEquals(usersController
                        .authenticateLogin("{\"password\":\"password\",\"username\":\"stu\"}"),
                "{\"username\":\"stu\",\"email\":\"stu@email.com\",\"password\":\"password\","
                        + "\"role\":\"student\",\"banned\":false}");
    }

    @Test
    void addUser() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(usersRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(usersRepository.save(testStudent)).thenReturn(testStudent);

        assertEquals(usersController.addUser(testStudent),
                "User type: student, username: stu has been added successfully");
    }

    @Test
    void banUser() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository.getByUsername(username)).thenReturn(testStudent);
        Mockito.when(usersRepository.save(testStudent)).thenReturn(testStudent);

        assertEquals(usersController.banUser(username), "User banned successfully");
    }

    @Test
    void unbanUser() {
        Mockito.when(usersRepository.existsByUsername(usernameBanned)).thenReturn(true);
        Mockito.when(usersRepository.getByUsername(usernameBanned)).thenReturn(testStudentBanned);
        Mockito.when(usersRepository.save(testStudentBanned)).thenReturn(testStudentBanned);

        assertEquals(usersController.unbanUser(usernameBanned), "User unbanned successfully");
    }

    @Test
    void searchStudentsNotBanned() {
        String query = "st";
        Mockito.when(usersRepository.findStudents(query,
                false)).thenReturn(Arrays.asList(testStudent));

        assertEquals(usersController.searchStudents(query,
                false), Arrays.asList(testStudent));
    }

    @Test
    void searchStudentsBanned() {
        String query = "uB";
        Mockito.when(usersRepository
                .findStudents(query, true)).thenReturn(Arrays.asList(testStudentBanned));

        assertEquals(usersController.searchStudents(query, true), Arrays.asList(testStudentBanned));
    }

    @Test
    void getNotBannedStudents() {
        Mockito.when(usersRepository.getNotBannedStudents()).thenReturn(Arrays.asList(testStudent));

        assertEquals(usersController.getNotBannedStudents(), Arrays.asList(testStudent));
    }

    @Test
    void getBannedStudents() {
        Mockito.when(usersRepository.getBannedStudents())
                .thenReturn(Arrays.asList(testStudentBanned));

        assertEquals(usersController.getBannedStudents(), Arrays.asList(testStudentBanned));
    }

    @Test
    void isUserBanned() {
        Mockito.when(usersRepository.isUserBanned(username)).thenReturn(false);
        assertEquals(usersController.isUserBanned(username), false);
    }
}