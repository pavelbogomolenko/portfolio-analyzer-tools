package io.github.pavelbogomolenko.stockhistoricalprice;

import org.mockito.ArgumentMatcher;

public class StockHistoricalPriceParamsMatcher implements ArgumentMatcher<StockHistoricalPriceParams> {

    private StockHistoricalPriceParams left;

    public StockHistoricalPriceParamsMatcher(StockHistoricalPriceParams left) {
        this.left = left;
    }

    @Override
    public boolean matches(StockHistoricalPriceParams right) {
        if(this.left.getSymbol() != null && this.left.getDateFrom() != null && this.left.getDateTo() != null) {
            return this.left.getSymbol().equals(right.getSymbol()) && this.left.getDateFrom().equals(right.getDateFrom()) &&
                    this.left.getDateTo().equals(right.getDateTo());
        }
        if(this.left.getSymbol() != null && this.left.getDateFrom() == null && this.left.getDateTo() == null) {
            return this.left.getSymbol().equals(right.getSymbol());
        }
        return false;
    }
}
