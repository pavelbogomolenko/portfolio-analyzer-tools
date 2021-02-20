package io.github.pavelbogomolenko.stockhistoricalprice;

public class StockHistoricalPriceProviderParams {
    private final String symbol;

    private StockHistoricalPriceProviderParams(Builder builder) {
        this.symbol = builder.symbol;
    }

    public static class Builder {
        private String symbol;

        public Builder symbol(String s) {
            this.symbol = s;
            return this;
        }

        public StockHistoricalPriceProviderParams build() {
            return new StockHistoricalPriceProviderParams(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getSymbol() {
        return symbol;
    }
}
