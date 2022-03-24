package io.github.pavelbogomolenko.stockhistoricalprice;

import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public class AVStockHistoricalPriceProviderService implements StockHistoricalPriceProviderService {
    private final IStockHistoricalApi dataSource;

    public AVStockHistoricalPriceProviderService(IStockHistoricalApi dataSource) {
        this.dataSource = dataSource;
    }

    public static class Builder {
        public AVStockHistoricalPriceProviderService buildWithFsDataSource() {
            IStockHistoricalApi fs = new AVStockHistoricalFsApi();
            return new AVStockHistoricalPriceProviderService(fs);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params) {
        Objects.requireNonNull(params.getSymbol());
        String rawData = this.dataSource.getRawMonthlyAdjPriceData(params.getSymbol());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockPriceTimeSeries.class, new AVStockHistoricalAdjPriceResponseDeserializer());
        StockPriceTimeSeries response = gsonBuilder.create().fromJson(rawData, StockPriceTimeSeries.class);

        return this.filterPrices(response, params);
    }

    @Override
    public StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params) {
        return null;
    }

    @Override
    public StockPriceTimeSeries getStockWeeklyHistoricalPrices(StockHistoricalPriceParams params) {
        throw new UnsupportedOperationException("getStockWeeklyHistoricalPrices is not implemented");
    }

    @Override
    public boolean isDateBetween(LocalDate date, LocalDate dateFrom, LocalDate dateTo) {
        YearMonth ymDate = YearMonth.of(date.getYear(), date.getMonth());
        return !(ymDate.isBefore(YearMonth.from(dateFrom)) || ymDate.isAfter(YearMonth.from(dateTo)));
    }
}
