package io.github.pavelbogomolenko.portfolio;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceProviderService;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPriceTimeSeries;
import io.github.pavelbogomolenko.timeseries.ListToDataSet;
import io.github.pavelbogomolenko.timeseries.DataSet;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PortfolioHistoricalDataSetService {
    private final StockHistoricalPriceProviderService stockHistoricalPriceProviderService;

    public PortfolioHistoricalDataSetService(StockHistoricalPriceProviderService stockHistoricalPriceProviderService) {
        this.stockHistoricalPriceProviderService = stockHistoricalPriceProviderService;
    }

    public ArrayList<DataSet> getDataSetListForStocksMonthlyClosePrices(PortfolioHistoricalDatasetParams portfolioHistoricalDatasetParams) {
        ArrayList<DataSet> allClosePriceTSMeasures = new ArrayList<>();
        for(String symbol: portfolioHistoricalDatasetParams.getSymbols()) {
            StockHistoricalPriceParams.Builder serviceParamBuilder = StockHistoricalPriceParams.newBuilder()
                    .symbol(symbol);
            if(portfolioHistoricalDatasetParams.getRange() == null) {
                serviceParamBuilder
                        .dateFrom(portfolioHistoricalDatasetParams.getDateFrom())
                        .dateTo(portfolioHistoricalDatasetParams.getDateTo());
            } else {
                serviceParamBuilder.range(portfolioHistoricalDatasetParams.getRange());
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
