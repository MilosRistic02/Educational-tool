package nl.tudelft.oopp.demo.communication;

import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import nl.tudelft.oopp.demo.entities.Question;

import java.net.http.HttpClient;
import java.util.List;

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

    public static List<Question> getAllQuestion(){
        return get("http://localhost:8080/question/get-all");
    }

}
