package io.github.pavelbogomolenko.stockhistoricalprice;

import io.github.pavelbogomolenko.simplehttprequestwrapper.Request;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AVHttpApiDataSourceUnitTest {

    @Test
    void getStockMonthlyHistoricalPriceData_should_call_request_get_and_return_data() {
        Request request = mock(Request.class);

        String symbol = "aapl";
        String expectedUrl = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=%s&apikey=%s",
                symbol, System.getenv("AV_API_KEY"));
        String expectedReturn = "123";
        when(request.get(expectedUrl)).thenReturn(expectedReturn);

        AVHttpApiDataSource avHttpApiDataSource = new AVHttpApiDataSource(request);
        String actualResult = avHttpApiDataSource.getStockMonthlyHistoricalPriceData(symbol);

        assertThat(actualResult, is(expectedReturn));
    }
}
