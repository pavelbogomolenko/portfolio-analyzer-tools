package io.github.pavelbogomolenko.portfoliofrontier;

import org.mockito.ArgumentMatcher;

public class PortfolioReturnsPerformanceParamsMatcher implements ArgumentMatcher<PortfolioReturnsPerformanceParams> {

    private PortfolioReturnsPerformanceParams left;

    public PortfolioReturnsPerformanceParamsMatcher(PortfolioReturnsPerformanceParams left) {
        this.left = left;
    }

    @Override
    public boolean matches(PortfolioReturnsPerformanceParams right) {
        return this.left.getSymbols().equals(right.getSymbols()) && this.left.getDateFrom().equals(right.getDateFrom()) &&
                this.left.getDateTo().equals(right.getDateTo());
    }
}
