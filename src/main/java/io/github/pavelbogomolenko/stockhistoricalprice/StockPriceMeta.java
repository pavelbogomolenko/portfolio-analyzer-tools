package io.github.pavelbogomolenko.stockhistoricalprice;

public class StockPriceMeta {
    private final String info;
    private final String symbol;
    private final String timeZone;

    public StockPriceMeta(String info, String symbol, String timeZone) {
        this.info = info;
        this.symbol = symbol;
        this.timeZone = timeZone;
    }

    public String getInfo() {
        return info;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
