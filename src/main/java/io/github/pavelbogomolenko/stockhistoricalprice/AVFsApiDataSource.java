package io.github.pavelbogomolenko.stockhistoricalprice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AVFsApiDataSource implements AVApiDataSource {

    private final static String FS_STORAGE_PATH = "data/stockprice/monthly/";

    public AVFsApiDataSource() {
    }

    @Override
    public String getStockMonthlyHistoricalAdjPriceData(String symbol) {
        try {
            String pathToFile = FS_STORAGE_PATH + symbol + ".json";
            if(!System.getenv("DOCKER_RELATIVE_PATH_BEGIN").equals("")) {
                pathToFile = "/" + pathToFile;
            }
            Path fileName = Path.of(pathToFile);
            return Files.readString(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
