package io.github.pavelbogomolenko.portfoliofrontier;

import java.time.LocalDate;
import java.util.ArrayList;

public class PortfolioReturnsPerformanceParams {
    private final ArrayList<String> symbols;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    private PortfolioReturnsPerformanceParams(Builder builder) {
        this.symbols = builder.symbols;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
    }

    public static class Builder {
        private ArrayList<String> symbols = new ArrayList<>();
        private LocalDate dateFrom;
        private LocalDate dateTo;

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

        public PortfolioReturnsPerformanceParams build() {
            return new PortfolioReturnsPerformanceParams(this);
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
}
