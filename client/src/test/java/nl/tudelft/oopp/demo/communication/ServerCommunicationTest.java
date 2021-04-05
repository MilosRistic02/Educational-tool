package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;

public class ServerCommunicationTest {

    private static ClientAndServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = startClientAndServer(8080);
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
        while (!mockServer.hasStopped(3,100L, TimeUnit.MILLISECONDS)){

        }
    }

    @Test
    void saveQuestion() throws JsonProcessingException {
        Question question = new Question("Is this a good example question?",
                "2802202001Stefan", "Stefan");
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/question/save-question/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(question))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Success"));

        assertEquals("Success", ServerCommunication.saveQuestion(question, "user"));

    }

    @Test
    void getLectureRoom() throws ParseException, JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        LectureRoom lectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
        lectureRoom.setLecturePin("123");
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/lecture/123")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(lectureRoom)));

        assertEquals(lectureRoom, ServerCommunication.getLectureRoom("123"));

    }

    @Test
    void addLectureRoom() throws ParseException, JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = "2021-04-01 12:34";
        LectureRoom lectureRoom = new LectureRoom("Stefan", "Reasoning and Logic",
                "CSE4200", format.parse(date));
        lectureRoom.setLecturePin("testing");
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/lecture/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(lectureRoom))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(lectureRoom.getLecturePin()));

        assertEquals("testing", ServerCommunication.addLectureRoom(lectureRoom, "user"));

    }

    @Test
    void getAllQuestion() throws JsonProcessingException {
        String questionString = "Is this a good example question?";
        String lecturePin = "2802202001Stefan";
        String author = "Stefan";
        Question question = new Question();
        Question question1 = new Question(questionString, lecturePin, author);
        Question question2 = new Question(questionString, lecturePin, author);
        List<Question> list = Arrays.asList(question, question1, question2);

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/question/get-all/123")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));

        assertArrayEquals(list.toArray(), ServerCommunication.getAllQuestion("123").toArray());

    }

    @Test
    void sendCredentials() throws JsonProcessingException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "user");
        credentials.put("password", "pass");
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/users/login")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(credentials))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("User logged in successfully"));

        assertEquals(ServerCommunication.sendCredentials("user", "pass"),
                "User logged in successfully");
    }

    @Test
    void testSendCredentials() throws JsonProcessingException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "user");
        credentials.put("email", "email");
        credentials.put("password", "password");
        credentials.put("role", "student");
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/users/register")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(credentials))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("User created successfully"));
        assertEquals("User created successfully",
                ServerCommunication.sendCredentials("user", "email", "password"));
    }


    @Test
    void deleteQuestion() {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("DELETE")
                                .withPath("/question/user/1")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Question deleted successfully"));
        assertEquals("Question deleted successfully",
                ServerCommunication.deleteQuestion("1", "user"));
    }

    //    @Test
    //    void updateAnswerQuestion() {
    //    }
    //
    //    @Test
    //    void updateContentQuestion() {
    //    }
    //
    //    @Test
    //    void closeRoom() {
    //    }
    //
    //    @Test
    //    void getClosedLecturePins() {
    //    }
    //
    //    @Test
    //    void exportRoom() {
    //    }
    //
    //    @Test
    //    void speedVote() {
    //    }
    //
    //    @Test
    //    void speedGetVotes() {
    //    }
    //
    //    @Test
    //    void createPoll() {
    //    }
    //
    //    @Test
    //    void getPoll() {
    //    }
    //
    //    @Test
    //    void getAllPolls() {
    //    }
    //
    //    @Test
    //    void closePoll() {
    //    }
    //
    //    @Test
    //    void vote() {
    //    }
    //
    //    @Test
    //    void updateFrequency() {
    //    }
    //
    //    @Test
    //    void getAllStudents() {
    //    }
    @Test
    void banUser() {
        String user = "user";
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/users/ban/user")
                                .withHeader("Content-type", "application.json")
                                .withBody(user)
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("User banned successfully"));
        assertEquals("User banned successfully",
                ServerCommunication.banUser("user", "user"));
    }

    @Test
    void getBySubstring() throws JsonProcessingException {
        Users users = new Users();
        users.setUsername("search");
        users.setPassword("pass");
        users.setRole("student");
        users.setEmail("student@mail.com");
        List<Users> list = new ArrayList<>();
        list.add(users);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/search/search/false")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));
        assertEquals(list,
                ServerCommunication.getBySubstring("search", false));
    }

    @Test
    void unbanUser() {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/users/unban/user")
                                .withHeader("Content-type", "application.json")
                                .withBody("user")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("User unbanned successfully"));
        assertEquals("User unbanned successfully",
                ServerCommunication.unbanUser("user", "user"));
    }

    @Test
    void getAllNotBannedStudents() throws JsonProcessingException {
        Users users = new Users();
        users.setUsername("user");
        users.setPassword("pass");
        users.setRole("student");
        users.setEmail("student@mail.com");
        users.setBanned(false);
        List list = new ArrayList<Users>();
        list.add(users);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/not-banned")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));
        assertEquals(list,
                ServerCommunication.getAllNotBannedStudents());
    }

    @Test
    void getAllBannedStudents() throws JsonProcessingException {
        Users users = new Users();
        users.setUsername("user");
        users.setPassword("pass");
        users.setRole("student");
        users.setEmail("student@mail.com");
        users.setBanned(true);
        List list = new ArrayList<Users>();
        list.add(users);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/banned")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));
        assertEquals(list,
                ServerCommunication.getAllBannedStudents());
    }

    //    @Test
    //    void isUserBanned() {
    //    }
    //
    //    @Test
    //    void voteQuestion() {
    //    }
    //
    //    @Test
    //    void getVotes() {
    //    }
    //    @Test
    //    void getAllAnsweredQuestions() {
    //    }
    //
    //    @Test
    //    void getAllNonAnsweredQuestions() {
    //    }
}