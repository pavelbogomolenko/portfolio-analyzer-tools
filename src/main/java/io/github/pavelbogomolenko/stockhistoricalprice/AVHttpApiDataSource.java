package io.github.pavelbogomolenko.stockhistoricalprice;


import io.github.pavelbogomolenko.simplehttprequestwrapper.Request;

import java.util.Objects;

public class AVHttpApiDataSource implements AVApiDataSource {

    private final static String AV_BASE_URL = "https://www.alphavantage.co/";

    private final Request request;
    private final String avApiKey;

    public AVHttpApiDataSource() {
        this.request = new Request();
        this.avApiKey = this.getAvApiKey();
    }

    public AVHttpApiDataSource(Request request) {
        this.request = request;
        this.avApiKey = this.getAvApiKey();
    }

    private String getAvApiKey() {
        String apiKey = System.getenv("AV_API_KEY");
        Objects.requireNonNull(apiKey);
        return apiKey;
    }

    @Override
    public String getStockMonthlyHistoricalPriceData(String symbol) {
        String apiEndpointUrl = String.format("%squery?function=TIME_SERIES_MONTHLY&symbol=%s&apikey=%s",
                AV_BASE_URL, symbol, this.avApiKey);
        return this.request.get(apiEndpointUrl);
    }
}
