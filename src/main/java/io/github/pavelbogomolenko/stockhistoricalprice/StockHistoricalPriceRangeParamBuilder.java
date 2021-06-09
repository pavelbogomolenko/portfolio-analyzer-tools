package io.github.pavelbogomolenko.stockhistoricalprice;

import java.util.HashMap;

public class StockHistoricalPriceRangeParamBuilder {
    private StockHistoricalPriceRangeParamBuilder() {
    }

    public static class FromStringRangeBuilder {
        private final String stringRange;
        private final HashMap<String, StockHistoricalPriceRangeParam> stringToRangeMap = new HashMap<>();

        public FromStringRangeBuilder(String range) {
            this.stringRange = range;
            this.stringToRangeMap.put("1y", StockHistoricalPriceRangeParam.ONE_YEAR);
            this.stringToRangeMap.put("5y", StockHistoricalPriceRangeParam.FIVE_YEARS);
            this.stringToRangeMap.put("7y", StockHistoricalPriceRangeParam.SEVEN_YEARS);
            this.stringToRangeMap.put("10y", StockHistoricalPriceRangeParam.TEN_YEARS);
            this.stringToRangeMap.put("15y", StockHistoricalPriceRangeParam.FIFTEEN_YEARS);
        }

        public StockHistoricalPriceRangeParam build() {
            return this.stringToRangeMap.getOrDefault(this.stringRange, StockHistoricalPriceRangeParam.NO_RANGE);
        }
    }

    public static FromStringRangeBuilder fromStringRange(String range) {
        return new FromStringRangeBuilder(range);
    }
}
