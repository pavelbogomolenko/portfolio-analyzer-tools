package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MonthlyStockPriceReturnsPerformanceUnitTest {
    @Test
    void shouldCalculateGrowthRatesForGivenStock() throws InterruptedException, IOException, URISyntaxException {
        String stockSymbol = "AMZN";
        AVStockTimeSeriesService avStockTimeSeriesServiceMock = mock(AVStockTimeSeriesService.class);
        StockTimeSeriesServiceParams givenInputParams = StockTimeSeriesServiceParams.newBuilder()
                .symbol(stockSymbol)
                .build();

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
        StockMonthlyTimeSeriesResponse stockMonthlyTimeSeriesResponse = new StockMonthlyTimeSeriesResponse(meta, priceTimeSeries);

        when(avStockTimeSeriesServiceMock.getStockMonthlyTimeSeriesResponse(givenInputParams)).thenReturn(stockMonthlyTimeSeriesResponse);
        MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(avStockTimeSeriesServiceMock, givenInputParams);

        ArrayList<Double> actualGrowthRate = stockPerformance.getClosePriceGrowthRates();

        double febToJanGrowth = (febPriceTimeSeries.getClose() - janPriceTimeSeries.getClose()) / janPriceTimeSeries.getClose();
        double marToFebGrowth = (marPriceTimeSeries.getClose() - febPriceTimeSeries.getClose()) / febPriceTimeSeries.getClose();

        verify(avStockTimeSeriesServiceMock, times(1)).getStockMonthlyTimeSeriesResponse(givenInputParams);
        assertThat(actualGrowthRate, contains(febToJanGrowth, marToFebGrowth));
    }
}
