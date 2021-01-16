package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.net.URISyntaxException;

public interface StockTimeSeriesDataProviderService {
    StockMonthlyTimeSeriesData getStockMonthlyTimeSeriesData(StockTimeSeriesServiceParams params) throws InterruptedException, IOException, URISyntaxException;
}
