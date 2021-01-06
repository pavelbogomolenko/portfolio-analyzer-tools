package io.github.pavelbogomolenko.portfoliofrontier;

public class AVStockMonthlyTimeSeriesResponse {
    private final StockMetaTimeSeries meta;
    private final StockPriceTimeSeries[] prices;

    public AVStockMonthlyTimeSeriesResponse(StockMetaTimeSeries meta, StockPriceTimeSeries[] prices) {
        this.meta = meta;
        this.prices = prices;
    }

    public StockMetaTimeSeries getMeta() {
        return meta;
    }

    public StockPriceTimeSeries[] getPrices() {
        return prices;
    }
}
