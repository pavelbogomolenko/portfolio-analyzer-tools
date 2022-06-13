package io.github.pavelbogomolenko.timeseries;

import io.github.pavelbogomolenko.stockhistoricalprice.StockHistoricalPriceParams;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPrice;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPriceMeta;
import io.github.pavelbogomolenko.stockhistoricalprice.StockPriceTimeSeries;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataSetsRelationUnitTest {
    private final ArrayList<String> symbols = new ArrayList<>(Arrays.asList("MSFT", "GOOGLE", "IBM"));
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
    void shouldBuildReturnsVarianceCovarianceMatrix() {
        DataSet msftTs = ListToDataSet.convert(msftData.prices(), "date", "close");
        DataSet googleTs = ListToDataSet.convert(googleData.prices(), "date", "close");
        DataSet ibmTs = ListToDataSet.convert(ibmData.prices(), "date", "close");
        ArrayList<DataSet> ts = new ArrayList<>(Arrays.asList(msftTs, googleTs, ibmTs));

        DataSetsRelation relations = new DataSetsRelation();
        double[][] resultMatrix = relations.annualizedVarCovarMatrix(ts);

        assertThat(resultMatrix.length, is(ts.size()));
        assertThat(resultMatrix[0].length, is(ts.size()));
        assertThat(resultMatrix[0][0], is(msftTs.getVariance() * 12));
        assertThat(resultMatrix[1][1], is(googleTs.getVariance() * 12));
        assertThat(resultMatrix[2][2], is(ibmTs.getVariance() * 12));
    }
}
