package io.github.pavelbogomolenko.stockhistoricalprice;


public class StockHistoricalPriceParams extends CommonStockHistoricalPriceParams {

    private StockHistoricalPriceParams(Builder builder) {
        this.symbol = builder.getSymbol();
        this.dateFrom = builder.getDateFrom();
        this.dateTo = builder.getDateTo();
        this.range = builder.getRange();
        this.period = builder.getPeriod();
    }

    public static class Builder extends StockHistoricalPriceParamBuilder<StockHistoricalPriceParams, Builder> {

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public StockHistoricalPriceParams build() {
            return new StockHistoricalPriceParams(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
