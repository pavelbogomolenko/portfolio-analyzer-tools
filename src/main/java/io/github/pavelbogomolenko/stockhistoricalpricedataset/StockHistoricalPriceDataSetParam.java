package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.CommonStockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParamBuilder;

import java.util.List;


public class StockHistoricalPriceDataSetParam extends CommonStockHistoricalPriceParams {

    private String property;
    private List<String> symbols;

    private StockHistoricalPriceDataSetParam(Builder builder) {
        this.symbol = builder.getSymbol();
        this.dateFrom = builder.getDateFrom();
        this.dateTo = builder.getDateTo();
        this.range = builder.getRange();
        this.period = builder.getPeriod();
        this.property = builder.property;
        this.symbols = builder.symbols;
    }

    public static class Builder extends StockHistoricalPriceParamBuilder<StockHistoricalPriceDataSetParam, Builder> {
        private String property = "adjClose";

        private List<String> symbols;

        public Builder property(String s) {
            this.property = s;
            return this.self();
        }

        public Builder symbols(List<String> s) {
            this.symbols = s;
            return this.self();
        }

        @Override
        public StockHistoricalPriceDataSetParam build() {
            return new StockHistoricalPriceDataSetParam(this);
        }

        @Override
        public Builder self() {
            return this;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getProperty() {
        return this.property;
    }

    public List<String> getSymbols() {
        return this.symbols;
    }
}
