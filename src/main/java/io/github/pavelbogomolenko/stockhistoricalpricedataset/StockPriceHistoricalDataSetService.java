package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.timeseries.*;


public class StockPriceHistoricalDataSetService {
    private final StockHistoricalPriceDataProviderFactory stockPriceDataProviderFactory;

    public StockPriceHistoricalDataSetService(StockHistoricalPriceDataProviderFactory stockPriceDataProviderFactory) {
        this.stockPriceDataProviderFactory = stockPriceDataProviderFactory;
    }

    public DataSet getDataSet(StockHistoricalPriceParams params) {
        StockPriceTimeSeries tsData = this.stockPriceDataProviderFactory.getStockPriceTimeSeries(params);
        return ListToDataSet.convert(tsData.getPrices(), "date", "adjClose");
    }
}
