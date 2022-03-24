package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceRangeParam;

import java.time.LocalDate;
import java.util.ArrayList;

public class StockPriceHistoricalDatasetListParams {
    private final ArrayList<String> symbols;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final StockHistoricalPriceRangeParam range;

    private StockPriceHistoricalDatasetListParams(Builder builder) {
        this.symbols = builder.symbols;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
        this.range = builder.range;
    }

    public static class Builder {
        private ArrayList<String> symbols = new ArrayList<>();
        private LocalDate dateFrom;
        private LocalDate dateTo;
        private StockHistoricalPriceRangeParam range;

        public Builder symbol(String s) {
            this.symbols.add(s);
            return this;
        }

        public Builder symbols(ArrayList<String> s) {
            this.symbols = s;
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

        public StockPriceHistoricalDatasetListParams build() {
            return new StockPriceHistoricalDatasetListParams(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public ArrayList<String> getSymbols() {
        return symbols;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public StockHistoricalPriceRangeParam getRange() {
        return this.range;
    }
}
