package io.github.pavelbogomolenko.stockhistoricalprice;

import io.github.pavelbogomolenko.utils.FSUtils;

public class EODHistoricalPriceFsApi implements IStockHistoricalApi {
    private final static String DAILY_HISTORICAL_PRICE_DATA_PATH = "data/stockprice/daily/";
    private final static String MONTHLY_HISTORICAL_PRICE_DATA_PATH = "data/stockprice/monthly/";
    private final static String WEEKLY_HISTORICAL_PRICE_DATA_PATH = "data/stockprice/weekly/";

    @Override
    public String getRawMonthlyAdjPriceData(String symbol) {
        String pathToFile = MONTHLY_HISTORICAL_PRICE_DATA_PATH + symbol + ".json";
        return FSUtils.readFileContent(pathToFile);
    }

    @Override
    public String getRawDailyAdjPriceData(String symbol) {
        String pathToFile = DAILY_HISTORICAL_PRICE_DATA_PATH + symbol + ".json";
        return FSUtils.readFileContent(pathToFile);
    }

    @Override
    public String getRawWeeklyAdjPriceData(String symbol) {
        String pathToFile = WEEKLY_HISTORICAL_PRICE_DATA_PATH + symbol + ".json";
        return FSUtils.readFileContent(pathToFile);
    }
}
