package io.github.pavelbogomolenko.stockhistoricalprice;


import java.time.LocalDate;

public abstract class StockHistoricalPriceParamBuilder<A, B>
        extends CommonStockHistoricalPriceParams implements Buildable<A, B> {

    public B symbol(String s) {
        this.symbol = s.toUpperCase();
        return this.self();
    }

    public B dateFrom(LocalDate d) {
        this.dateFrom = d;
        return this.self();
    }

    public B dateTo(LocalDate d) {
        this.dateTo = d;
        return this.self();
    }

    public B range(StockHistoricalPriceRangeParam range) {
        this.range = range;
        return this.self();
    }

    public B period(StockHistoricalPricePeriodParam period) {
        this.period = period;
        return this.self();
    }
}
