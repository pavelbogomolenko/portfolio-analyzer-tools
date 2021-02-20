package io.github.pavelbogomolenko.stockhistoricalprice;

import java.util.ArrayList;

public class StockPriceTimeSeries {
    private final StockPriceMeta meta;
    private final ArrayList<StockPrice> prices;

    public StockPriceTimeSeries(StockPriceMeta meta, ArrayList<StockPrice> prices) {
        this.meta = meta;
        this.prices = prices;
    }

    public StockPriceMeta getMeta() {
        return meta;
    }

    public ArrayList<StockPrice> getPrices() {
        return prices;
    }
}
