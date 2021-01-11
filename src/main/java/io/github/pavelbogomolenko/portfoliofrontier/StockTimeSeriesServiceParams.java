package io.github.pavelbogomolenko.portfoliofrontier;

import java.time.LocalDate;

public class StockTimeSeriesServiceParams {
    private final String symbol;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    private StockTimeSeriesServiceParams(Builder builder) {
        this.symbol = builder.symbol;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
    }

    public static class Builder {
        private String symbol;
        private LocalDate dateFrom;
        private LocalDate dateTo;

        public Builder symbol(String s) {
            this.symbol = s;
            return this;
        }

        public Builder dateFrom(LocalDate d) {
            this.dateFrom = d;
            return this;
        }

        public Builder dateTo(LocalDate d) {
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

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }
}
