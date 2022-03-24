package io.github.pavelbogomolenko.stockhistoricalprice;

import java.util.*;

public enum StockHistoricalPriceRangeParam {
    NO_RANGE(""),
    SEVEN_DAYS("7d"),
    FOURTEEN_DAYS("14d"),
    THIRTY_DAYS("30d"),
    NINETY_DAYS("90d"),
    HUNDRED_EIGHTY_DAYS("180d"),
    THREE_HUNDRED_SIXTY_DAYS("360d"),
    ONE_WEEK("1w"),
    TWO_WEEKS("2w"),
    FOUR_WEEKS("4w"),
    ONE_MONTH("1m"),
    THREE_MONTH("3m"),
    SIX_MONTH("6m"),
    TWELVE_MONTH("12m"),
    ONE_YEAR("1y"),
    TWO_YEARS("2y"),
    FIVE_YEARS("5y"),
    SEVEN_YEARS("7y"),
    TEN_YEARS("10y"),
    FIFTEEN_YEARS("15y");

    private final String range;
    private static final Map<String, StockHistoricalPriceRangeParam> ENUM_LABELS = new HashMap<>();

    static {
        for (StockHistoricalPriceRangeParam e: values()) {
            ENUM_LABELS.put(e.toString(), e);
        }
    }

    StockHistoricalPriceRangeParam(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return this.range;
    }

    public static StockHistoricalPriceRangeParam fromString(String label) {
        return ENUM_LABELS.get(label);
    }
}
