package io.github.pavelbogomolenko.portfoliofrontier;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        AVStockMonthlyTimeSeriesResponse response = gsonBuilder.create().fromJson(rawData, AVStockMonthlyTimeSeriesResponse.class);

        ArrayList<StockPriceTimeSeries> filteredPrices = new ArrayList<>();
        for (StockPriceTimeSeries price: response.getPrices()) {
            if(shouldFilterOutPrice(price, params)) {
                filteredPrices.add(price);
            }
        }

        return new AVStockMonthlyTimeSeriesResponse(response.getMeta(), filteredPrices);
    }

    private boolean shouldFilterOutPrice(StockPriceTimeSeries price, AVStockTimeSeriesServiceParams params) {
        if(Objects.nonNull(params.getDateFrom()) && Objects.nonNull(params.getDateTo())) {
            if(price.getDate().getYear() >= params.getDateFrom().getYear() && price.getDate().getYear() <= params.getDateTo().getYear()) {
                if(price.getDate().getMonthValue() >= params.getDateFrom().getMonthValue() && price.getDate().getMonthValue() <= params.getDateTo().getMonthValue()) {
                    return true;
                }
            }
        } else {
           return true;
        }
        return false;
    }
}
