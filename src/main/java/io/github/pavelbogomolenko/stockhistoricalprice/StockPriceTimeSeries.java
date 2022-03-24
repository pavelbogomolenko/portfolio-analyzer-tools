package io.github.pavelbogomolenko.stockhistoricalprice;

import java.util.List;

public class StockPriceTimeSeries {
    private final StockPriceMeta meta;
    private final List<StockPrice> prices;

    public StockPriceTimeSeries(StockPriceMeta meta, List<StockPrice> prices) {
        this.meta = meta;
        this.prices = prices;
    }

    public StockPriceMeta getMeta() {
        return meta;
    }

    public List<StockPrice> getPrices() {
        return prices;
    }
}
