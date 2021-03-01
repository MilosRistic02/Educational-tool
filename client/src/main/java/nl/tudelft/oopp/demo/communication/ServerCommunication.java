package nl.tudelft.oopp.demo.communication;

import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;

import java.net.http.HttpClient;

public class ServerCommunication extends Request {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retrieves a quote from the server.
     * @return the body of a get request to the server.
     * @throws Exception if communication with the server fails.
     */
    public static String getQuote() {
        return get("http://localhost:8080/quote");
    }

}
