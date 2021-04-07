package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.SpeedLog;
import nl.tudelft.oopp.demo.entities.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;

public class ServerCommunicationTest {

    private static ClientAndServer mockServer;

    private Users users = new Users("p", "p@tudelft.nl", "pass", "pass");
    private Question question = new Question("How can I ask questions?", "0", "Thomas");
    private LectureRoom lectureRoom = new LectureRoom("3739232b", "Stefan", "CSE1300");
    private SpeedLog speedLog = new SpeedLog(users, lectureRoom, 95);
    private Poll poll1 = new Poll("3739232b", 4, Arrays.asList('A'), "poll");
    private Poll poll2 = new Poll("3739232b", 8, Arrays.asList('B', 'E'), "another poll");

    @BeforeEach
    void setUp() throws InterruptedException {
        mockServer = startClientAndServer(8080);
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
        // Sometimes the previous mockServer is not yet shutdown while
        // the next request is already posted.
        // While loop will make sure the server has quit.
        // found on: https://github.com/mock-server/mockserver/issues/498
        while (!mockServer.hasStopped(3,100L, TimeUnit.MILLISECONDS)) {

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

    @Test
    void updateAnswerQuestion() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                            .withMethod("PUT")
                            .withPath("/question/update-answer/user")
                            .withHeader("Content-type", "application/json")
                            .withBody(new ObjectMapper().writeValueAsString(question)))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Updated answer of Question"));

        assertEquals("Updated answer of Question",
                ServerCommunication.updateAnswerQuestion(question, "user"));
    }

    @Test
    void updateContentQuestion() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/question/update-content/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(question)))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Updated content of Question"));

        assertEquals("Updated content of Question",
                ServerCommunication.updateContentQuestion(question, "user"));
    }

    @Test
    void closeRoom() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/lecture/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(lectureRoom)))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Updated room"));

        assertEquals("Updated room",
                ServerCommunication.closeRoom(lectureRoom, "user"));
    }

    @Test
    void getClosedLecturePins() throws JsonProcessingException {
        LectureRoom lectureRoom1 = new LectureRoom("3739232b", "Stefan", "CSE1300");
        lectureRoom1.setOpen(false);

        List<LectureRoom> closedRooms = Arrays.asList(lectureRoom1);

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/lecture/getClosed/"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(closedRooms)));

        assertEquals(closedRooms,
                ServerCommunication.getClosedLecturePins());
    }

    @Test
    void exportRoom() throws JsonProcessingException {
        File testFile = new File("testFile");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("post")
                                .withPath("/lecture/file/0")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(testFile)))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Export successful"));

        assertEquals("Export successful",
                ServerCommunication.exportRoom(testFile, "0"));

        testFile.deleteOnExit();
    }

    @Test
    void speedVote() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/speedlog/speed-vote/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(speedLog))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Success"));

        assertEquals("Success", ServerCommunication.speedVote(speedLog, "user"));

    }

    @Test
    void speedGetVotes() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/speedlog/get-speed-votes/3739232b")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(40.0)));

        assertEquals(40.0, ServerCommunication.speedGetVotes("3739232b"));
    }

    @Test
    void createPoll() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/poll/create/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(poll1))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Success"));

        assertEquals("Success", ServerCommunication.createPoll(poll1, "user"));
    }

    @Test
    void getPoll() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/poll/3739232b")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(poll1)));

        assertEquals(poll1, ServerCommunication.getPoll("3739232b"));
    }

    @Test
    void getAllPolls() throws JsonProcessingException {
        List<Poll> polls = Arrays.asList(poll1, poll2);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/poll/lecture-polls/3739232b")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(polls)));

        assertEquals(polls, ServerCommunication.getAllPolls("3739232b"));
    }
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

    @Test
    void isUserBanned() {
        new MockServerClient("localhost", 8080)
            .when(
                    request()
                            .withMethod("GET")
                            .withPath("/users/check-banned/me")
            )
            .respond(
                    HttpResponse.response()
                            .withStatusCode(200)
                            .withBody("true")
            );
        assertEquals(true, ServerCommunication.isUserBanned("me"));
    }

    @Test
    void voteQuestion() throws JsonProcessingException {
        Question question = new Question("Is this a good example question?",
                "2802202001Stefan", "Stefan");
        Users users = new Users("Stefan", "stefan@tudelft.nl", "123", "student");
        ScoringLog scoringLog = new ScoringLog(question, users, -1);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/scoringlog/vote/stefan")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(scoringLog))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Success"));

        assertEquals("Success", ServerCommunication.voteQuestion(scoringLog, "stefan"));
    }

    @Test
    void getVotes() throws JsonProcessingException {
        Question question1 = new Question("Is this a good example question?",
                "2802202001Stefan", "Stefan");
        Question question2 = new Question("Is this a good example question too?",
                "2802202001Stefan", "Stefan");
        Question question3 = new Question("Is this a good example question three?",
                "2802202001Stefan", "Stefan");
        Users users = new Users("Stefan", "stefan@tudelft.nl", "123", "student");
        ScoringLog scoringLog1 = new ScoringLog(question1, users, -1);
        ScoringLog scoringLog2 = new ScoringLog(question2, users, 1);
        ScoringLog scoringLog3 = new ScoringLog(question3, users, 0);
        List<ScoringLog> list = Arrays.asList(scoringLog1, scoringLog2, scoringLog3);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/scoringlog/get-votes")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(users))
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));

        assertArrayEquals(list.toArray(), ServerCommunication.getVotes(users).toArray());
    }

    @Test
    void getAllAnsweredQuestions() throws JsonProcessingException {
        Question question1 = new Question("Is this a good example question?",
                "2802202001Stefan", "Stefan");
        Question question2 = new Question("Is this a good example question too?",
                "2802202001Stefan", "Stefan");
        Question question3 = new Question("Is this a good example question three?",
                "2802202001Stefan", "Stefan");
        question1.setAnswered(1);
        question2.setAnswered(1);
        question3.setAnswered(1);
        List<Question> list = Arrays.asList(question1, question2, question3);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/question/get-all/answered/2802202001Stefan")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));

        assertArrayEquals(list.toArray(),
                ServerCommunication.getAllAnsweredQuestions("2802202001Stefan").toArray());
    }

    @Test
    void getAllNonAnsweredQuestions() throws JsonProcessingException {
        Question question1 = new Question("Is this a good example question?",
                "2802202001Stefan", "Stefan");
        Question question2 = new Question("Is this a good example question too?",
                "2802202001Stefan", "Stefan");
        Question question3 = new Question("Is this a good example question three?",
                "2802202001Stefan", "Stefan");
        List<Question> list = Arrays.asList(question1, question2, question3);
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/question/get-all/non-answered/2802202001Stefan")
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(new ObjectMapper().writeValueAsString(list)));

        assertArrayEquals(list.toArray(),
                ServerCommunication.getAllNonAnsweredQuestions("2802202001Stefan").toArray());
    }

    @Test
    void closePollTest() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/poll/close/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(poll1)))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Poll closed"));

        assertEquals("Poll closed",
                ServerCommunication.closePoll(poll1, "user"));
    }

    @Test
    void voteTest() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/poll/vote/" + poll1.getId() + "/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString('c')))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Vote sent"));

        assertEquals("Vote sent",
                ServerCommunication.vote('c', poll1.getId(), "user"));
    }

    /*
     public static String updateFrequency(LectureRoom lectureRoom, String username) {
        return put("http://localhost:8080/lecture/update-frequency/" + username, lectureRoom);
    }
     */
    @Test
    void updateFrequencyTest() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("PUT")
                                .withPath("/lecture/update-frequency/user")
                                .withHeader("Content-type", "application/json")
                                .withBody(new ObjectMapper().writeValueAsString(lectureRoom)))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody("Frequency updated"));

        assertEquals("Frequency updated",
                ServerCommunication.updateFrequency(lectureRoom, "user"));
    }
}