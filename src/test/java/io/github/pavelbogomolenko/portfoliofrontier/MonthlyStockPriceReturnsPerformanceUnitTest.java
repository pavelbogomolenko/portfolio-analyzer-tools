package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MonthlyStockPriceReturnsPerformanceUnitTest {
    private StockMonthlyTimeSeriesData stockMonthlyTimeSeriesData;
    private AVStockTimeSeriesServiceImpl avStockTimeSeriesServiceImplMock;
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
        stockMonthlyTimeSeriesData = new StockMonthlyTimeSeriesData(meta, priceTimeSeries);
    }

    @Test
    void shouldCalculateStockReturns() {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(stockMonthlyTimeSeriesData);

        ArrayList<Double> actualReturns = stockPerformance.getMonthlyReturns();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        assertThat(actualReturns, contains(febToJanGrowth, marToFebGrowth));
    }

    @Test
    void shouldCalculateAverageReturn() {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(stockMonthlyTimeSeriesData);

        double actualAverageReturn = stockPerformance.getAverageReturn();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        double expectedAverageReturn = (febToJanGrowth + marToFebGrowth) / 2;

        assertThat(actualAverageReturn, equalTo(expectedAverageReturn));
    }

    @Test
    void shouldCalculateAverageAnnualReturn() {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(stockMonthlyTimeSeriesData);

        double actualAverageAnnualReturn = stockPerformance.getAverageAnnualReturn();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        double expectedAverageAnnualReturn = ((febToJanGrowth + marToFebGrowth) / 2) * 12;

        assertThat(actualAverageAnnualReturn, equalTo(expectedAverageAnnualReturn));
    }

    @Test
    void shouldCalculateVarianceReturn() {
        StockPriceTimeSeries marPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(0);
        StockPriceTimeSeries febPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(1);
        StockPriceTimeSeries janPriceTimeSeries = stockMonthlyTimeSeriesData.getPrices().get(2);

        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(stockMonthlyTimeSeriesData);

        double febToJanReturn = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebReturn = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        double averageReturn = (febToJanReturn + marToFebReturn) / 2;

        double expectedVariance = (Math.pow(febToJanReturn - averageReturn, 2) + Math.pow(marToFebReturn - averageReturn, 2)) / 2;

        assertThat(stockPerformance.getVariance(), equalTo(expectedVariance));
        assertThat(stockPerformance.getAnnualVariance(), equalTo(expectedVariance * 12));
    }
}
