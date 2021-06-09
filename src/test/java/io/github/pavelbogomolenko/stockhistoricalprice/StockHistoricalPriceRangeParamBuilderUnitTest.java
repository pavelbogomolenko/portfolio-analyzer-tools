package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StockHistoricalPriceRangeParamBuilderUnitTest {

    @ParameterizedTest
    @ValueSource(strings = {"1y", "5y", "10y", ""})
    void whenShouldBuildStockHistoricalPriceRangeParamFromStringInput(String rangeStr) {
        StockHistoricalPriceRangeParam param = StockHistoricalPriceRangeParamBuilder
                .fromStringRange(rangeStr)
                .build();
        assertThat(param.getRange(), is(rangeStr));
    }
}
