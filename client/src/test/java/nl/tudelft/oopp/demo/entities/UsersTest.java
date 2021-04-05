package nl.tudelft.oopp.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class UsersTest {

    Users student;
    Users moderator;
    Users lecturer;
    Users student2;
    Users moderator2;
    Users lecturer2;


    /**
     * Test for the different User classes.
     */
    @BeforeEach
    public void setup() {
        student = new Users("user_std","user_std@gmail.com","pass123", "student");
        moderator = new Users("user_mod", "user_mod@gmail.com","pass123", "moderator");
        lecturer = new Users("user_lec", "user_lec@gmail.com", "pass123", "lecturer");
        student2 = new Users("user_std2","user_std2@gmail.com","pass123", "student2");
        moderator2 = new Users("user_mod2","user_mod2@gmail.com","pass123", "moderator2");
        lecturer2 = new Users("user_lec2", "user_lec2@gmail.com", "pass123", "lecturer2");
    }

    @Test
    void userEmptyConstructorTest() {
        assertNotNull(new Users());
    }

    @Test
    void studentConstructorTest() {
        assertNotNull(student);
    }

    @Test
    void moderatorConstructorTest() {
        assertNotNull(moderator);
    }

    @Test
    void lecturerConstructorTest() {
        assertNotNull(lecturer);
    }

    @Test
    void getUsernameStudentTest() {
        assertEquals(student.getUsername(), "user_std");
    }

    @Test
    void getUsernameModeratorTest() {
        assertEquals(moderator.getUsername(), "user_mod");
    }

    @Test
    void getUsernameLecturerTest() {
        assertEquals(lecturer.getUsername(), "user_lec");
    }

    @Test
    void isBannedTest() {
        assertEquals(student.isBanned(), false);
    }

    @Test
    void getEmailTest() {
        assertEquals(student.getEmail(), "user_std@gmail.com");
    }

    @Test
    void getPasswordTest() {
        assertEquals(student.getPassword(), "pass123");
    }

    @Test
    void getRoleStudentTest() {
        assertEquals(student.getRole(), "student");

    }

    @Test
    void getRoleModeratorTest() {
        assertEquals(moderator.getRole(), "moderator");
    }

    @Test
    void getRoleLecturerTest() {
        assertEquals(lecturer.getRole(), "lecturer");
    }

    @Test
    void setUsernameTest() {
        student.setUsername("newUsername");
        assertEquals(student.getUsername(), "newUsername");
    }

    @Test
    void setPasswordTest() {
        student.setPassword("newPassword123");
        assertEquals(student.getPassword(), "newPassword123");
    }

    @Test
    void setBannedTest() {
        student.setBanned(true);
        assertEquals(student.isBanned(), true);
    }

    @Test
    void setEmailTest() {
        student.setEmail("newEmail@gmail.com");
        assertEquals(student.getEmail(), "newEmail@gmail.com");
    }

    @Test
    void setRoleTest() {
        student.setRole("lecturer");
        assertEquals(student.getRole(), "lecturer");
    }

    @Test
    void equalsDifferentClassTest() {
        assertNotEquals(student, new ArrayList());
    }

    @Test
    void equalsStudentTestDifferentStudents() {
        assertNotEquals(student, student2);
    }

    @Test
    void equalsModeratorTest() {
        assertEquals(moderator, moderator);
    }

    @Test
    void equalsModeratorTestDifferentModerators() {
        assertNotEquals(moderator, moderator2);
    }

    @Test
    void equalsLecturerTest() {
        assertEquals(lecturer, lecturer);
    }

    @Test
    void equalsDifferentTypes_StudentAndLecturer() {
        student.setUsername("user_lec");
        assertNotEquals(lecturer, student);
    }

    @Test
    void equalsStudentTest() {
        assertEquals(student, student);
    }

    @Test
    void equalsDifferentTypes_StudentAndModerator() {
        student.setUsername("user_mod");
        assertNotEquals(student, moderator);
    }

    @Test
    void equalsDifferentTypes_LecturerAndModerator() {
        lecturer.setUsername("user_mod");
        assertNotEquals(lecturer, moderator);
    }

    @Test
    void hashingMethodTest() {
        assertEquals(student.hashCode(), -266142770);
    }



}
