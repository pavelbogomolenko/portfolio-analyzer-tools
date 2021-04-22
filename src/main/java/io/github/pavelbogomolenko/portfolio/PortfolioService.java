package io.github.pavelbogomolenko.portfolio;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceProviderService;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPriceTimeSeries;
import io.github.pavelbogomolenko.timeseries.ListToDataSet;
import io.github.pavelbogomolenko.timeseries.DataSet;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PortfolioService {
    private final StockHistoricalPriceProviderService stockHistoricalPriceProviderService;

    public PortfolioService(StockHistoricalPriceProviderService stockHistoricalPriceProviderService) {
        this.stockHistoricalPriceProviderService = stockHistoricalPriceProviderService;
    }

    public ArrayList<DataSet> getDataSetListForStocksClosePrices(PortfolioParams params) {
        ArrayList<DataSet> allClosePriceTSMeasures = new ArrayList<>();
        long expectedItemsCount = ChronoUnit.MONTHS.between(params.getDateFrom(), params.getDateTo()) + 1;

        for(String symbol: params.getSymbols()) {
            StockHistoricalPriceParams serviceParams = StockHistoricalPriceParams.newBuilder()
                    .symbol(symbol)
                    .dateFrom(params.getDateFrom())
                    .dateTo(params.getDateTo())
                    .build();

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
