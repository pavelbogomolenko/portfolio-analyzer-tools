package io.github.pavelbogomolenko.portfoliofrontier;

import java.util.ArrayList;

public class StockMonthlyTimeSeriesData {
    private final StockMetaTimeSeries meta;
    private final ArrayList<StockPriceTimeSeries> prices;

    public StockMonthlyTimeSeriesData(StockMetaTimeSeries meta, ArrayList<StockPriceTimeSeries> prices) {
        this.meta = meta;
        this.prices = prices;
    }

    public StockMetaTimeSeries getMeta() {
        return meta;
    }

    public ArrayList<StockPriceTimeSeries> getPrices() {
        return prices;
    }
}
