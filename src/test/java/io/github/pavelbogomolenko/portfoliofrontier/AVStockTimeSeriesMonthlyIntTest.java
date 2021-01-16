package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AVStockTimeSeriesMonthlyIntTest {

    @Test
    void GivenGOOGLSymbol_WhenGetStockMonthlyTimeSeriesResponseIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() throws InterruptedException, IOException, URISyntaxException {
        String givenSymbol = "GOOGL";

        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesServiceImpl = new AVStockTimeSeriesDataProviderServiceImpl();

        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        StockMonthlyTimeSeriesData stockMonthlyTimeSeriesData = avStockTimeSeriesServiceImpl.getStockMonthlyTimeSeriesData(params);

        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("info"));
        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("symbol"));
        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("timeZone"));

        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("date"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("open"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("low"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("high"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("close"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("volume"));
    }
}
