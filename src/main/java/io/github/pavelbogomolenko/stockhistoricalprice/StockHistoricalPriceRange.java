package io.github.pavelbogomolenko.stockhistoricalprice;

public enum StockHistoricalPriceRange {
    NO_RANGE(""),
    ONE_YEAR("1y"),
    TWO_YEARS("2y"),
    FIVE_YEARS("5y"),
    FIFTEEN_YEARS("15y");

    private final String range;

    StockHistoricalPriceRange(String range) {
        this.range = range;
    }

    public String getRange() {
        return this.range;
    }
}
