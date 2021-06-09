package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StockHistoricalPriceParamsUnitTest {

    @ParameterizedTest
    @ValueSource(strings = {"ONE_YEAR", "FIVE_YEARS", "SEVEN_YEARS"})
    void whenGivenValidRangeShouldConvertItToDateFromAndDateTo(String rangeStr) {
        StockHistoricalPriceRangeParam range = StockHistoricalPriceRangeParam.valueOf(rangeStr);
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("qwerty")
                .range(range)
                .build();
        int intRange = Integer.parseInt(range.getRange().substring(0, 1));
        LocalDate expectedDateTo = LocalDate.now().minus(1, ChronoUnit.MONTHS);
        LocalDate expectedDateFrom = expectedDateTo.minus(12 * intRange, ChronoUnit.MONTHS);
        assertThat(params.getDateFrom(), is(YearMonth.from(expectedDateFrom)));
        assertThat(params.getDateTo(), is(YearMonth.from(expectedDateTo)));
    }

    @Test
    void whenGivenNoRangeShouldConvertItToDateFromAndDateTo() {
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("qwerty")
                .range(StockHistoricalPriceRangeParam.NO_RANGE)
                .build();
        assertThat(params.getDateFrom(), equalTo(null));
        assertThat(params.getDateTo(), equalTo(null));
    }

    @Test
    void shouldAlwaysConvertSymbolToUpperCase() {
        String givenSymbol = "aapl";
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        assertThat(params.getSymbol(), equalTo(givenSymbol.toUpperCase()));
    }
}
