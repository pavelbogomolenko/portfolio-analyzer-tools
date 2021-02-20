package io.github.pavelbogomolenko.stockhistoricalprice;


public interface StockHistoricalPriceProviderService {
    StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params);
    StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params);
}
