package io.github.pavelbogomolenko.stockhistoricalprice;


import java.util.*;

public enum StockHistoricalPricePeriodParam {
    DAILY("d"),
    WEEKLY("w"),
    MONTHLY("m");

    private static final Map<String, StockHistoricalPricePeriodParam> ENUM_LABELS = new HashMap<>();
    static {
        for (StockHistoricalPricePeriodParam e: values()) {
            ENUM_LABELS.put(e.toString(), e);
        }
    }

    private final String period;

    StockHistoricalPricePeriodParam(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return this.period;
    }

    @Override
    public String toString() {
        return this.period;
    }

    public static StockHistoricalPricePeriodParam fromString(String label) {
        return ENUM_LABELS.get(label);
    }
}
