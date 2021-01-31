package io.github.pavelbogomolenko.portfoliofrontier;

import java.time.YearMonth;

public class StockTimeSeriesServiceParams {
    private final String symbol;
    private final YearMonth dateFrom;
    private final YearMonth dateTo;

    private StockTimeSeriesServiceParams(Builder builder) {
        this.symbol = builder.symbol;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
    }

    public static class Builder {
        private String symbol;
        private YearMonth dateFrom;
        private YearMonth dateTo;

        public Builder symbol(String s) {
            this.symbol = s;
            return this;
        }

        public Builder dateFrom(YearMonth d) {
            this.dateFrom = d;
            return this;
        }

        public Builder dateTo(YearMonth d) {
            this.dateTo = d;
            return this;
        }

        public StockTimeSeriesServiceParams build() {
            return new StockTimeSeriesServiceParams(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getSymbol() {
        return symbol;
    }

    public YearMonth getDateFrom() {
        return dateFrom;
    }

    public YearMonth getDateTo() {
        return dateTo;
    }
}
