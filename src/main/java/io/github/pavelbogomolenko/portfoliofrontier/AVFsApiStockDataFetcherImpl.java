package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AVFsApiStockDataFetcherImpl implements AVApiStockDataFetcher {

    private final static String FS_STORAGE_PATH = "data/";

    public AVFsApiStockDataFetcherImpl() {
    }

    @Override
    public String getMonthlyTimeSeries(String symbol) throws IOException {
        Path fileName = Path.of(FS_STORAGE_PATH + symbol + ".txt");
        return Files.readString(fileName);
    }
}
