package io.github.pavelbogomolenko.stockhistoricalpricedataset;

import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.timeseries.DataSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockPriceHistoricalDataSetListServiceUnitTest {
    private final ArrayList<String> symbols = new ArrayList<>(Arrays.asList("MSFT", "GOOGLE", "IBM"));
    private final YearMonth dateFrom = YearMonth.parse("2020-10");
    private final  YearMonth dateTo = YearMonth.parse("2020-12");
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
    void shouldReturnListTSMeasures_for_given_SybmolsAndDates() {
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceMock = mock(AVStockHistoricalPriceProviderService.class);
        when(avStockTimeSeriesServiceMock.getStockMonthlyHistoricalPrices(any())).thenReturn(msftData, googleData,ibmData);

        StockPriceHistoricalDatasetListParams params = StockPriceHistoricalDatasetListParams.newBuilder()
                .symbols(symbols)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        StockPriceHistoricalDataSetListService portfolio = new StockPriceHistoricalDataSetListService(avStockTimeSeriesServiceMock);
        ArrayList<DataSet> actualResult = portfolio.getDataSetListForStocksMonthlyClosePrices(params);

        StockHistoricalPriceParamsMatcher msftParamMatcher = new StockHistoricalPriceParamsMatcher(msftParams);
        StockHistoricalPriceParamsMatcher googleParamMatcher = new StockHistoricalPriceParamsMatcher(googleParams);
        StockHistoricalPriceParamsMatcher ibmParamMatcher = new StockHistoricalPriceParamsMatcher(ibmParams);

        verify(avStockTimeSeriesServiceMock, times(1))
                .getStockMonthlyHistoricalPrices(argThat(msftParamMatcher));
        verify(avStockTimeSeriesServiceMock, times(1))
                .getStockMonthlyHistoricalPrices(argThat(googleParamMatcher));
        verify(avStockTimeSeriesServiceMock, times(1))
                .getStockMonthlyHistoricalPrices(argThat(ibmParamMatcher));

        assertThat(actualResult.size(), is(equalTo(3)));
    }

    @Test
    void shouldThrowExceptionWhenStockTimeSeriesHasLessRecordsThanMonthGivenWithDateRange() {
        ArrayList<String> symbols = new ArrayList<>(Arrays.asList("MSFT", "GOOGLE", "IBM"));

        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceMock = mock(AVStockHistoricalPriceProviderService.class);

        StockPriceMeta msftMetaData = new StockPriceMeta("a", symbols.get(0), "us");
        StockPrice msftOctPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(500)
                .build();
        StockPrice msftNovPriceData = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        ArrayList<StockPrice> msftPriceData = new ArrayList<>(Arrays.asList(msftNovPriceData, msftOctPriceData));
        StockPriceTimeSeries msftData = new StockPriceTimeSeries(msftMetaData, msftPriceData);
        when(avStockTimeSeriesServiceMock.getStockMonthlyHistoricalPrices(any())).thenReturn(msftData);

        StockPriceHistoricalDatasetListParams params = StockPriceHistoricalDatasetListParams.newBuilder()
                .symbols(symbols)
                .dateFrom(YearMonth.parse("2020-10"))
                .dateTo(YearMonth.parse("2020-12"))
                .build();
        assertThrows(IllegalArgumentException.class, () -> {
            StockPriceHistoricalDataSetListService stockPriceHistoricalDataSetListService = new StockPriceHistoricalDataSetListService(avStockTimeSeriesServiceMock);
            stockPriceHistoricalDataSetListService.getDataSetListForStocksMonthlyClosePrices(params);
        });
    }
}
