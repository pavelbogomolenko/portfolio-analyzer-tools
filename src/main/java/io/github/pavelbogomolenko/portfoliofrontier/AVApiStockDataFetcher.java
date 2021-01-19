package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AVApiStockDataFetcher {
    String getMonthlyTimeSeries(String symbol) throws IOException, InterruptedException, URISyntaxException;
}
