package io.github.pavelbogomolenko.portfoliofrontier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class AVHttpApiStockDataFetcherImpl implements AVApiStockDataFetcher {

    private final static String AV_BASE_URL = "https://www.alphavantage.co/";
    private static final Logger logger = LoggerFactory.getLogger(AVHttpApiStockDataFetcherImpl.class);

    private final HttpClient httpClient;
    private final String avApiKey;

    public AVHttpApiStockDataFetcherImpl() {
        this.httpClient = HttpClient.newHttpClient();
        String apiKey = System.getenv("AV_API_KEY");
        Objects.requireNonNull(apiKey);
        this.avApiKey = apiKey;
    }

    @Override
    public String getMonthlyTimeSeries(String symbol) throws IOException, InterruptedException, URISyntaxException {
        String apiEndpointUrl = String.format("%squery?function=TIME_SERIES_MONTHLY&symbol=%s&apikey=%s", AV_BASE_URL, symbol, this.avApiKey);
        HttpRequest httpGetRequest = HttpRequest.newBuilder(new URI(apiEndpointUrl))
                .GET()
                .build();

        HttpResponse response = this.httpClient.send(httpGetRequest, HttpResponse.BodyHandlers.ofString());
        return response.body().toString();
    }
}
