package nl.tudelft.oopp.demo.communication;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import nl.tudelft.oopp.demo.entities.LectureRoom;

public class Request {

    private static HttpClient client = HttpClient.newBuilder().build();


    /**
     * Handles GET requests from the client.
     *
     * @param url that which the request is made
     * @return the string response body
     */
    public static String get(String url) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Communication with server failed";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
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
