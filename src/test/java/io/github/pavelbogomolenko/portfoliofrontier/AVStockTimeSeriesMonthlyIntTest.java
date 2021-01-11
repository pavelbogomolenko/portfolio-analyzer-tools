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

        AVStockTimeSeriesService avStockTimeSeriesService = new AVStockTimeSeriesService();

        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        StockMonthlyTimeSeriesResponse stockMonthlyTimeSeriesResponse = avStockTimeSeriesService.getStockMonthlyTimeSeriesResponse(params);

        assertThat(stockMonthlyTimeSeriesResponse.getMeta(), hasProperty("info"));
        assertThat(stockMonthlyTimeSeriesResponse.getMeta(), hasProperty("symbol"));
        assertThat(stockMonthlyTimeSeriesResponse.getMeta(), hasProperty("timeZone"));

        assertThat(stockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("date"));
        assertThat(stockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("open"));
        assertThat(stockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("low"));
        assertThat(stockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("high"));
        assertThat(stockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("close"));
        assertThat(stockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("volume"));
    }
}
