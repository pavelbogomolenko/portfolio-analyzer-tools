package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AVFsApiStockDataFetcherImpl implements AVApiStockDataFetcher {

    private final static String FS_STORAGE_PATH = "data/";

    public AVFsApiStockDataFetcherImpl() {
    }

    @Override
    public String getMonthlyTimeSeries(String symbol) {
        try {
            Path fileName = Path.of(FS_STORAGE_PATH + symbol + ".txt");
            return Files.readString(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
