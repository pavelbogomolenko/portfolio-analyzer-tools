package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import java.util.ArrayList;

import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.timeseries.ListToDataSet;
import io.github.pavelbogomolenko.timeseries.DataSet;


public class StockPriceHistoricalDataSetListService {
    private final StockHistoricalPriceProviderService stockHistoricalPriceProviderService;

    public StockPriceHistoricalDataSetListService(StockHistoricalPriceProviderService stockHistoricalPriceProviderService) {
        this.stockHistoricalPriceProviderService = stockHistoricalPriceProviderService;
    }

    public ArrayList<DataSet> getDataSetListForStocksMonthlyClosePrices(StockPriceHistoricalDatasetListParams stockPriceHistoricalDatasetListParams) {
        ArrayList<DataSet> allClosePriceTSMeasures = new ArrayList<>();
        int prevCount = 0;
        for(String symbol: stockPriceHistoricalDatasetListParams.getSymbols()) {
            StockHistoricalPriceParams.Builder serviceParamBuilder = StockHistoricalPriceParams.newBuilder()
                    .period(StockHistoricalPricePeriodParam.MONTHLY)
                    .symbol(symbol);
            if(stockPriceHistoricalDatasetListParams.getRange() == null) {
                serviceParamBuilder
                        .dateFrom(stockPriceHistoricalDatasetListParams.getDateFrom())
                        .dateTo(stockPriceHistoricalDatasetListParams.getDateTo());
            } else {
                serviceParamBuilder.range(stockPriceHistoricalDatasetListParams.getRange());
            }
            StockHistoricalPriceParams serviceParams = serviceParamBuilder.build();
            StockPriceTimeSeries data = this.stockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(serviceParams);
            int stockPricesSize = data.getPrices().size();
            if(stockPricesSize == 0) {
                throw new RuntimeException(String.format("No data found for stock '%s'", symbol));
            }
            if(prevCount > 0 && prevCount != stockPricesSize) {
                throw new RuntimeException(String.format("Stock data for '%s' less than given time range", symbol));
            }
            prevCount = stockPricesSize;
            DataSet closePriceDataSet = ListToDataSet.convert(data.getPrices(), "date", "adjClose");
            allClosePriceTSMeasures.add(closePriceDataSet);
        }
        return allClosePriceTSMeasures;
    }
}
