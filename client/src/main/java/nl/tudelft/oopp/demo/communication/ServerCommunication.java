package nl.tudelft.oopp.demo.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;



public class ServerCommunication extends Request {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a quote from the server.
     * @return the body of a get request to the server.
     * @throws Exception if communication with the server fails.
     */
    public static String saveQuestion(Question question) {
        return post(
                "http://localhost:8080/question/save-question",
                question);
    }

    public static LectureRoom getLectureRoom(String pin) {
        return getPin("http://localhost:8080/lecture/" + pin);
    }

    public static String addLectureRoom(LectureRoom lectureRoom) {
        return post("http://localhost:8080/lecture", lectureRoom);
    }

    public static List<Question> getAllQuestion(String lectureRoom) {
        return getQuestions("http://localhost:8080/question/get-all/" + lectureRoom);
    }


    /**
     * Sends the user information to the server via a post request.
     * @param username of the user
     * @param password of the user
     * @return response body of the post request
     * @throws JsonProcessingException when the json couldn't be processed
     */
    public static String sendCredentials(String username,
                                         String password) throws JsonProcessingException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        return post(
                "http://localhost:8080/users/login",
                new ObjectMapper().writeValueAsString(credentials));
    }

    /**
     * Sends the user information to the server via a post request.
     * @param username of the user
     * @param email    of the user
     * @param password of the user
     * @return response body of the post request
     * @throws JsonProcessingException when the json couldn't be processed
     */
    public static String sendCredentials(String username,
                                         String email,
                                         String password) throws JsonProcessingException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("email", email);
        credentials.put("password", password);
        credentials.put("role", "student");
        return post(
                "http://localhost:8080/users/register",
                new ObjectMapper().writeValueAsString(credentials));
    }

    public static String voteQuestion(ScoringLog scoringLog) {

        return post("http://localhost:8080/scoringlog/vote", scoringLog);
    }

    public static List<ScoringLog> getVotes() {
        return getVotes("http://localhost:8080/scoringlog/get-votes");
    }

    public static String deleteQuestion(String id) {
        return delete("http://localhost:8080/question", id);
    }

    public static String updateAnswerQuestion(Question question) {
        return put("http://localhost:8080/question/update-answer", question);
    }

    public static String updateContentQuestion(Question question) {
        return put("http://localhost:8080/question/update-content", question);
    }

    public static String closeRoom(LectureRoom lectureRoom) {
        return put("http://localhost:8080/lecture/", lectureRoom);
    }

    public static List<LectureRoom> getClosedLecturePins(String lecturerId) {
        return getClosedPins("http://localhost:8080/lecture/getClosed/" + lecturerId);
    }

    public static String createPoll(Poll poll){
        return post("http://localhost:8080/poll/create/", poll);
    }
    public static Poll getPoll(long id) throws JsonProcessingException {
        String response = get("http://localhost:8080/poll/" + id);
        return new ObjectMapper().readValue(response, Poll.class);
    }
}
