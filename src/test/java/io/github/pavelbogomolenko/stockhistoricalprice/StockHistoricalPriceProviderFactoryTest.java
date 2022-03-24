package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

public class StockHistoricalPriceProviderFactoryTest {

    @Test
    void shouldCallGetStockDailyHistoricalPricesIfDailyPeriodProvided() {
        StockPriceMeta meta = new StockPriceMeta("", "s", "");
        StockPrice price = StockPrice.newBuilder()
                .close(123.0)
                .volume(10000000.0)
                .build();
        List<StockPrice> prices = new ArrayList<>(List.of(price));
        StockPriceTimeSeries expectedResult = new StockPriceTimeSeries(meta, prices);

        EODStockHistoricalPriceProviderService service = mock(EODStockHistoricalPriceProviderService.class);
        when(service.getStockDailyHistoricalPrices(any())).thenReturn(expectedResult);

        StockHistoricalPriceParams params  = StockHistoricalPriceParams.newBuilder()
                .symbol(meta.getSymbol())
                .period(StockHistoricalPricePeriodParam.DAILY)
                .build();
        StockHistoricalPriceDataProviderFactory sut = new StockHistoricalPriceDataProviderFactory(service);
        StockPriceTimeSeries actualResult = sut.getStockPriceTimeSeries(params);

        assertThat(actualResult, is(equalTo(expectedResult)));
    }

    @Test
    void shouldCallGetStockDailyHistoricalPricesIfMonthlyPeriodProvided() {
        StockPriceMeta meta = new StockPriceMeta("", "s", "");
        StockPrice price = StockPrice.newBuilder()
                .close(123.0)
                .volume(10000000.0)
                .build();
        List<StockPrice> prices = new ArrayList<>(List.of(price));
        StockPriceTimeSeries expectedResult = new StockPriceTimeSeries(meta, prices);

        EODStockHistoricalPriceProviderService service = mock(EODStockHistoricalPriceProviderService.class);
        when(service.getStockMonthlyHistoricalPrices(any())).thenReturn(expectedResult);

        StockHistoricalPriceParams params  = StockHistoricalPriceParams.newBuilder()
                .symbol(meta.getSymbol())
                .period(StockHistoricalPricePeriodParam.MONTHLY)
                .build();
        StockHistoricalPriceDataProviderFactory sut = new StockHistoricalPriceDataProviderFactory(service);
        StockPriceTimeSeries actualResult = sut.getStockPriceTimeSeries(params);

        assertThat(actualResult, is(equalTo(expectedResult)));
    }

    @Test
    void shouldCallGetStockDailyHistoricalPricesIfWeeklyPeriodProvided() {
        StockPriceMeta meta = new StockPriceMeta("", "s", "");
        StockPrice price = StockPrice.newBuilder()
                .close(123.0)
                .volume(10000000.0)
                .build();
        List<StockPrice> prices = new ArrayList<>(List.of(price));
        StockPriceTimeSeries expectedResult = new StockPriceTimeSeries(meta, prices);

        EODStockHistoricalPriceProviderService service = mock(EODStockHistoricalPriceProviderService.class);
        when(service.getStockWeeklyHistoricalPrices(any())).thenReturn(expectedResult);

        StockHistoricalPriceParams params  = StockHistoricalPriceParams.newBuilder()
                .symbol(meta.getSymbol())
                .period(StockHistoricalPricePeriodParam.WEEKLY)
                .build();
        StockHistoricalPriceDataProviderFactory sut = new StockHistoricalPriceDataProviderFactory(service);
        StockPriceTimeSeries actualResult = sut.getStockPriceTimeSeries(params);

        assertThat(actualResult, is(equalTo(expectedResult)));
    }
}
