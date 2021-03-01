package nl.tudelft.oopp.demo.communication;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {

    private static HttpClient client = HttpClient.newBuilder().build();

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

    public static <T> String post(String url, T t) {
        Entity<T> requestBody = Entity.entity(t, MediaType.APPLICATION_JSON);
        GenericType<String> responseBodyType = new GenericType<String>(){};

        String objects = ClientBuilder.newClient()
            .target(url)
            .request(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .post(requestBody, responseBodyType);

        return "Saved to database";
    }

}
