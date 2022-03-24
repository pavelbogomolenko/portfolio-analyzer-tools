package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StockHistoricalPriceParamsUnitTest {

    @Test
    void shouldAlwaysConvertSymbolToUpperCase() {
        String givenSymbol = "aapl";
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        assertThat(params.getSymbol(), equalTo(givenSymbol.toUpperCase()));
    }
}
