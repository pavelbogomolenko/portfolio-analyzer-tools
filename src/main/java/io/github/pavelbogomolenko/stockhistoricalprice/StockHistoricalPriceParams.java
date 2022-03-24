package io.github.pavelbogomolenko.stockhistoricalprice;

import java.time.LocalDate;


public class StockHistoricalPriceParams {
    private final String symbol;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final StockHistoricalPriceRangeParam range;
    private final StockHistoricalPricePeriodParam period;

    private StockHistoricalPriceParams(Builder builder) {
        this.symbol = builder.symbol;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
        this.range = builder.range;
        this.period = builder.period;
    }

    public static class Builder {
        private String symbol;
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private StockHistoricalPriceRangeParam range = StockHistoricalPriceRangeParam.NO_RANGE;
        private StockHistoricalPricePeriodParam period;

        public Builder symbol(String s) {
            this.symbol = s.toUpperCase();
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

        public Builder range(StockHistoricalPriceRangeParam range) {
            this.range = range;
            return this;
        }

        public Builder period(StockHistoricalPricePeriodParam period) {
            this.period = period;
            return this;
        }

        public StockHistoricalPriceParams build() {
            return new StockHistoricalPriceParams(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getSymbol() {
        return this.symbol;
    }

    public LocalDate getDateFrom() {
        return this.dateFrom;
    }

    public LocalDate getDateTo() {
        return this.dateTo;
    }

    public StockHistoricalPriceRangeParam getRange() {
        return this.range;
    }

    public StockHistoricalPricePeriodParam getPeriod() {
        return this.period;
    }
}
