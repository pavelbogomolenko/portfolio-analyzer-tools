package io.github.pavelbogomolenko.portfolio;

import java.time.YearMonth;
import java.util.ArrayList;

public class PortfolioParams {
    private final ArrayList<String> symbols;
    private final YearMonth dateFrom;
    private final YearMonth dateTo;

    private PortfolioParams(Builder builder) {
        this.symbols = builder.symbols;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
    }

    public static class Builder {
        private ArrayList<String> symbols = new ArrayList<>();
        private YearMonth dateFrom;
        private YearMonth dateTo;

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

        public PortfolioParams build() {
            return new PortfolioParams(this);
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
}
