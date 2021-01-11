package io.github.pavelbogomolenko.portfoliofrontier;

import java.util.ArrayList;

public class AVStockMonthlyTimeSeriesResponse {
    private final StockMetaTimeSeries meta;
    private final ArrayList<StockPriceTimeSeries> prices;

    public AVStockMonthlyTimeSeriesResponse(StockMetaTimeSeries meta, ArrayList<StockPriceTimeSeries> prices) {
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
