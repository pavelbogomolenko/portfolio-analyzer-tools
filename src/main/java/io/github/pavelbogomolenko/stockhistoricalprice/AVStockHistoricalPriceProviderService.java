package io.github.pavelbogomolenko.stockhistoricalprice;

import com.google.gson.GsonBuilder;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Objects;

public class AVStockHistoricalPriceProviderService implements StockHistoricalPriceProviderService {
    private final AVApiDataSource avApiDataSource;

    public AVStockHistoricalPriceProviderService(AVApiDataSource avApiDataSource) {
        this.avApiDataSource = avApiDataSource;
    }

    public static class Builder {
        public AVStockHistoricalPriceProviderService buildWithFsDataSource() {
            AVApiDataSource fs = new AVFsApiDataSource();
            return new AVStockHistoricalPriceProviderService(fs);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params) {
        Objects.requireNonNull(params.getSymbol());
        String rawData = this.avApiDataSource.getStockMonthlyHistoricalAdjPriceData(params.getSymbol());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockPriceTimeSeries.class, new AVStockHistoricalAdjPriceResponseDeserializer());
        StockPriceTimeSeries response = gsonBuilder.create().fromJson(rawData, StockPriceTimeSeries.class);

        if(Objects.isNull(params.getDateFrom()) && Objects.isNull(params.getDateTo())) {
            return response;
        }

        ArrayList<StockPrice> filteredPrices = new ArrayList<>();
        for (StockPrice price: response.getPrices()) {
            if(shouldFilterOutPrice(price, params)) {
                filteredPrices.add(price);
            }
        }

        return new StockPriceTimeSeries(response.getMeta(), filteredPrices);
    }

    @Override
    public StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params) {
        return null;
    }

    private boolean shouldFilterOutPrice(StockPrice price, StockHistoricalPriceParams params) {
        YearMonth d = YearMonth.of(price.getDate().getYear(), price.getDate().getMonth());
        if(isLocalDateBetween(d, params.getDateFrom(), params.getDateTo())) {
            return true;
        }
        return false;
    }

    private boolean isLocalDateBetween(YearMonth date, YearMonth from, YearMonth to) {
        return !(date.isBefore(from) || date.isAfter(to));
    }
}
