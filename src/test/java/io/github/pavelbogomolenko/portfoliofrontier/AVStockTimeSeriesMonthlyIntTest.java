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

        AVStockMonthlyTimeSeriesResponse avStockMonthlyTimeSeriesResponse = avStockTimeSeriesService.getStockMonthlyTimeSeriesResponse(givenSymbol);

        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("info"));
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("symbol"));
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("timeZone"));

        assertThat(avStockMonthlyTimeSeriesResponse.getPrices()[0], hasProperty("date"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices()[0], hasProperty("open"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices()[0], hasProperty("low"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices()[0], hasProperty("high"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices()[0], hasProperty("close"));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices()[0], hasProperty("volume"));
    }
}
