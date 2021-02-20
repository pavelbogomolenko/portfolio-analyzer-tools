package io.github.pavelbogomolenko.stockhistoricalprice;

import java.time.YearMonth;

public class StockHistoricalPriceParams {
    private final String symbol;
    private final YearMonth dateFrom;
    private final YearMonth dateTo;
    private final StockHistoricalPriceRange range;

    private StockHistoricalPriceParams(Builder builder) {
        this.symbol = builder.symbol;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
        this.range = builder.range;
    }

    public static class Builder {
        private String symbol;
        private YearMonth dateFrom;
        private YearMonth dateTo;
        private StockHistoricalPriceRange range = StockHistoricalPriceRange.NO_RANGE;

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

        public Builder range(StockHistoricalPriceRange range) {
            this.range = range;
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

    public YearMonth getDateFrom() {
        return this.dateFrom;
    }

    public YearMonth getDateTo() {
        return this.dateTo;
    }

    public String getRange() {
        return this.range.getRange();
    }
}
