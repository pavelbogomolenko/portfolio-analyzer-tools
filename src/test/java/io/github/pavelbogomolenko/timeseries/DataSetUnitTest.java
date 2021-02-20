package io.github.pavelbogomolenko.timeseries;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPrice;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataSetUnitTest {
    private DataSet dataSet;
    private StockHistoricalPriceParams givenInputParams = StockHistoricalPriceParams.newBuilder()
            .symbol("AMZN")
            .build();

    @BeforeAll
    void initForAll() {
        StockPrice marPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-03-30"))
                .close(1150.0)
                .build();
        StockPrice febPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-02-28"))
                .close(1100.0)
                .build();
        StockPrice janPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-01-30"))
                .close(1000.0)
                .build();
        ArrayList<StockPrice> priceTimeSeries = new ArrayList<>(
                Arrays.asList(marPriceTimeSeries, febPriceTimeSeries, janPriceTimeSeries)
        );
        this.dataSet = ListToDataSet.convert(priceTimeSeries, "date", "close");
    }

    @Test
    void shouldCalculateStockReturns() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        ArrayList<Double> actualReturns = this.dataSet.getGrowthRates();

        double febToJanGrowth = (febPriceTimeSeries.getValue() - janPriceTimeSeries.getValue()) / janPriceTimeSeries.getValue();
        double marToFebGrowth = (marPriceTimeSeries.getValue() - febPriceTimeSeries.getValue()) / febPriceTimeSeries.getValue();

        assertThat(actualReturns, contains(febToJanGrowth, marToFebGrowth));
    }

    @Test
    void shouldCalculateAverageReturn() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        double actualAverageReturn = this.dataSet.getAverageGrowth();

        double febToJanGrowth = (febPriceTimeSeries.getValue() - janPriceTimeSeries.getValue()) / janPriceTimeSeries.getValue();
        double marToFebGrowth = (marPriceTimeSeries.getValue() - febPriceTimeSeries.getValue()) / febPriceTimeSeries.getValue();

        double expectedAverageReturn = (febToJanGrowth + marToFebGrowth) / 2;

        assertThat(actualAverageReturn, equalTo(expectedAverageReturn));
    }

    @Test
    void shouldCalculateAverageAnnualReturn() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        double actualAverageAnnualReturn = this.dataSet.getAverageGrowth() * 12;

        double febToJanGrowth = (febPriceTimeSeries.getValue() - janPriceTimeSeries.getValue()) / janPriceTimeSeries.getValue();
        double marToFebGrowth = (marPriceTimeSeries.getValue() - febPriceTimeSeries.getValue()) / febPriceTimeSeries.getValue();

        double expectedAverageAnnualReturn = ((febToJanGrowth + marToFebGrowth) / 2) * 12;

        assertThat(actualAverageAnnualReturn, equalTo(expectedAverageAnnualReturn));
    }

    @Test
    void shouldCalculateDifferenceBetweenEachReturnAndAverageReturn() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        double febToJanReturn = (febPriceTimeSeries.getValue() - janPriceTimeSeries.getValue()) / janPriceTimeSeries.getValue();
        double marToFebReturn = (marPriceTimeSeries.getValue() - febPriceTimeSeries.getValue()) / febPriceTimeSeries.getValue();

        double averageReturn = (febToJanReturn + marToFebReturn) / 2;

        ArrayList<Double> expectedDiffs = new ArrayList<>(Arrays.asList((febToJanReturn - averageReturn), (marToFebReturn - averageReturn)));
        assertThat(this.dataSet.getGrowthRatesToAverage(), contains(expectedDiffs.get(0), expectedDiffs.get(1)));
    }

    @Test
    void shouldCalculateVarianceReturn() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        DataSet dataSet = new DataSet(this.dataSet.getDataPoints());

        double febToJanReturn = (febPriceTimeSeries.getValue() - janPriceTimeSeries.getValue()) / janPriceTimeSeries.getValue();
        double marToFebReturn = (marPriceTimeSeries.getValue() - febPriceTimeSeries.getValue()) / febPriceTimeSeries.getValue();

        double averageReturn = (febToJanReturn + marToFebReturn) / 2;

        double expectedVariance = (Math.pow(febToJanReturn - averageReturn, 2) + Math.pow(marToFebReturn - averageReturn, 2)) / 2;

        assertThat(dataSet.getVariance(), equalTo(expectedVariance));
    }

    @Test
    void shouldCalculateVarianceAndStdDev() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        double febToJanReturn = (febPriceTimeSeries.getValue() - janPriceTimeSeries.getValue()) / janPriceTimeSeries.getValue();
        double marToFebReturn = (marPriceTimeSeries.getValue() - febPriceTimeSeries.getValue()) / febPriceTimeSeries.getValue();

        double averageReturn = (febToJanReturn + marToFebReturn) / 2;

        double expectedVariance = (Math.pow(febToJanReturn - averageReturn, 2) + Math.pow(marToFebReturn - averageReturn, 2)) / 2;

        assertThat(this.dataSet.getVariance(), equalTo(expectedVariance));
        assertThat(this.dataSet.getStdDev(), equalTo(Math.sqrt(expectedVariance)));
    }

    @Test
    void shouldThrowIfGivenNotOrderedDataPoints() {
        DataPoint marPriceTimeSeries = this.dataSet.getDataPoints().get(0);
        DataPoint febPriceTimeSeries = this.dataSet.getDataPoints().get(1);
        DataPoint janPriceTimeSeries = this.dataSet.getDataPoints().get(2);

        ArrayList<DataPoint> notOrderedDataPoints = new ArrayList<>(Arrays.asList(febPriceTimeSeries, marPriceTimeSeries, janPriceTimeSeries));

        assertThrows(IllegalArgumentException.class, () -> {
            new DataSet(notOrderedDataPoints);
        });
    }
}
