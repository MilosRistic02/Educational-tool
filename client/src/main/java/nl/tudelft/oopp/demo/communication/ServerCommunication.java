package nl.tudelft.oopp.demo.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.oopp.demo.entities.*;


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

    public static List<Question> getAllAnsweredQuestions(String lecturePin) {
        return getQuestions("http://localhost:8080/question/get-all/answered/" + lecturePin);
    }

    public static List<Question> getAllNonAnsweredQuestions(String lecturePin) {
        return getQuestions("http://localhost:8080/question/get-all/non-answered/" + lecturePin);
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

    public static String speedVote(SpeedLog speedLog) {

        return post("http://localhost:8080/speedlog/speed-vote", speedLog);
    }

    public static List<SpeedLog> speedGetVotes() {
        return getSpeedVotes("http://localhost:8080/speedlog/get-speed-votes");
    }

    public static String createPoll(Poll poll) {
        return post("http://localhost:8080/poll/create/", poll);
    }

    public static Poll getPoll(String lecturePin) throws JsonProcessingException {
        String response = get("http://localhost:8080/poll/" + lecturePin);
        return response.length() == 0 ? null : new ObjectMapper().readValue(response, Poll.class);
    }

    public static String closePoll(Poll poll) {
        return put("http://localhost:8080/poll/close", poll);
    }

    public static String vote(Character c, long id) {
        return put("http://localhost:8080/poll/vote/" + id, c);
    }

    public static List<Users> getAllStudents() throws JsonProcessingException {
        String response = get("http://localhost:8080/users/student");
        return new ObjectMapper().readValue(response, new TypeReference<List<Users>>(){});
    }

    public static String banUser(String username) {
        return put("http://localhost:8080/users/ban", username);
    }

    public static List<Users> getBySubstring(String search, boolean view) throws JsonProcessingException {
        String response = get("http://localhost:8080/users/search/" + search +"/"+view);
        return new ObjectMapper().readValue(response, new TypeReference<>(){});
    }

    public static String unbanUser(String username) {
        return put("http://localhost:8080/users/unban", username);
    }

    public static List<Users> getAllNotBannedStudents() throws JsonProcessingException {
        String response = get("http://localhost:8080/users/not-banned");
        return new ObjectMapper().readValue(response, new TypeReference<>(){});
    }

    public static List<Users> getAllBannedStudents() throws JsonProcessingException {
        String response = get("http://localhost:8080/users/banned");
        return new ObjectMapper().readValue(response, new TypeReference<>(){});
    }

    public static boolean isUserBanned(String username){
        return Boolean.parseBoolean(get("http://localhost:8080/users/check-banned/"+username));
    }
}
