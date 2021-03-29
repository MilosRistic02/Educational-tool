package nl.tudelft.oopp.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
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
class UsersServiceTest {

    @Autowired
    private UsersService usersService;

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
        testStudentBanned =
                new Users("stuBanned", "stu@email.com", "password", "student");
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
        assertEquals(usersService.getAllStudents(), Arrays.asList(testStudent));
    }

    @Test
    void authenticateLogin() throws JsonProcessingException {
        Mockito.when(usersRepository
                .existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository
                .existsByUsernameAndPassword(username, password))
                .thenReturn(true);
        Mockito.when(usersRepository
                .getByUsernameAndPassword(username,password))
                .thenReturn(testStudent);
        assertEquals(usersService
                        .authenticateLogin(username,password),
                "{\"username\":\"stu\",\"email\":\"stu@email.com\","
                        + "\"password\":\"password\","
                        + "\"role\":\"student\",\"banned\":false}");
    }

    @Test
    void authenticateLoginUserDoesntExist() throws JsonProcessingException {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(false);
        assertEquals(usersService.authenticateLogin(username,password),
                "User doesn't exist");
    }

    @Test
    void authenticateLoginWrongPassword() throws JsonProcessingException {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository
                .existsByUsernameAndPassword(username, password)).thenReturn(false);
        assertEquals(usersService.authenticateLogin(username,password),
                "Wrong password");
    }

    @Test
    void authenticateLoginUserBanned() throws JsonProcessingException {
        Mockito.when(usersRepository.existsByUsername(usernameBanned)).thenReturn(true);
        Mockito.when(usersRepository
                .existsByUsernameAndPassword(usernameBanned, password)).thenReturn(true);
        Mockito.when(usersRepository
                .getByUsernameAndPassword(usernameBanned,password)).thenReturn(testStudentBanned);
        assertEquals(usersService.authenticateLogin(usernameBanned,password),
                "This user is banned!");
    }
    /*
    @Test
    void authenticateLoginException() throws JsonProcessingException {
        Mockito.when(usersRepository
            .existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository
            .existsByUsernameAndPassword(username, password)).thenReturn(true);
        Mockito.when(usersRepository
            .getByUsernameAndPassword(username,password)).thenReturn(testStudent);
        Exception exception = assertThrows(JsonProcessingException.class, () -> {
            new ObjectMapper().writeValueAsString();
        });

        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    */

    @Test
    void addUser() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(usersRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(usersRepository.save(testStudent)).thenReturn(testStudent);

        assertEquals(usersService.addUser(testStudent),
                "User type: student, username: stu has been added successfully");
    }

    @Test
    void addUserUsernameAlreadyExists() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(true);
        assertEquals(usersService.addUser(testStudent), "This user already exists!");
    }

    @Test
    void addUserEmailAlreadyExists() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(usersRepository.existsByEmail(email)).thenReturn(true);
        assertEquals(usersService.addUser(testStudent), "This email address is already used!");
    }

    @Test
    void banUser() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository.getByUsername(username)).thenReturn(testStudent);
        Mockito.when(usersRepository.save(testStudent)).thenReturn(testStudent);

        assertEquals(usersService.banUser(username), "User banned successfully");
    }

    @Test
    void banUserUserDoesntExist() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(false);
        assertEquals(usersService.banUser(username), "User doesn't exist");
    }

    @Test
    void banUserAlreadyBanned() {
        Mockito.when(usersRepository.existsByUsername(usernameBanned)).thenReturn(true);
        Mockito.when(usersRepository.getByUsername(usernameBanned)).thenReturn(testStudentBanned);
        Mockito.when(usersRepository.save(testStudentBanned)).thenReturn(testStudentBanned);

        assertEquals(usersService.banUser(usernameBanned), "User is already banned");
    }

    @Test
    void unbanUser() {
        Mockito.when(usersRepository.existsByUsername(usernameBanned)).thenReturn(true);
        Mockito.when(usersRepository.getByUsername(usernameBanned)).thenReturn(testStudentBanned);
        Mockito.when(usersRepository.save(testStudentBanned)).thenReturn(testStudentBanned);

        assertEquals(usersService.unbanUser(usernameBanned), "User unbanned successfully");
    }

    @Test
    void unbanUserDoesntExist() {
        Mockito.when(usersRepository.existsByUsername(usernameBanned)).thenReturn(false);
        assertEquals(usersService.unbanUser(usernameBanned), "User doesn't exist");
    }

    @Test
    void unbanUserNotBannedAlready() {
        Mockito.when(usersRepository.existsByUsername(username)).thenReturn(true);
        Mockito.when(usersRepository.getByUsername(username)).thenReturn(testStudent);
        Mockito.when(usersRepository.save(testStudent)).thenReturn(testStudent);

        assertEquals(usersService.unbanUser(username), "This user has not been banned");
    }

    @Test
    void searchStudentsNotBannedStudentsNullQuery() {
        String query = null;
        assertEquals(usersService.searchStudents(query, false), new ArrayList<>());

    }

    @Test
    void searchStudentsNotBannedStudentsEmptyQuery() {
        String query = "";
        assertEquals(usersService.searchStudents(query, false), new ArrayList<>());

    }

    @Test
    void searchStudentsNotBannedStudents() {
        String query = "st";
        Mockito.when(usersRepository.findStudents(query, false))
                .thenReturn(Arrays.asList(testStudent));

        assertEquals(usersService.searchStudents(query, false),
                Arrays.asList(testStudent));

    }

    @Test
    void searchStudentsBannedStudents() {
        String query = "uB";
        Mockito.when(usersRepository.findStudents(query, true))
                .thenReturn(Arrays.asList(testStudentBanned));

        assertEquals(usersService.searchStudents(query, true),
                Arrays.asList(testStudentBanned));

    }

    @Test
    void getNotBannedStudents() {
        Mockito.when(usersRepository.getNotBannedStudents())
                .thenReturn(Arrays.asList(testStudent));

        assertEquals(usersService.getNotBannedStudents(),
                Arrays.asList(testStudent));
    }

    @Test
    void getBannedStudents() {
        Mockito.when(usersRepository.getBannedStudents())
                .thenReturn(Arrays.asList(testStudentBanned));

        assertEquals(usersService.getBannedStudents(),
                Arrays.asList(testStudentBanned));
    }

    @Test
    void isUserBanned() {
        Mockito.when(usersRepository.isUserBanned(username)).thenReturn(false);
        assertEquals(usersService.isUserBanned(username), false);
    }
}