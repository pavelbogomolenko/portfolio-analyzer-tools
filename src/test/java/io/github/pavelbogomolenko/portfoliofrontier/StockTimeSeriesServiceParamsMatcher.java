package io.github.pavelbogomolenko.portfoliofrontier;

import org.mockito.ArgumentMatcher;

public class StockTimeSeriesServiceParamsMatcher implements ArgumentMatcher<StockTimeSeriesServiceParams> {

    private StockTimeSeriesServiceParams left;

    public StockTimeSeriesServiceParamsMatcher(StockTimeSeriesServiceParams left) {
        this.left = left;
    }

    @Override
    public boolean matches(StockTimeSeriesServiceParams right) {
        return this.left.getSymbol().equals(right.getSymbol()) && this.left.getDateFrom().equals(right.getDateFrom()) &&
                this.left.getDateTo().equals(right.getDateTo());
    }
}
