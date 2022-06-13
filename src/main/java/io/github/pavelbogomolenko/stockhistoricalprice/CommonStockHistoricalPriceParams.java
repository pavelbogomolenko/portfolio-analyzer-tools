package io.github.pavelbogomolenko.stockhistoricalprice;

import java.time.LocalDate;


public abstract class CommonStockHistoricalPriceParams {
    protected String symbol;
    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected StockHistoricalPriceRangeParam range = StockHistoricalPriceRangeParam.NO_RANGE;;
    protected StockHistoricalPricePeriodParam period;

    public String getSymbol() {
        return this.symbol;
    }

    public LocalDate getDateFrom() {
        return this.dateFrom;
    }

    public LocalDate getDateTo() {
        return this.dateTo;
    }

    public StockHistoricalPriceRangeParam getRange() {
        return this.range;
    }

    public StockHistoricalPricePeriodParam getPeriod() {
        return this.period;
    }
}
