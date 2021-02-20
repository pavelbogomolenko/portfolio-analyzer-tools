package io.github.pavelbogomolenko.stockhistoricalprice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AVFsApiDataSource implements AVApiDataSource {

    private final static String FS_STORAGE_PATH = "data/stockprice/monthly/";

    public AVFsApiDataSource() {
    }

    @Override
    public String getStockMonthlyHistoricalPriceData(String symbol) {
        try {
            Path fileName = Path.of(FS_STORAGE_PATH + symbol + ".json");
            return Files.readString(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
