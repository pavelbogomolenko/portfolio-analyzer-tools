package io.github.pavelbogomolenko.simplehttprequestwrapper;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestUnitTest {

    @Test
    void test_get() throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse httpResponse = mock(HttpResponse.class);

        String expectedResponse = "123123";
        when(httpResponse.body()).thenReturn(expectedResponse);
        when(httpClient.send(any(), any())).thenReturn(httpResponse);

        String url = "http://google.com/";

        Request request = new Request(httpClient);
        String actualResponse = request.get(url);

        assertThat(actualResponse, is(expectedResponse));
    }
}
