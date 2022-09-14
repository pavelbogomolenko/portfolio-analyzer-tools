package io.github.pavelbogomolenko.stockhistoricalprice;

import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EODStockHistoricalPriceProviderService implements StockHistoricalPriceProviderService {
    private final IStockHistoricalApi dataSource;

    public EODStockHistoricalPriceProviderService(IStockHistoricalApi dataSource) {
        this.dataSource = dataSource;
    }

    public static class Builder {
        public EODStockHistoricalPriceProviderService buildWithFsDataSource() {
            IStockHistoricalApi fs = new EODHistoricalPriceFsApi();
            return new EODStockHistoricalPriceProviderService(fs);
        }
    }

    public static EODStockHistoricalPriceProviderService.Builder newBuilder() {
        return new EODStockHistoricalPriceProviderService.Builder();
    }

    @Override
    public StockPriceTimeSeries getStockYearlyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawMonthlyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        List<StockPrice> eoyPrices = new ArrayList<>();
        StockPrice lastStockPrice = stockPriceTimeSeries.prices().get(0);
        int prevYear = lastStockPrice.getDate().getYear();
        eoyPrices.add(lastStockPrice);
        for(StockPrice stockPrice: stockPriceTimeSeries.prices()) {
            LocalDate d = stockPrice.getDate();
            if(prevYear != d.getYear()) {
                eoyPrices.add(stockPrice);
                prevYear = d.getYear();
            }
        }
        StockPriceTimeSeries yearlyStockPriceTimeSeries = new StockPriceTimeSeries(stockPriceTimeSeries.meta(), eoyPrices);
        return this.limitPricesByRange(yearlyStockPriceTimeSeries, params);
    }

    @Override
    public StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawMonthlyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        return this.limitPricesByRange(stockPriceTimeSeries, params);
    }

    @Override
    public StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawDailyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        return this.limitPricesByRange(stockPriceTimeSeries, params);
    }

    @Override
    public StockPriceTimeSeries getStockWeeklyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawWeeklyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        return this.limitPricesByRange(stockPriceTimeSeries, params);
    }

    private StockPriceTimeSeries jsonStringToStockPriceTimeSeries(String data, String symbol) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockPriceTimeSeries.class, new EODStockHistoricalPriceResponseDeserializer(symbol));
        return gsonBuilder.create().fromJson(data, StockPriceTimeSeries.class);
    }
}
