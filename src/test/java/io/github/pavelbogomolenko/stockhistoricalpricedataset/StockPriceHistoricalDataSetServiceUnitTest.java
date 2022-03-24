package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.timeseries.DataSet;
import io.github.pavelbogomolenko.timeseries.ListToDataSet;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class StockPriceHistoricalDataSetServiceUnitTest {

    @Test
    void shouldGetDataSetForStockMonthlyClosePrice() {
        String symbol = "msft";
        StockPriceMeta msftMetaData = new StockPriceMeta("a", symbol, "us");
        StockPrice msftOctPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(500)
                .build();
        StockPrice msftNovPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        StockPrice msftDecPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPrice> msftPriceData = new ArrayList<>(Arrays.asList(msftDecPriceData, msftNovPriceData, msftOctPriceData));
        StockPriceTimeSeries msftTs = new StockPriceTimeSeries(msftMetaData, msftPriceData);

        StockHistoricalPriceDataProviderFactory mockPriceProvider = mock(StockHistoricalPriceDataProviderFactory.class);
        when(mockPriceProvider.getStockPriceTimeSeries(any())).thenReturn(msftTs);

        StockPriceHistoricalDataSetService sut = new StockPriceHistoricalDataSetService(mockPriceProvider);

        StockHistoricalPriceParams sutParams = StockHistoricalPriceParams.newBuilder()
                .symbol(symbol)
                .build();
        DataSet actualResult = sut.getDataSet(sutParams);
        DataSet expectedDataSet = ListToDataSet.convert(msftPriceData, "date", "adjClose");

        assertThat(actualResult.getAverageGrowth(), is(expectedDataSet.getAverageGrowth()));
        assertThat(actualResult.getStdDev(), is(expectedDataSet.getStdDev()));
    }

    @Test
    void shouldGetDataSetForStockDailyClosePrice() {
        String symbol = "msft";
        StockPriceMeta msftMetaData = new StockPriceMeta("a", symbol, "us");
        StockPrice msftOctPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-28"))
                .close(500)
                .build();
        StockPrice msftNovPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-29"))
                .close(510)
                .build();
        StockPrice msftDecPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPrice> msftPriceData = new ArrayList<>(Arrays.asList(msftDecPriceData, msftNovPriceData, msftOctPriceData));
        StockPriceTimeSeries msftTs = new StockPriceTimeSeries(msftMetaData, msftPriceData);

        StockHistoricalPriceDataProviderFactory mockPriceProvider = mock(StockHistoricalPriceDataProviderFactory.class);
        when(mockPriceProvider.getStockPriceTimeSeries(any())).thenReturn(msftTs);

        StockPriceHistoricalDataSetService sut = new StockPriceHistoricalDataSetService(mockPriceProvider);
        StockHistoricalPriceParams sutParams = StockHistoricalPriceParams.newBuilder()
                .symbol(symbol)
                .build();
        DataSet actualResult = sut.getDataSet(sutParams);

        assertThat(actualResult.getDataPoints().size(), is(equalTo(msftPriceData.size())));
    }
}
