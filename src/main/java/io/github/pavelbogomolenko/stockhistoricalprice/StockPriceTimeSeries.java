package io.github.pavelbogomolenko.stockhistoricalprice;

import java.util.List;

public record StockPriceTimeSeries(StockPriceMeta meta, List<StockPrice> prices) {

}
