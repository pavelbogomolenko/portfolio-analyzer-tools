package io.github.pavelbogomolenko.stockhistoricalprice;


import io.github.pavelbogomolenko.simplehttprequestwrapper.Request;

import java.util.Objects;

public class AVStockHistoricalHttpApi implements IStockHistoricalApi {

    private final static String AV_BASE_URL = "https://www.alphavantage.co/";

    private final Request request;
    private final String avApiKey;

    public AVStockHistoricalHttpApi() {
        this.request = new Request();
        this.avApiKey = this.getAvApiKey();
    }

    public AVStockHistoricalHttpApi(Request request) {
        this.request = request;
        this.avApiKey = this.getAvApiKey();
    }

    private String getAvApiKey() {
        String apiKey = System.getenv("AV_API_KEY");
        Objects.requireNonNull(apiKey);
        return apiKey;
    }

    @Override
    public String getRawMonthlyAdjPriceData(String symbol) {
        String apiEndpointUrl = String.format("%squery?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=%s&apikey=%s",
                AV_BASE_URL, symbol, this.avApiKey);
        return this.request.get(apiEndpointUrl);
    }

    @Override
    public String getRawDailyAdjPriceData(String symbol) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRawWeeklyAdjPriceData(String symbol) {
        throw new UnsupportedOperationException();
    }
}
