package io.github.pavelbogomolenko.stockhistoricalprice;

public enum StockHistoricalPriceRangeParam {
    NO_RANGE(""),
    ONE_YEAR("1y"),
    TWO_YEARS("2y"),
    FIVE_YEARS("5y"),
    SEVEN_YEARS("7y"),
    TEN_YEARS("10y"),
    FIFTEEN_YEARS("15y");

    private final String range;

    StockHistoricalPriceRangeParam(String range) {
        this.range = range;
    }

    public String getRange() {
        return this.range;
    }
}
