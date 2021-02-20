package io.github.pavelbogomolenko.stockhistoricalprice;

import java.time.LocalDate;

public class StockPrice {
    private final LocalDate date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    private StockPrice(Builder builder) {
        this.date = builder.date;
        this.open = builder.open;
        this.high = builder.high;
        this.low = builder.low;
        this.close = builder.close;
        this.volume = builder.volume;
    }

    public static class Builder {
        private LocalDate date;
        private double open;
        private double high;
        private double low;
        private double close;
        private double volume;

        public Builder date(LocalDate d) {
            this.date = d;
            return this;
        }

        public Builder open(double p) {
            this.open = p;
            return this;
        }

        public Builder high(double p) {
            this.high = p;
            return this;
        }

        public Builder low(double p) {
            this.low = p;
            return this;
        }

        public Builder close(double p) {
            this.close = p;
            return this;
        }

        public Builder volume(double volume) {
            this.volume = volume;
            return this;
        }

        public StockPrice build() {
            return new StockPrice(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public LocalDate getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }
}
