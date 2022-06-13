package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import java.time.LocalDate;
import java.util.*;

import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.timeseries.DataSet;

import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockPriceHistoricalDataSetListServiceUnitTest {
    private final List<String> symbols = new ArrayList<>(Arrays.asList("MSFT", "GOOGLE", "IBM"));
    private final LocalDate dateFrom = LocalDate.parse("2020-10-01");
    private final LocalDate dateTo = LocalDate.parse("2020-12-01");
    private final StockHistoricalPriceParams msftParams = StockHistoricalPriceParams.newBuilder()
            .symbol(symbols.get(0))
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
    private final StockHistoricalPriceParams googleParams = StockHistoricalPriceParams.newBuilder()
            .symbol(symbols.get(1))
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
    private final StockHistoricalPriceParams ibmParams = StockHistoricalPriceParams.newBuilder()
            .symbol(symbols.get(1))
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
    private StockPriceTimeSeries msftData;
    private StockPriceTimeSeries googleData;
    private StockPriceTimeSeries ibmData;

    @BeforeAll
    void initBeforeAll() {
        StockPriceMeta msftMetaData = new StockPriceMeta("a", symbols.get(0), "us");
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
        msftData = new StockPriceTimeSeries(msftMetaData, msftPriceData);

        StockPriceMeta googleMetaData = new StockPriceMeta("a", symbols.get(1), "us");
        StockPrice googleOctPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(500)
                .build();
        StockPrice googleNovPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        StockPrice googleDecPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPrice> googlePriceData = new ArrayList<>(Arrays.asList(googleDecPriceData, googleNovPriceData, googleOctPriceData));
        googleData = new StockPriceTimeSeries(googleMetaData, googlePriceData);

        StockPriceMeta ibmMetaData = new StockPriceMeta("a", symbols.get(2), "us");
        StockPrice ibmOctPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(500)
                .build();
        StockPrice ibmNovPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        StockPrice ibmDecPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPrice> ibmPriceData = new ArrayList<>(Arrays.asList(ibmDecPriceData, ibmNovPriceData, ibmOctPriceData));
        ibmData = new StockPriceTimeSeries(ibmMetaData, ibmPriceData);
    }

    @Test
    void shouldReturnDataSetList_for_given_SymbolsAndDates() {
        StockHistoricalPriceDataProviderFactory serviceFactory = mock(StockHistoricalPriceDataProviderFactory.class);
        when(serviceFactory.getStockPriceTimeSeries(any())).thenReturn(msftData, googleData,ibmData);

        StockHistoricalPriceDataSetParam params = StockHistoricalPriceDataSetParam.newBuilder()
                .symbols(symbols)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .property("adjClose")
                .build();
        StockPriceHistoricalDataSetService sut = new StockPriceHistoricalDataSetService(serviceFactory);
        List<DataSet> actualResult = sut.getMonthlyAdjustedDataSetListForProperty(params);

        StockHistoricalPriceParamsMatcher msftParamMatcher = new StockHistoricalPriceParamsMatcher(msftParams);
        StockHistoricalPriceParamsMatcher googleParamMatcher = new StockHistoricalPriceParamsMatcher(googleParams);
        StockHistoricalPriceParamsMatcher ibmParamMatcher = new StockHistoricalPriceParamsMatcher(ibmParams);

        verify(serviceFactory, times(1))
                .getStockPriceTimeSeries(argThat(msftParamMatcher));
        verify(serviceFactory, times(1))
                .getStockPriceTimeSeries(argThat(googleParamMatcher));
        verify(serviceFactory, times(1))
                .getStockPriceTimeSeries(argThat(ibmParamMatcher));

        assertThat(actualResult.size(), is(equalTo(3)));
    }
}
