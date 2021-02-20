package io.github.pavelbogomolenko.stockhistoricalprice;

import io.github.pavelbogomolenko.simplehttprequestwrapper.Request;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class IexStockHistoricalPriceProviderServiceUnitTest {

    @Test
    void getStockDailyHistoricalPrices_should_return_StockTimeSeries_when_http_client_responds_with_data() {
        Request requestMock = mock(Request.class);

        StockPrice firstPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-03-25"))
                .close(120.39)
                .volume(80819203)
                .build();
        StockPrice secondPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-03-26"))
                .close(121.31)
                .volume(80819200)
                .build();

        String rawHttpResponseBody = "[";
        rawHttpResponseBody += "{'date': '2021-03-25', 'close': 120.39, 'volume': 80819203},";
        rawHttpResponseBody += "{'date': '2021-03-26', 'close': 121.31, 'volume': 80819200}";
        rawHttpResponseBody += "]";
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("aapl")
                .build();
        String url = String.format("https://cloud.iexapis.com/stable/stock/%s/chart/?token=%s", params.getSymbol(), System.getenv("IEX_CLOUD_API_KEY"));
        when(requestMock.get(url)).thenReturn(rawHttpResponseBody);

        IexStockHistoricalPriceProviderService iexStockHistoricalPriceProviderService = new IexStockHistoricalPriceProviderService(requestMock);
        StockPriceTimeSeries response = iexStockHistoricalPriceProviderService.getStockDailyHistoricalPrices(params);

        assertThat(response, is(notNullValue()));
        assertThat(response.getPrices().get(0).getDate(), is(firstPrice.getDate()));
        assertThat(response.getPrices().get(1).getDate(), is(secondPrice.getDate()));
    }

    @Test
    void getStockDailyHistoricalPricesWithRangeParam_should_return_StockTimeSeries_when_http_client_responds_with_data() {
        Request requestMock = mock(Request.class);

        StockPrice firstPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-03-25"))
                .close(120.39)
                .volume(80819203)
                .build();
        StockPrice secondPrice = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-03-26"))
                .close(121.31)
                .volume(80819200)
                .build();

        String rawHttpResponseBody = "[";
        rawHttpResponseBody += "{'date': '2021-03-25', 'close': 120.39, 'volume': 80819203},";
        rawHttpResponseBody += "{'date': '2021-03-26', 'close': 121.31, 'volume': 80819200}";
        rawHttpResponseBody += "]";
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("aapl")
                .range(StockHistoricalPriceRange.ONE_YEAR)
                .build();
        String url = String.format("https://cloud.iexapis.com/stable/stock/%s/chart/%s?token=%s",
                params.getSymbol(),
                params.getRange(),
                System.getenv("IEX_CLOUD_API_KEY"));
        when(requestMock.get(url)).thenReturn(rawHttpResponseBody);

        IexStockHistoricalPriceProviderService iexStockHistoricalPriceProviderService = new IexStockHistoricalPriceProviderService(requestMock);
        StockPriceTimeSeries response = iexStockHistoricalPriceProviderService.getStockDailyHistoricalPrices(params);

        assertThat(response, is(notNullValue()));
        assertThat(response.getPrices().get(0).getDate(), is(firstPrice.getDate()));
        assertThat(response.getPrices().get(1).getDate(), is(secondPrice.getDate()));
    }

    @Test
    void getStockMonthlyHistoricalPrices_should_return_StockTimeSeries_when_http_client_responds_with_data() {
        Request requestMock = mock(Request.class);

        StockPrice p1 = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-01-30"))
                .close(3186.6300)
                .volume(4411449)
                .build();
        StockPrice p2 = StockPrice.newBuilder()
                .date(LocalDate.parse("2021-01-02"))
                .close(3140.6300)
                .volume(4411449)
                .build();
        StockPrice p3 = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-31"))
                .close(3186.6300)
                .volume(4411449)
                .build();
        StockPrice p4 = StockPrice.newBuilder()
                .date(LocalDate.parse("2020-12-02"))
                .close(3186.6300)
                .volume(4411449)
                .build();

        String rawHttpResponseBody = "[";
        rawHttpResponseBody += String.format("{'date': '%s', 'close': %s, 'volume': %s},", p1.getDate(), p1.getClose(), p1.getVolume());
        rawHttpResponseBody += String.format("{'date': '%s', 'close': %s, 'volume': %s},", p2.getDate(), p2.getClose(), p2.getVolume());
        rawHttpResponseBody += String.format("{'date': '%s', 'close': %s, 'volume': %s},", p3.getDate(), p3.getClose(), p3.getVolume());
        rawHttpResponseBody += String.format("{'date': '%s', 'close': %s, 'volume': %s}", p4.getDate(), p4.getClose(), p4.getVolume());
        rawHttpResponseBody += "]";
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("aapl")
                .build();
        String url = String.format("https://cloud.iexapis.com/stable/stock/%s/chart/?token=%s", params.getSymbol(), System.getenv("IEX_CLOUD_API_KEY"));
        when(requestMock.get(url)).thenReturn(rawHttpResponseBody);

        IexStockHistoricalPriceProviderService iexStockHistoricalPriceProviderService = new IexStockHistoricalPriceProviderService(requestMock);
        StockPriceTimeSeries stockPriceTimeSeries = iexStockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(params);

        assertThat(stockPriceTimeSeries.getPrices().size(), is(equalTo(2)));
        assertThat(stockPriceTimeSeries.getPrices().get(0).getDate(), is(p1.getDate()));
        assertThat(stockPriceTimeSeries.getPrices().get(1).getDate(), is(p3.getDate()));
    }
}
