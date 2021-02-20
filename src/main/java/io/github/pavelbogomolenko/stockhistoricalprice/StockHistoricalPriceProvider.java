package io.github.pavelbogomolenko.stockhistoricalprice;


public interface StockHistoricalPriceProvider {
    StockPriceTimeSeries getStockHistoricalPrices(StockHistoricalPriceProviderParams params);
}
