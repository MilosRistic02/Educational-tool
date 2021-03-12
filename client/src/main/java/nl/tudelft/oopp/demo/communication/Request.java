package nl.tudelft.oopp.demo.communication;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import java.net.http.HttpClient;
import java.util.List;

import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;

public class Request {

    private static HttpClient client = HttpClient.newBuilder().build();

    /** Method to get questions.
     *
     * @param url   the url
     * @return      returns a list of questions
     */
    public static List<Question> get(String url) {
        GenericType<List<Question>> responseBodyType = new GenericType<List<Question>>(){};

        List<Question> responseBody = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(responseBodyType);

        return responseBody;
    }

    /** Method to get lectureroom with a pin.
     *
     * @param url   the url
     * @return      returns a lectureroom
     */
    public static LectureRoom getPin(String url) {
        GenericType<LectureRoom> responseBodyType = new GenericType<LectureRoom>(){};

        LectureRoom responseBody = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(responseBodyType);

        return responseBody;
    }

    /** Method to get all pins of closed lectures of a lecturer.
     *
     * @param url   the url
     * @return      returns a list with pins
     */
    public static List<LectureRoom> getClosedPins(String url) {
        GenericType<List<LectureRoom>> responseBodyType = new GenericType<List<LectureRoom>>(){};

        List<LectureRoom> responseBody = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(responseBodyType);

        return responseBody;
    }

    /** Method to get all scoring logs.
     *
     * @param url   the url
     * @return      returns a list of scoring logs
     */
    public static List<ScoringLog> getVotes(String url) {
        GenericType<List<ScoringLog>> responseBodyType = new GenericType<List<ScoringLog>>(){};

        List<ScoringLog> responseBody = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(responseBodyType);

        return responseBody;
    }

    /**
     * Handles POST requests from the client.
     * @param url that which the request is made
     * @param t Json version of the object handled
     * @return response body
     */
    public static <T> String post(String url, T t) {
        Entity<T> requestBody = Entity.entity(t, MediaType.APPLICATION_JSON);
        GenericType<String> responseBodyType = new GenericType<String>(){};

        String responseBody = ClientBuilder.newClient()
            .target(url)
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .post(requestBody, responseBodyType);

        return responseBody;
    }

    /**
     * Handles PUT requests from the client.
     * @param url that which the request is made
     * @param t Json version of the object handled
     * @return response body
     */
    public static <T> String put(String url, T t) {
        Entity<T> requestBody = Entity.entity(t, MediaType.APPLICATION_JSON);
        GenericType<String> responseBodyType = new GenericType<String>(){};

        String responseBody = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .put(requestBody, responseBodyType);

        return responseBody;
    }

    /**
     * Handles DELETE requests from the client.
     * @param url that which the request is made
     * @param id id of the object stored in the database
     * @return deleted object
     */
    public static String delete(String url, String id) {
        GenericType<String> responseBodyType = new GenericType<String>(){};
        String objects = ClientBuilder.newClient()
                .target(url + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .delete(responseBodyType);
        return objects;
    }

}
