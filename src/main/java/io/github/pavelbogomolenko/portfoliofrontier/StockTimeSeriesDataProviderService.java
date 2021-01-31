package io.github.pavelbogomolenko.portfoliofrontier;


public interface StockTimeSeriesDataProviderService {
    StockMonthlyTimeSeriesData getStockMonthlyTimeSeriesData(StockTimeSeriesServiceParams params);
}
