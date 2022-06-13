package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import java.util.ArrayList;
import java.util.Arrays;

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
        int prevStockPriceCount = 0;
        int symbolCounter = 0;
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
            if(prevStockPriceCount > 0 && prevStockPriceCount != stockPricesSize) {
                if(prevStockPriceCount > stockPricesSize) {
                    for(int i = 0; i < symbolCounter; i++) {
                        DataSet prevDs = allClosePriceTSMeasures.get(i);
                        prevDs = prevDs.sliceFromHead(stockPricesSize);
                        allClosePriceTSMeasures.remove(i);
                        allClosePriceTSMeasures.add(i, prevDs);
                    }
                } else {
                    data = new StockPriceTimeSeries(data.getMeta(), data.getPrices().subList(0, prevStockPriceCount));
                }
            }
            prevStockPriceCount = stockPricesSize;
            DataSet closePriceDataSet = ListToDataSet.convert(data.getPrices(), "date", "adjClose");
            allClosePriceTSMeasures.add(symbolCounter, closePriceDataSet);
            symbolCounter++;
        }
        return allClosePriceTSMeasures;
    }
}
