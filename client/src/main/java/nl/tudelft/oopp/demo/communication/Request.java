package nl.tudelft.oopp.demo.communication;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import nl.tudelft.oopp.demo.entities.LectureRoom;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.ScoringLog;
import nl.tudelft.oopp.demo.entities.SpeedLog;

public class Request {


    /**
     * Generic get method.
     * @param url String with the url we want to GET
     * @return String with the responseBody
     */
    public static String get(String url) {
        GenericType<String> responseBodyType = new GenericType<String>(){};

        String responseBody = ClientBuilder.newClient()
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
