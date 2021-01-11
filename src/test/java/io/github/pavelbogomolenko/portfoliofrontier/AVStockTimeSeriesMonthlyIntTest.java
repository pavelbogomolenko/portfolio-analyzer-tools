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

        AVStockTimeSeriesServiceParams params = AVStockTimeSeriesServiceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        AVStockMonthlyTimeSeriesResponse avStockMonthlyTimeSeriesResponse = avStockTimeSeriesService.getStockMonthlyTimeSeriesResponse(params);

        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("info"));
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("symbol"));
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("timeZone"));

        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("date"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("open"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("low"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("high"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("close"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0), hasProperty("volume"));
    }
}
