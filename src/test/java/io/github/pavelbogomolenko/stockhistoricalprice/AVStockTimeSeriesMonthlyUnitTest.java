package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class AVStockTimeSeriesMonthlyUnitTest {

    @Test
    void WhenCallingGetStockMonthlyTimeSeriesResponseWithValidSymbolAndNoDateRange_ShouldAVTimeSeriesMonthlyResponseContainAllMonthlyTimeSeriesFromAVStockDataFetcherResponse() {
        StockPrice firstPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-01-04"))
                .open(3270.0)
                .close(3186.6300)
                .adjClose(186.6300)
                .high(3272.0)
                .low(33144.0200)
                .volume(4411449)
                .build();
        StockPrice secondPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-31"))
                .open(3258.0000)
                .close(3186.6300)
                .adjClose(186.0)
                .high(3272.0)
                .low(33144.0200)
                .volume(4411449)
                .build();
        String rawStringResponse = "{";
        rawStringResponse += "'Meta Data':";
        rawStringResponse += "{'1. Information': 'info', '2. Symbol': 'AMZN', '3. Last Refreshed': '2021-01-04', '4. Time Zone': 'us'},";
        rawStringResponse += "'Monthly Adjusted Time Series':";
        rawStringResponse += "{";
        rawStringResponse += String.format("'%s': {'1. open': '%s', '2. high': '%s', '3. low': '%s', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '%s'},",
                firstPrice.getDate(), firstPrice.getOpen(), firstPrice.getHigh(), firstPrice.getLow(), firstPrice.getClose(), firstPrice.getAdjClose(), firstPrice.getVolume());
        rawStringResponse += String.format("'%s': {'1. open': '%s', '2. high': '%s', '3. low': '%s', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '%s'}",
                secondPrice.getDate(), secondPrice.getOpen(), secondPrice.getHigh(), secondPrice.getLow(), secondPrice.getClose(), secondPrice.getAdjClose(), secondPrice.getVolume());
        rawStringResponse += "}"; // end Monthly Time Series
        rawStringResponse += "}"; // end
        String givenSymbol = "AMZN";

        AVStockHistoricalHttpApi avHttpApiDataSource = mock(AVStockHistoricalHttpApi.class);

        when(avHttpApiDataSource.getRawMonthlyAdjPriceData(givenSymbol)).thenReturn(rawStringResponse);
        AVStockHistoricalPriceProviderService avStockHistoricalPriceProviderService = new AVStockHistoricalPriceProviderService(avHttpApiDataSource);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(params);

        verify(avHttpApiDataSource, times(1)).getRawMonthlyAdjPriceData("AMZN");
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().get(0).getDate(), is(firstPrice.getDate()));
        assertThat(stockPriceTimeSeries.getPrices().get(1).getDate(), is(secondPrice.getDate()));
        assertThat(stockPriceTimeSeries.getPrices().get(0).getAdjClose(), is(firstPrice.getAdjClose()));
        assertThat(stockPriceTimeSeries.getPrices().get(1).getAdjClose(), is(secondPrice.getAdjClose()));
    }

    @Test
    void shouldThrowWhenUnexpectedResponseHasBeenReturned() {
        String givenSymbol = "someSymbol";
        String unexpectedRawResponse = "{'some': thing}";
        AVStockHistoricalHttpApi avHttpApiDataSource = mock(AVStockHistoricalHttpApi.class);

        when(avHttpApiDataSource.getRawMonthlyAdjPriceData(givenSymbol.toUpperCase()))
                .thenReturn(unexpectedRawResponse);
        AVStockHistoricalPriceProviderService service = new AVStockHistoricalPriceProviderService(avHttpApiDataSource);

        assertThrows(RuntimeException.class, () -> {
            StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                    .symbol("someSymbol")
                    .build();
            StockPriceTimeSeries stockPriceTimeSeries = service.getStockMonthlyHistoricalPrices(params);
        });
    }

    @Test
    void WhenGetStockMonthlyTimeSeriesResponseWithNullSymbolIsCalled_ThenThrowException_symbol_should_not_be_empty() {
        assertThrows(NullPointerException.class, () -> {
            AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(mock(AVStockHistoricalHttpApi.class));
            avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(null);
        });
    }

    @Test
    void WhenCallingGetStockMonthlyTimeSeriesResponseWithValidSymbolAndDateRange_ShouldAVTimeSeriesMonthlyResponseContainAllMonthlyTimeSeriesFromAVStockDataFetcherResponseSatisfyingInputDateRange() {
        StockPrice decPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-31"))
                .close(3186.6300)
                .build();
        StockPrice novPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(3150.6300)
                .adjClose(150.6300)
                .build();
        StockPrice octPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(3140.6300)
                .adjClose(140.6300)
                .build();
        String rawStringResponse = "{";
        rawStringResponse += "'Meta Data':";
        rawStringResponse += "{'1. Information': 'info', '2. Symbol': 'AMZN', '3. Last Refreshed': '2021-01-04', '4. Time Zone': 'us'},";
        rawStringResponse += "'Monthly Adjusted Time Series':";
        rawStringResponse += "{";
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '1'},",
                decPrice.getDate(), decPrice.getClose(), decPrice.getAdjClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '1'},",
                novPrice.getDate(), novPrice.getClose(), novPrice.getAdjClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '1'}",
                octPrice.getDate(), octPrice.getClose(), octPrice.getAdjClose());
        rawStringResponse += "}"; // end Monthly Time Series
        rawStringResponse += "}"; // end
        String givenSymbol = "AMZN";

        AVStockHistoricalHttpApi avHttpApiDataSource = mock(AVStockHistoricalHttpApi.class);

        when(avHttpApiDataSource.getRawMonthlyAdjPriceData(givenSymbol)).thenReturn(rawStringResponse);
        AVStockHistoricalPriceProviderService avStockHistoricalPriceProviderService = new AVStockHistoricalPriceProviderService(avHttpApiDataSource);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .dateFrom(LocalDate.parse("2020-10-01"))
                .dateTo(LocalDate.parse("2020-11-01"))
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(params);

        verify(avHttpApiDataSource, times(1)).getRawMonthlyAdjPriceData("AMZN");
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().size(), equalTo(2));
        assertThat(stockPriceTimeSeries.getPrices().get(0).getDate(), is(novPrice.getDate()));
        assertThat(stockPriceTimeSeries.getPrices().get(1).getDate(), is(octPrice.getDate()));
    }

    @Test
    void WhenCallingGetStockMonthlyTimeSeriesResponseWithValidSymbolAndNonExistingDateRange_ShouldAVTimeSeriesMonthlyResponseHaveZeroPrices() {
        StockPrice decPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-31"))
                .close(3186.6300)
                .adjClose(186.6300)
                .build();
        StockPrice novPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(3150.6300)
                .adjClose(150.6300)
                .build();
        StockPrice octPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(3140.6300)
                .adjClose(140.6300)
                .build();
        String rawStringResponse = "{";
        rawStringResponse += "'Meta Data':";
        rawStringResponse += "{'1. Information': 'info', '2. Symbol': 'AMZN', '3. Last Refreshed': '2021-01-04', '4. Time Zone': 'us'},";
        rawStringResponse += "'Monthly Adjusted Time Series':";
        rawStringResponse += "{";
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '1'},",
                decPrice.getDate(), decPrice.getClose(), decPrice.getAdjClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '1'},",
                novPrice.getDate(), novPrice.getClose(), novPrice.getAdjClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. adjusted close': '%s', '6. volume': '1'}",
                octPrice.getDate(), octPrice.getClose(), octPrice.getAdjClose());
        rawStringResponse += "}"; // end Monthly Time Series
        rawStringResponse += "}"; // end
        String givenSymbol = "AMZN";

        AVStockHistoricalHttpApi avHttpApiDs = mock(AVStockHistoricalHttpApi.class);

        when(avHttpApiDs.getRawMonthlyAdjPriceData(givenSymbol)).thenReturn(rawStringResponse);
        AVStockHistoricalPriceProviderService service = new AVStockHistoricalPriceProviderService(avHttpApiDs);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .dateFrom(LocalDate.parse("2020-09-01"))
                .dateTo(LocalDate.parse("2020-08-01"))
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = service.getStockMonthlyHistoricalPrices(params);

        verify(avHttpApiDs, times(1)).getRawMonthlyAdjPriceData("AMZN");
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().size(), equalTo(0));
    }
}
