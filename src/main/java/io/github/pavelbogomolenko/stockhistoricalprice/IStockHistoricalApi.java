package io.github.pavelbogomolenko.stockhistoricalprice;

public interface IStockHistoricalApi {
    String getRawMonthlyAdjPriceData(String symbol);
    String getRawDailyAdjPriceData(String symbol);
    String getRawWeeklyAdjPriceData(String symbol);
}
