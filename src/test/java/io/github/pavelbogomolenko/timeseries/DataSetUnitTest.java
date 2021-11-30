package io.github.pavelbogomolenko.timeseries;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPrice;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @Test
    void shouldCalculateAverage() {
        double expectedSum = 0;
        for(DataPoint dp: this.dataSet.getDataPoints()) {
            expectedSum += dp.getValue();
        }
        double expectedAv = expectedSum / this.dataSet.getDataPoints().size();

        assertThat(this.dataSet.getAverage(), equalTo(expectedAv));
    }

    @Test
    void shouldCalculateAverageFor2LastDataPoints() {
        int n = 2;
        double last = this.dataSet.getDataPoints().get(0).getValue();
        double lastBeforeLast = this.dataSet.getDataPoints().get(1).getValue();

        assertThat(this.dataSet.getNthAverage(n), equalTo((last + lastBeforeLast) / n));
    }

    @ParameterizedTest
    @ValueSource(ints = {10, -1, 0})
    void shouldThrowOutOfTheBoundErrorWhenCalculatingNthAverageNGreaterThanAmountOfDataPoints(int n) {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.dataSet.getNthAverage(n);
        });
    }

    @Test
    void shouldSliceDataSet() {
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
        StockPrice decPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2019-12-30"))
                .close(999.0)
                .build();
        ArrayList<StockPrice> oldTs = new ArrayList<>(
                Arrays.asList(marPriceTimeSeries, febPriceTimeSeries, janPriceTimeSeries, decPriceTimeSeries)
        );

        DataSet oldDs = ListToDataSet.convert(oldTs, "date", "close");

        ArrayList<StockPrice> newTs = new ArrayList<>(
                Arrays.asList(marPriceTimeSeries, febPriceTimeSeries)
        );
        DataSet newDs = ListToDataSet.convert(newTs, "date", "close");

        int sliceSize = 2;
        DataSet actualDs = oldDs.sliceFromHead(sliceSize);

        assertThat(actualDs.getDataPoints(), hasSize(sliceSize));
        assertThat(actualDs.getDataPoints().get(0), hasProperty("value", equalTo(1150.0)));
        assertThat(actualDs.getDataPoints().get(1), hasProperty("value", equalTo(1100.0)));
    }

    @Test
    void shouldThrowOutOfTheBoundErrorWhenSlicingDataSet() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.dataSet.sliceFromHead(10);
        });
    }

    @Test
    void shouldCalculateMedianOddThreePoints() {
        double expectedValue = 1000.0;
        StockPrice marPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-03-30"))
                .close(990.0)
                .build();
        StockPrice febPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-02-28"))
                .close(1100.0)
                .build();
        StockPrice janPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-01-30"))
                .close(expectedValue)
                .build();
        ArrayList<StockPrice> oldTs = new ArrayList<>(
                Arrays.asList(marPriceTimeSeries, febPriceTimeSeries, janPriceTimeSeries)
        );

        DataSet ds = ListToDataSet.convert(oldTs, "date", "close");
        assertThat(ds.getMedian(), equalTo(expectedValue));
    }

    @Test
    void shouldCalculateMedianOddFivePoints() {
        double expectedValue = 1045.0;
        StockPrice mayPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-05-30"))
                .close(1045.0)
                .build();
        StockPrice aprPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-04-30"))
                .close(1050.0)
                .build();
        StockPrice marPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-03-30"))
                .close(990.0)
                .build();
        StockPrice febPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-02-28"))
                .close(1100.0)
                .build();
        StockPrice janPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-01-30"))
                .close(1000.0)
                .build();
        ArrayList<StockPrice> oldTs = new ArrayList<>(
                Arrays.asList(mayPriceTimeSeries, aprPriceTimeSeries, marPriceTimeSeries, febPriceTimeSeries, janPriceTimeSeries)
        );

        DataSet ds = ListToDataSet.convert(oldTs, "date", "close");
        assertThat(ds.getMedian(), equalTo(expectedValue));
    }

    @Test
    void shouldCalculateMedianOnePoint() {
        double expectedValue = 1045.0;
        StockPrice mayPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-05-30"))
                .close(1045.0)
                .build();
        ArrayList<StockPrice> oldTs = new ArrayList<>(
                Arrays.asList(mayPriceTimeSeries)
        );

        DataSet ds = ListToDataSet.convert(oldTs, "date", "close");
        assertThat(ds.getMedian(), equalTo(expectedValue));
    }

    @Test
    void shouldCalculateMedianEvenPoints() {
        StockPrice aprPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-04-30"))
                .close(1050.0)
                .build();
        StockPrice marPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-03-30"))
                .close(990.0)
                .build();
        StockPrice febPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-02-28"))
                .close(1100.0)
                .build();
        StockPrice janPriceTimeSeries = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-01-30"))
                .close(1000.0)
                .build();
        ArrayList<StockPrice> oldTs = new ArrayList<>(
                Arrays.asList(aprPriceTimeSeries, marPriceTimeSeries, febPriceTimeSeries, janPriceTimeSeries)
        );

        double expectedMedian = (janPriceTimeSeries.getClose() + aprPriceTimeSeries.getClose()) / 2;
        DataSet ds = ListToDataSet.convert(oldTs, "date", "close");
        assertThat(ds.getMedian(), equalTo(expectedMedian));
    }
}
