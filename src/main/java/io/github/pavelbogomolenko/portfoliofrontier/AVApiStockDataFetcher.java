package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;

public interface AVApiStockDataFetcher {
    String getMonthlyTimeSeries(String symbol) throws IOException, InterruptedException;
}
