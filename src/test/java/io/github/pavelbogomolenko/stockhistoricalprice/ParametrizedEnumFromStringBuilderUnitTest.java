package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParametrizedEnumFromStringBuilderUnitTest {

    @ParameterizedTest
    @ValueSource(strings = {"1y", "5y", "10y", ""})
    void shouldBuildStockHistoricalPriceRangeParamFromStringInput(String rangeStr) {
        assertThat(StockHistoricalPriceRangeParam.fromString(rangeStr).toString(), is(rangeStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {"d", "w", "m"})
    void shouldBuildStockHistoricalPricePeriodParamFromStringInput(String period) {
        assertThat(StockHistoricalPricePeriodParam.fromString(period).toString(), is(period));
    }
}
