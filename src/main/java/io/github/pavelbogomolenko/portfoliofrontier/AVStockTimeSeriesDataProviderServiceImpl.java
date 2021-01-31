package io.github.pavelbogomolenko.portfoliofrontier;

import com.google.gson.GsonBuilder;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Objects;

public class AVStockTimeSeriesDataProviderServiceImpl implements StockTimeSeriesDataProviderService {
    private final AVApiStockDataFetcher avApiStockDataFetcher;

    public AVStockTimeSeriesDataProviderServiceImpl(AVApiStockDataFetcher avApiStockDataFetcher) {
        this.avApiStockDataFetcher = avApiStockDataFetcher;
    }

    @Override
    public StockMonthlyTimeSeriesData getStockMonthlyTimeSeriesData(StockTimeSeriesServiceParams params) {
        Objects.requireNonNull(params.getSymbol());
        String rawData = this.avApiStockDataFetcher.getMonthlyTimeSeries(params.getSymbol());

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
