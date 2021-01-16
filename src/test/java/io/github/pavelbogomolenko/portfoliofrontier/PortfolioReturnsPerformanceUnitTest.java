package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfolioReturnsPerformanceUnitTest {
    private final ArrayList<String> symbols = new ArrayList<>(Arrays.asList("MSFT", "GOOGLE", "IBM"));
    private final LocalDate dateFrom = LocalDate.parse("2020-10-30");
    private final  LocalDate dateTo = LocalDate.parse("2020-12-30");
    private final StockTimeSeriesServiceParams msftParams = StockTimeSeriesServiceParams.newBuilder()
            .symbol(symbols.get(0))
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
    private final StockTimeSeriesServiceParams googleParams = StockTimeSeriesServiceParams.newBuilder()
            .symbol(symbols.get(1))
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
    private final StockTimeSeriesServiceParams ibmParams = StockTimeSeriesServiceParams.newBuilder()
            .symbol(symbols.get(1))
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
    private StockMonthlyTimeSeriesData msftData;
    private StockMonthlyTimeSeriesData googleData;
    private StockMonthlyTimeSeriesData ibmData;

    @BeforeAll
    void initBeforeAll() {
        StockMetaTimeSeries msftMetaData = new StockMetaTimeSeries("a", symbols.get(0), "us");
        StockPriceTimeSeries msftOctPriceData = StockPriceTimeSeries.newBuilder()
                .date(dateFrom)
                .close(500)
                .build();
        StockPriceTimeSeries msftNovPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        StockPriceTimeSeries msftDecPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPriceTimeSeries> msftPriceData = new ArrayList<>(Arrays.asList(msftDecPriceData, msftNovPriceData, msftOctPriceData));
        msftData = new StockMonthlyTimeSeriesData(msftMetaData, msftPriceData);

        StockMetaTimeSeries googleMetaData = new StockMetaTimeSeries("a", symbols.get(1), "us");
        StockPriceTimeSeries googleOctPriceData = StockPriceTimeSeries.newBuilder()
                .date(dateFrom)
                .close(500)
                .build();
        StockPriceTimeSeries googleNovPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        StockPriceTimeSeries googleDecPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPriceTimeSeries> googlePriceData = new ArrayList<>(Arrays.asList(googleDecPriceData, googleNovPriceData, googleOctPriceData));
        googleData = new StockMonthlyTimeSeriesData(googleMetaData, googlePriceData);

        StockMetaTimeSeries ibmMetaData = new StockMetaTimeSeries("a", symbols.get(2), "us");
        StockPriceTimeSeries ibmOctPriceData = StockPriceTimeSeries.newBuilder()
                .date(dateFrom)
                .close(500)
                .build();
        StockPriceTimeSeries ibmNovPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        StockPriceTimeSeries ibmDecPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-12-30"))
                .close(520)
                .build();
        ArrayList<StockPriceTimeSeries> ibmPriceData = new ArrayList<>(Arrays.asList(ibmDecPriceData, ibmNovPriceData, ibmOctPriceData));
        ibmData = new StockMonthlyTimeSeriesData(ibmMetaData, ibmPriceData);
    }

    @Test
    void givenListOfStockSymbolsAndDateRange_whenGetMonthlyStockPriceReturnsPerformanceIsCalled_shouldReturnListOfMonthlyStockPriceReturnsPerformance()
            throws InterruptedException, IOException, URISyntaxException {
        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesServiceMock = mock(AVStockTimeSeriesDataProviderServiceImpl.class);
        when(avStockTimeSeriesServiceMock.getStockMonthlyTimeSeriesData(any())).thenReturn(msftData, googleData,ibmData);

        PortfolioReturnsPerformance portfolioReturnsPerformance = new PortfolioReturnsPerformance(avStockTimeSeriesServiceMock);
        PortfolioReturnsPerformanceParams params = PortfolioReturnsPerformanceParams.newBuilder()
                .symbols(symbols)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        ArrayList<MonthlyStockPriceReturnsPerformance> actualResult = portfolioReturnsPerformance.getMonthlyStockPriceReturnsPerformances(params);

        StockTimeSeriesServiceParamsMatcher msftParamMatcher = new StockTimeSeriesServiceParamsMatcher(msftParams);
        StockTimeSeriesServiceParamsMatcher googleParamMatcher = new StockTimeSeriesServiceParamsMatcher(googleParams);
        StockTimeSeriesServiceParamsMatcher ibmParamMatcher = new StockTimeSeriesServiceParamsMatcher(ibmParams);

        verify(avStockTimeSeriesServiceMock, times(1))
                .getStockMonthlyTimeSeriesData(argThat(msftParamMatcher));
        verify(avStockTimeSeriesServiceMock, times(1))
                .getStockMonthlyTimeSeriesData(argThat(googleParamMatcher));
        verify(avStockTimeSeriesServiceMock, times(1))
                .getStockMonthlyTimeSeriesData(argThat(ibmParamMatcher));

        assertThat(actualResult.size(), is(equalTo(3)));
    }

    @Test
    void shouldThrowExceptionWhenStockTimeSeriesHasLessRecordsThanMonthGivenWithDateRange()
            throws InterruptedException, IOException, URISyntaxException {
        ArrayList<String> symbols = new ArrayList<>(Arrays.asList("MSFT", "GOOGLE", "IBM"));
        LocalDate dateFrom = LocalDate.parse("2020-10-30");
        LocalDate dateTo = LocalDate.parse("2020-12-30");

        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesServiceMock = mock(AVStockTimeSeriesDataProviderServiceImpl.class);

        StockMetaTimeSeries msftMetaData = new StockMetaTimeSeries("a", symbols.get(0), "us");
        StockPriceTimeSeries msftOctPriceData = StockPriceTimeSeries.newBuilder()
                .date(dateFrom)
                .close(500)
                .build();
        StockPriceTimeSeries msftNovPriceData = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(510)
                .build();
        ArrayList<StockPriceTimeSeries> msftPriceData = new ArrayList<>(Arrays.asList(msftNovPriceData, msftOctPriceData));
        StockMonthlyTimeSeriesData msftData = new StockMonthlyTimeSeriesData(msftMetaData, msftPriceData);
        when(avStockTimeSeriesServiceMock.getStockMonthlyTimeSeriesData(any())).thenReturn(msftData);

        PortfolioReturnsPerformance portfolioReturnsPerformance = new PortfolioReturnsPerformance(avStockTimeSeriesServiceMock);
        PortfolioReturnsPerformanceParams params = PortfolioReturnsPerformanceParams.newBuilder()
                .symbols(symbols)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            portfolioReturnsPerformance.getMonthlyStockPriceReturnsPerformances(params);
        });
    }

    @Test
    void shouldBuildReturnsVarianceCovarianceMatrix() throws InterruptedException, IOException, URISyntaxException {
        MonthlyStockPriceReturnsPerformance msftPerf = new MonthlyStockPriceReturnsPerformance(msftData);
        MonthlyStockPriceReturnsPerformance googlePerf = new MonthlyStockPriceReturnsPerformance(googleData);
        MonthlyStockPriceReturnsPerformance ibmPerf = new MonthlyStockPriceReturnsPerformance(googleData);
        ArrayList<MonthlyStockPriceReturnsPerformance> portfolioReturns = new ArrayList<>(Arrays.asList(msftPerf, googlePerf, ibmPerf));

        PortfolioReturnsPerformance portfolioReturnsPerformance = mock(PortfolioReturnsPerformance.class, CALLS_REAL_METHODS);
        PortfolioReturnsPerformanceParams params = PortfolioReturnsPerformanceParams.newBuilder()
                .symbols(symbols)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();
        PortfolioReturnsPerformanceParamsMatcher paramsMatcher = new PortfolioReturnsPerformanceParamsMatcher(params);
        doReturn(portfolioReturns).when(portfolioReturnsPerformance).getMonthlyStockPriceReturnsPerformances(argThat(paramsMatcher));

        double[][] resultMatrix = portfolioReturnsPerformance.getReturnsVarianceCovarianceMatrix(params);

        assertThat(resultMatrix.length, is(equalTo(portfolioReturns.size())));
        assertThat(resultMatrix[0].length, is(equalTo(portfolioReturns.size())));
        assertThat(resultMatrix[0][0], is(equalTo(msftPerf.getVariance())));
        assertThat(resultMatrix[1][1], is(equalTo(googlePerf.getVariance())));
        assertThat(resultMatrix[2][2], is(equalTo(ibmPerf.getVariance())));
    }
}
