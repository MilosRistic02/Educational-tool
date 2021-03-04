package nl.tudelft.oopp.demo.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import nl.tudelft.oopp.demo.entities.Question;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

public class ServerCommunication extends Request {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a quote from the server.
     * @return the body of a get request to the server.
     * @throws Exception if communication with the server fails.
     */
    public static String saveQuestion(Question question) {
        return post("http://localhost:8080/question/save-question", question);
    }

    public static String sendCredentials(String username, String password) throws JsonProcessingException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        return post("http://localhost:8080/users/login", new ObjectMapper().writeValueAsString(credentials));
    }
}
