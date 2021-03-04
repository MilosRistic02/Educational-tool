package nl.tudelft.oopp.demo.communication;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import nl.tudelft.oopp.demo.entities.Question;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Request {

    private static HttpClient client = HttpClient.newBuilder().build();

    public static <T> List<Question> get(String url) {
        GenericType<List<Question>> responseBodyType = new GenericType<List<Question>>(){};

         List<Question> responseBody = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(responseBodyType);

        return responseBody;
    }

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
