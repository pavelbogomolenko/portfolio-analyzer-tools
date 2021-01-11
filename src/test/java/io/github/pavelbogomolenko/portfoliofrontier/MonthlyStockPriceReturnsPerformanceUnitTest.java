package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MonthlyStockPriceReturnsPerformanceUnitTest {
    private StockMonthlyTimeSeriesResponse stockMonthlyTimeSeriesResponse;
    private AVStockTimeSeriesService avStockTimeSeriesServiceMock;
    private StockTimeSeriesServiceParams givenInputParams = StockTimeSeriesServiceParams.newBuilder()
            .symbol("AMZN")
            .build();

    @BeforeAll
    void initForAll() {
        StockMetaTimeSeries meta = new StockMetaTimeSeries("a", "b", "c");
        StockPriceTimeSeries marPriceTimeSeries = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-03-30"))
                .close(1150.0)
                .build();
        StockPriceTimeSeries febPriceTimeSeries = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-02-28"))
                .close(1100.0)
                .build();
        StockPriceTimeSeries janPriceTimeSeries = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-01-30"))
                .close(1000.0)
                .build();
        ArrayList<StockPriceTimeSeries> priceTimeSeries = new ArrayList<>(
                Arrays.asList(marPriceTimeSeries, febPriceTimeSeries, janPriceTimeSeries)
        );
        stockMonthlyTimeSeriesResponse = new StockMonthlyTimeSeriesResponse(meta, priceTimeSeries);
    }

    @BeforeEach
    void initBeforeEach() throws InterruptedException, IOException, URISyntaxException {
        avStockTimeSeriesServiceMock = mock(AVStockTimeSeriesService.class);
        when(avStockTimeSeriesServiceMock.getStockMonthlyTimeSeriesResponse(givenInputParams)).thenReturn(stockMonthlyTimeSeriesResponse);
    }

    @AfterEach
    void tearDownAfterEach() throws InterruptedException, IOException, URISyntaxException {
        verify(avStockTimeSeriesServiceMock, times(1)).getStockMonthlyTimeSeriesResponse(givenInputParams);
    }

    @Test
    void shouldCalculateStockReturns() throws InterruptedException, IOException, URISyntaxException {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(avStockTimeSeriesServiceMock, givenInputParams);

        ArrayList<Double> actualReturns = stockPerformance.getMonthlyReturns();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        assertThat(actualReturns, contains(febToJanGrowth, marToFebGrowth));
    }

    @Test
    void shouldCalculateAverageReturn() throws InterruptedException, IOException, URISyntaxException {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(avStockTimeSeriesServiceMock, givenInputParams);

        double actualAverageReturn = stockPerformance.getAverageReturn();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        double expectedAverageReturn = (febToJanGrowth + marToFebGrowth) / 2;

        assertThat(actualAverageReturn, equalTo(expectedAverageReturn));
    }

    @Test
    void shouldCalculateAverageAnnualReturn() throws InterruptedException, IOException, URISyntaxException {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesResponse.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(avStockTimeSeriesServiceMock, givenInputParams);

        double actualAverageAnnualReturn = stockPerformance.getAverageAnnualReturn();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        double expectedAverageAnnualReturn = ((febToJanGrowth + marToFebGrowth) / 2) * 12;

        assertThat(actualAverageAnnualReturn, equalTo(expectedAverageAnnualReturn));
    }
}
