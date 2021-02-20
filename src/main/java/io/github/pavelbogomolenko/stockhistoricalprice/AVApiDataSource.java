package io.github.pavelbogomolenko.stockhistoricalprice;

public interface AVApiDataSource {
    String getStockMonthlyHistoricalPriceData(String symbol);
}
