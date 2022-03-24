package io.github.pavelbogomolenko.stockhistoricalprice;

import com.google.gson.GsonBuilder;


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
    public StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawMonthlyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        return this.filterPrices(stockPriceTimeSeries, params);
    }

    @Override
    public StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawDailyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        return this.filterPrices(stockPriceTimeSeries, params);
    }

    @Override
    public StockPriceTimeSeries getStockWeeklyHistoricalPrices(StockHistoricalPriceParams params) {
        String rawData = this.dataSource.getRawWeeklyAdjPriceData(params.getSymbol());
        StockPriceTimeSeries stockPriceTimeSeries = this.jsonStringToStockPriceTimeSeries(rawData, params.getSymbol());
        return this.filterPrices(stockPriceTimeSeries, params);
    }

    private StockPriceTimeSeries jsonStringToStockPriceTimeSeries(String data, String symbol) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockPriceTimeSeries.class, new EODStockHistoricalPriceResponseDeserializer(symbol));
        return gsonBuilder.create().fromJson(data, StockPriceTimeSeries.class);
    }
}
