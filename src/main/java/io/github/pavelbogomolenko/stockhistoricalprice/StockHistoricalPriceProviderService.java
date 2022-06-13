package io.github.pavelbogomolenko.stockhistoricalprice;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface StockHistoricalPriceProviderService {
    Map<String, Double> ROUGH_TIME_PERIOD_UNIT_TO_UNIT_MULTIPLE = new HashMap<>() {{
        put("MY", 12.0);
        put("MM", 1.0);
        put("MD", (1.0 / 30.0));
        put("WY", 52.0);
        put("WM", 4.0);
        put("WW", 1.0);
        put("WD", (1.0 / 7.0));
        put("DY", 360.0);
        put("DM", 30.0);
        put("DW", 7.0);
        put("DD", 1.0);
        put("YY", 1.0);
    }};
    StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params);
    StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params);
    StockPriceTimeSeries getStockWeeklyHistoricalPrices(StockHistoricalPriceParams params);

    default StockPriceTimeSeries filterPrices(StockPriceTimeSeries priceTimeSeries, StockHistoricalPriceParams params) {
        if(priceTimeSeries.prices().size() == 0) {
            return priceTimeSeries;
        }

        ArrayList<StockPrice> filteredPrices = new ArrayList<>();
        if(params.getRange() != null && params.getRange() != StockHistoricalPriceRangeParam.NO_RANGE) {
            int limit = this.calculateRangeLimit(params.getRange(), params.getPeriod());
            int count = 0;
            for (StockPrice price: priceTimeSeries.prices()) {
                if(count++ < limit) {
                    filteredPrices.add(price);
                }
            }
        } else if(params.getDateFrom() != null && params.getDateTo() != null) {
            for (StockPrice price: priceTimeSeries.prices()) {
                if(isDateBetween(price.getDate(), params.getDateFrom(), params.getDateTo())) {
                    filteredPrices.add(price);
                }
            }
        } else {
            return priceTimeSeries;
        }

        return new StockPriceTimeSeries(priceTimeSeries.meta(), filteredPrices);
    }

    default int calculateRangeLimit(StockHistoricalPriceRangeParam range, StockHistoricalPricePeriodParam period) {
        String rangeStr = range.toString();
        int rangeValue = Integer.parseInt(rangeStr.substring(0, 1));
        String rangePeriodValue = range.toString().substring(1, 2);
        if(range.toString().length() > 2) {
            rangeValue = Integer.parseInt(range.toString().substring(0, rangeStr.length() - 1));
            rangePeriodValue = rangeStr.substring(rangeStr.length() - 1);
        }
        String timePeriodUnitToUnitKey = period.getPeriod().toUpperCase() + rangePeriodValue.toUpperCase();
        double timePeriodMultiple = ROUGH_TIME_PERIOD_UNIT_TO_UNIT_MULTIPLE.getOrDefault(timePeriodUnitToUnitKey, 0.0);
        return (int)(rangeValue * timePeriodMultiple);
    }

    default boolean isDateBetween(LocalDate date, LocalDate dateFrom, LocalDate dateTo) {
        return !(date.isBefore(dateFrom) || date.isAfter(dateTo));
    }
}
