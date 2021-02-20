package io.github.pavelbogomolenko.simplehttprequestwrapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {

    private final HttpClient httpClient;

    public Request() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public Request(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String get(String url) {
        try {
            HttpRequest getRequest = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = this.httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
