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
                .high(3272.0)
                .low(33144.0200)
                .volume(4411449)
                .build();
        StockPrice secondPrice = StockPrice.newBuilder()
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

        AVHttpApiDataSource avHttpApiStockDataFetcherImpl = mock(AVHttpApiDataSource.class);

        when(avHttpApiStockDataFetcherImpl.getStockMonthlyHistoricalPriceData(givenSymbol)).thenReturn(rawStringResponse);
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcherImpl);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        verify(avHttpApiStockDataFetcherImpl, times(1)).getStockMonthlyHistoricalPriceData("AMZN");
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().get(0).getDate(), is(firstPrice.getDate()));
        assertThat(stockPriceTimeSeries.getPrices().get(1).getDate(), is(secondPrice.getDate()));
    }

    @Test
    void shouldThrowWhenUnexpectedResponseHasBeenReturned() {
        String unexpectedRawResponse = "{'some': thing}";
        AVHttpApiDataSource avHttpApiStockDataFetcherImpl = mock(AVHttpApiDataSource.class);

        when(avHttpApiStockDataFetcherImpl.getStockMonthlyHistoricalPriceData("someSymbol")).thenReturn(unexpectedRawResponse);
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcherImpl);

        assertThrows(RuntimeException.class, () -> {
            StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                    .symbol("someSymbol")
                    .build();
            StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);
        });
    }

    @Test
    void WhenGetStockMonthlyTimeSeriesResponseWithNullSymbolIsCalled_ThenThrowException_symbol_should_not_be_empty() {
        assertThrows(NullPointerException.class, () -> {
            AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(mock(AVHttpApiDataSource.class));
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
                .build();
        StockPrice octPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(3140.6300)
                .build();
        String rawStringResponse = "{";
        rawStringResponse += "'Meta Data':";
        rawStringResponse += "{'1. Information': 'info', '2. Symbol': 'AMZN', '3. Last Refreshed': '2021-01-04', '4. Time Zone': 'us'},";
        rawStringResponse += "'Monthly Time Series':";
        rawStringResponse += "{";
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. volume': '1'},", decPrice.getDate(), decPrice.getClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. volume': '1'},", novPrice.getDate(), novPrice.getClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. volume': '1'}", octPrice.getDate(), octPrice.getClose());
        rawStringResponse += "}"; // end Monthly Time Series
        rawStringResponse += "}"; // end
        String givenSymbol = "AMZN";

        AVHttpApiDataSource avHttpApiStockDataFetcherImpl = mock(AVHttpApiDataSource.class);

        when(avHttpApiStockDataFetcherImpl.getStockMonthlyHistoricalPriceData(givenSymbol)).thenReturn(rawStringResponse);
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcherImpl);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .dateFrom(YearMonth.parse("2020-10"))
                .dateTo(YearMonth.parse("2020-11"))
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        verify(avHttpApiStockDataFetcherImpl, times(1)).getStockMonthlyHistoricalPriceData("AMZN");
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
                .build();
        StockPrice novPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-11-30"))
                .close(3150.6300)
                .build();
        StockPrice octPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-10-30"))
                .close(3140.6300)
                .build();
        String rawStringResponse = "{";
        rawStringResponse += "'Meta Data':";
        rawStringResponse += "{'1. Information': 'info', '2. Symbol': 'AMZN', '3. Last Refreshed': '2021-01-04', '4. Time Zone': 'us'},";
        rawStringResponse += "'Monthly Time Series':";
        rawStringResponse += "{";
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. volume': '1'},", decPrice.getDate(), decPrice.getClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. volume': '1'},", novPrice.getDate(), novPrice.getClose());
        rawStringResponse += String.format("'%s': {'1. open': '1', '2. high': '1', '3. low': '1', '4. close': '%s', '5. volume': '1'}", octPrice.getDate(), octPrice.getClose());
        rawStringResponse += "}"; // end Monthly Time Series
        rawStringResponse += "}"; // end
        String givenSymbol = "AMZN";

        AVHttpApiDataSource avHttpApiStockDataFetcherImpl = mock(AVHttpApiDataSource.class);

        when(avHttpApiStockDataFetcherImpl.getStockMonthlyHistoricalPriceData(givenSymbol)).thenReturn(rawStringResponse);
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcherImpl);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol(givenSymbol)
                .dateFrom(YearMonth.parse("2020-09"))
                .dateTo(YearMonth.parse("2020-08"))
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        verify(avHttpApiStockDataFetcherImpl, times(1)).getStockMonthlyHistoricalPriceData("AMZN");
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().size(), equalTo(0));
    }
}
