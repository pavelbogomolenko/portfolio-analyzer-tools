package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceProviderService;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPriceTimeSeries;
import io.github.pavelbogomolenko.timeseries.ListToDataSet;
import io.github.pavelbogomolenko.timeseries.DataSet;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class StockPriceHistoricalDataSetListService {
    private final StockHistoricalPriceProviderService stockHistoricalPriceProviderService;

    public StockPriceHistoricalDataSetListService(StockHistoricalPriceProviderService stockHistoricalPriceProviderService) {
        this.stockHistoricalPriceProviderService = stockHistoricalPriceProviderService;
    }

    public ArrayList<DataSet> getDataSetListForStocksMonthlyClosePrices(StockPriceHistoricalDatasetListParams stockPriceHistoricalDatasetListParams) {
        ArrayList<DataSet> allClosePriceTSMeasures = new ArrayList<>();
        for(String symbol: stockPriceHistoricalDatasetListParams.getSymbols()) {
            StockHistoricalPriceParams.Builder serviceParamBuilder = StockHistoricalPriceParams.newBuilder()
                    .symbol(symbol);
            if(stockPriceHistoricalDatasetListParams.getRange() == null) {
                serviceParamBuilder
                        .dateFrom(stockPriceHistoricalDatasetListParams.getDateFrom())
                        .dateTo(stockPriceHistoricalDatasetListParams.getDateTo());
            } else {
                serviceParamBuilder.range(stockPriceHistoricalDatasetListParams.getRange());
            }
            StockHistoricalPriceParams serviceParams = serviceParamBuilder.build();
            long expectedItemsCount = ChronoUnit.MONTHS.between(serviceParams.getDateFrom(), serviceParams.getDateTo()) + 1;
            StockPriceTimeSeries data = this.stockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(serviceParams);
            int stockPricesSize = data.getPrices().size();
            if(stockPricesSize != expectedItemsCount) {
                throw new IllegalArgumentException(String.format("Stock data for '%s' less than given time range", symbol));
            }
            DataSet closePriceDataSet = ListToDataSet.convert(data.getPrices(), "date", "adjClose");
            allClosePriceTSMeasures.add(closePriceDataSet);
        }
        return allClosePriceTSMeasures;
    }
}
