package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.timeseries.*;

import java.util.ArrayList;
import java.util.List;


public class StockPriceHistoricalDataSetService {
    private final StockHistoricalPriceDataProviderFactory stockPriceDataProviderFactory;

    public StockPriceHistoricalDataSetService(StockHistoricalPriceDataProviderFactory stockPriceDataProviderFactory) {
        this.stockPriceDataProviderFactory = stockPriceDataProviderFactory;
    }

    public DataSet getDataSetForProperty(StockHistoricalPriceDataSetParam params) {
        StockPriceTimeSeries tsData = this.getStockPriceTimeSeries(params, "");
        return ListToDataSet.convert(tsData.prices(), "date", params.getProperty());
    }

    public List<DataSet> getMonthlyAdjustedDataSetListForProperty(StockHistoricalPriceDataSetParam param) {
        List<DataSet> allClosePriceTSMeasures = new ArrayList<>();
        int prevStockPriceCount = 0;
        int symbolCounter = 0;
        for(String symbol: param.getSymbols()) {
            StockPriceTimeSeries data = this.getStockPriceTimeSeries(param, symbol);
            int stockPricesSize = data.prices().size();
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
                    data = new StockPriceTimeSeries(data.meta(), data.prices().subList(0, prevStockPriceCount));
                }
            }
            prevStockPriceCount = stockPricesSize;
            DataSet closePriceDataSet = ListToDataSet.convert(data.prices(), "date", param.getProperty());
            allClosePriceTSMeasures.add(symbolCounter, closePriceDataSet);
            symbolCounter++;
        }
        return allClosePriceTSMeasures;
    }

    private StockPriceTimeSeries getStockPriceTimeSeries(StockHistoricalPriceDataSetParam params, String symbol) {
        StockHistoricalPriceParams stockPriceParams = StockHistoricalPriceParams.newBuilder()
                .symbol(symbol.equals("") ? params.getSymbol() : symbol)
                .dateFrom(params.getDateFrom())
                .dateTo(params.getDateTo())
                .period(params.getPeriod())
                .range(params.getRange())
                .build();
        return this.stockPriceDataProviderFactory.getStockPriceTimeSeries(stockPriceParams);
    }
}
