package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceProviderService;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPriceTimeSeries;
import io.github.pavelbogomolenko.timeseries.DataSet;
import io.github.pavelbogomolenko.timeseries.ListToDataSet;


public class StockPriceHistoricalDataSetService {
    private final StockHistoricalPriceProviderService stockHistoricalPriceProviderService;

    public StockPriceHistoricalDataSetService(StockHistoricalPriceProviderService stockHistoricalPriceProviderService) {
        this.stockHistoricalPriceProviderService = stockHistoricalPriceProviderService;
    }

    public DataSet getDataSetForStockMonthlyClosePrice(StockHistoricalPriceParams params) {
        StockPriceTimeSeries tsData = this.stockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(params);
        return ListToDataSet.convert(tsData.getPrices(), "date", "adjClose");
    }
}
