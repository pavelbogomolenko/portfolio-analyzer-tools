package io.github.pavelbogomolenko.portfoliofrontier;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class AVStockTimeSeriesServiceImpl implements StockTimeSeriesService {
    private final AVStockDataFetcher avStockDataFetcher;

    public AVStockTimeSeriesServiceImpl() {
        this.avStockDataFetcher = new AVStockDataFetcher();
    }

    public AVStockTimeSeriesServiceImpl(AVStockDataFetcher avStockDataFetcher) {
        this.avStockDataFetcher = avStockDataFetcher;
    }

    @Override
    public StockMonthlyTimeSeriesData getStockMonthlyTimeSeriesData(StockTimeSeriesServiceParams params) throws InterruptedException, IOException, URISyntaxException {
        Objects.requireNonNull(params.getSymbol());
        String rawData = this.avStockDataFetcher.getMonthlyTimeSeries(params.getSymbol());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockMonthlyTimeSeriesData.class, new AVStockMonthlyTimeSeriesResponseDeserializer());
        StockMonthlyTimeSeriesData response = gsonBuilder.create().fromJson(rawData, StockMonthlyTimeSeriesData.class);

        if(Objects.isNull(params.getDateFrom()) && Objects.isNull(params.getDateTo())) {
            return response;
        }

        ArrayList<StockPriceTimeSeries> filteredPrices = new ArrayList<>();
        for (StockPriceTimeSeries price: response.getPrices()) {
            if(shouldFilterOutPrice(price, params)) {
                filteredPrices.add(price);
            }
        }

        return new StockMonthlyTimeSeriesData(response.getMeta(), filteredPrices);
    }

    private boolean shouldFilterOutPrice(StockPriceTimeSeries price, StockTimeSeriesServiceParams params) {
        if(price.getDate().getYear() >= params.getDateFrom().getYear() && price.getDate().getYear() <= params.getDateTo().getYear()) {
            if(price.getDate().getMonthValue() >= params.getDateFrom().getMonthValue() && price.getDate().getMonthValue() <= params.getDateTo().getMonthValue()) {
                return true;
            }
        }
        return false;
    }
}
