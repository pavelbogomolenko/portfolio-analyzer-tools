package io.github.pavelbogomolenko.stockhistoricalprice;

public interface AVApiDataSource {
    String getStockMonthlyHistoricalAdjPriceData(String symbol);
}
