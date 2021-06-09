package io.github.pavelbogomolenko.portfolio;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceRangeParam;

import java.time.YearMonth;
import java.util.ArrayList;

public class PortfolioHistoricalDatasetParams {
    private final ArrayList<String> symbols;
    private final YearMonth dateFrom;
    private final YearMonth dateTo;
    private final StockHistoricalPriceRangeParam range;

    private PortfolioHistoricalDatasetParams(Builder builder) {
        this.symbols = builder.symbols;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
        this.range = builder.range;
    }

    public static class Builder {
        private ArrayList<String> symbols = new ArrayList<>();
        private YearMonth dateFrom;
        private YearMonth dateTo;
        private StockHistoricalPriceRangeParam range;

        public Builder symbol(String s) {
            this.symbols.add(s);
            return this;
        }

        public Builder symbols(ArrayList<String> s) {
            this.symbols = s;
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

        public Builder range(StockHistoricalPriceRangeParam range) {
            this.range = range;
            return this;
        }

        public PortfolioHistoricalDatasetParams build() {
            return new PortfolioHistoricalDatasetParams(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public ArrayList<String> getSymbols() {
        return symbols;
    }

    public YearMonth getDateFrom() {
        return dateFrom;
    }

    public YearMonth getDateTo() {
        return dateTo;
    }

    public StockHistoricalPriceRangeParam getRange() {
        return this.range;
    }
}
