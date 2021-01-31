package io.github.pavelbogomolenko.portfoliofrontier;


public interface AVApiStockDataFetcher {
    String getMonthlyTimeSeries(String symbol);
}
