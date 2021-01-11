package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class AVStockTimeSeriesMonthlyUnitTest {

    @Test
    void WhenCallingGetStockMonthlyTimeSeriesResponseWithValidSymbolAndNoDateRange_ShouldAVTimeSeriesMonthlyResponseContainAllMonthlyTimeSeriesFromAVStockDataFetcherResponse()
            throws InterruptedException, IOException, URISyntaxException {
        StockPriceTimeSeries firstPrice = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2021-01-04"))
                .open(3270.0)
                .close(3186.6300)
                .high(3272.0)
                .low(33144.0200)
                .volume(4411449)
                .build();
        StockPriceTimeSeries secondPrice = StockPriceTimeSeries.newBuilder()
                .date(LocalDate.parse("2020-12-31"))
                .open(3258.0000)
                .close(3186.6300)
                .high(3272.0)
                .low(33144.0200)
                .volume(4411449)
                .build();
        String rawStringResponse = "{";
        rawStringResponse += "'Meta Data':";
        rawStringResponse += "{'1. Information': 'info', '2. Symbol': 'AMZN', '3. Last Refreshed': '2021-01-04', '4. Time Zone': 'us'},";
        rawStringResponse += "'Monthly Time Series':";
        rawStringResponse += "{";
        rawStringResponse += String.format("'%s': {'1. open': '%s', '2. high': '%s', '3. low': '%s', '4. close': '%s', '5. volume': '%s'},",
                firstPrice.getDate(), firstPrice.getOpen(), firstPrice.getHigh(), firstPrice.getLow(), firstPrice.getClose(), firstPrice.getVolume());
        rawStringResponse += String.format("'%s': {'1. open': '%s', '2. high': '%s', '3. low': '%s', '4. close': '%s', '5. volume': '%s'}",
                secondPrice.getDate(), secondPrice.getOpen(), secondPrice.getHigh(), secondPrice.getLow(), secondPrice.getClose(), secondPrice.getVolume());
        rawStringResponse += "}"; // end Monthly Time Series
        rawStringResponse += "}"; // end
        String givenSymbol = "AMZN";

        AVStockDataFetcher avStockDataFetcher = mock(AVStockDataFetcher.class);

        when(avStockDataFetcher.getMonthlyTimeSeries(givenSymbol)).thenReturn(rawStringResponse);
        AVStockTimeSeriesService avStockTimeSeriesService = new AVStockTimeSeriesService(avStockDataFetcher);

        AVStockTimeSeriesServiceParams params = AVStockTimeSeriesServiceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        AVStockMonthlyTimeSeriesResponse avStockMonthlyTimeSeriesResponse = avStockTimeSeriesService.getStockMonthlyTimeSeriesResponse(params);

        verify(avStockDataFetcher, times(1)).getMonthlyTimeSeries("AMZN");
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("info"));
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("symbol"));
        assertThat(avStockMonthlyTimeSeriesResponse.getMeta(), hasProperty("timeZone"));

        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(0).getDate(), is(firstPrice.getDate()));
        assertThat(avStockMonthlyTimeSeriesResponse.getPrices().get(1).getDate(), is(secondPrice.getDate()));
    }

    @Test
    void WhenGetStockMonthlyTimeSeriesResponseWithNullSymbolIsCalled_ThenThrowException_symbol_should_not_be_empty() {
        assertThrows(NullPointerException.class, () -> {
            AVStockTimeSeriesService avStockTimeSeriesService = new AVStockTimeSeriesService(mock(AVStockDataFetcher.class));
            avStockTimeSeriesService.getStockMonthlyTimeSeriesResponse(null);
        });
    }

}
