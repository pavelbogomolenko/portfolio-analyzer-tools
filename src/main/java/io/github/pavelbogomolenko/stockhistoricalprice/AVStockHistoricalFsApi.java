package io.github.pavelbogomolenko.stockhistoricalprice;

import io.github.pavelbogomolenko.utils.FSUtils;


public class AVStockHistoricalFsApi implements IStockHistoricalApi {

    private final static String FS_STORAGE_PATH = "data/stockprice/monthly/";

    public AVStockHistoricalFsApi() {
    }

    @Override
    public String getRawMonthlyAdjPriceData(String symbol) {
        String pathToFile = FS_STORAGE_PATH + symbol + ".json";
        return FSUtils.readFileContent(pathToFile);
    }

    @Override
    public String getRawDailyAdjPriceData(String symbol) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRawWeeklyAdjPriceData(String symbol) {
        throw new UnsupportedOperationException();
    }
}
