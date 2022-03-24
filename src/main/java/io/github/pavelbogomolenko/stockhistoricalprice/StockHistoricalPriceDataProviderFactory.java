package io.github.pavelbogomolenko.stockhistoricalprice;


import java.util.HashMap;
import java.util.function.Function;

public class StockHistoricalPriceDataProviderFactory {
    private final HashMap<StockHistoricalPricePeriodParam, Function<StockHistoricalPriceParams, StockPriceTimeSeries>> periodFn;

    public StockHistoricalPriceDataProviderFactory(StockHistoricalPriceProviderService priceProviderService) {
        this.periodFn = new HashMap<>() {{
           put(StockHistoricalPricePeriodParam.DAILY, priceProviderService::getStockDailyHistoricalPrices);
           put(StockHistoricalPricePeriodParam.WEEKLY, priceProviderService::getStockWeeklyHistoricalPrices);
           put(StockHistoricalPricePeriodParam.MONTHLY, priceProviderService::getStockMonthlyHistoricalPrices);
        }};
    }

    public StockPriceTimeSeries getStockPriceTimeSeries(StockHistoricalPriceParams params) {
        if(!this.periodFn.containsKey(params.getPeriod())) {
            throw new IllegalArgumentException("Not supported period");
        }
        return this.periodFn.get(params.getPeriod()).apply(params);
    }
}
