package io.github.pavelbogomolenko.portfoliofrontier;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class AVStockTimeSeriesService {
    private final AVStockDataFetcher avStockDataFetcher;

    public AVStockTimeSeriesService() {
        this.avStockDataFetcher = new AVStockDataFetcher();
    }

    public AVStockTimeSeriesService(AVStockDataFetcher avStockDataFetcher) {
        this.avStockDataFetcher = avStockDataFetcher;
    }

    public AVStockMonthlyTimeSeriesResponse getStockMonthlyTimeSeriesResponse(AVStockTimeSeriesServiceParams params) throws InterruptedException, IOException, URISyntaxException {
        Objects.requireNonNull(params.getSymbol());
        String rawData = this.avStockDataFetcher.getMonthlyTimeSeries(params.getSymbol());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AVStockMonthlyTimeSeriesResponse.class, new AVStockMonthlyTimeSeriesResponseDeserializer());
        return gsonBuilder.create().fromJson(rawData, AVStockMonthlyTimeSeriesResponse.class);
    }
}
