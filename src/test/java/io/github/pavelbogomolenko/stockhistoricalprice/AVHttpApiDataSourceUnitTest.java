package io.github.pavelbogomolenko.stockhistoricalprice;

import io.github.pavelbogomolenko.simplehttprequestwrapper.Request;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AVHttpApiDataSourceUnitTest {

    @Test
    void getStockMonthlyHistoricalAdjPriceData_should_call_request_get_and_return_data() {
        Request request = mock(Request.class);

        String symbol = "aapl";
        String expectedUrl = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=%s&apikey=%s",
                symbol, System.getenv("AV_API_KEY"));
        String expectedReturn = "123";
        when(request.get(expectedUrl)).thenReturn(expectedReturn);

        AVStockHistoricalHttpApi avHttpApiDataSource = new AVStockHistoricalHttpApi(request);
        String actualResult = avHttpApiDataSource.getRawMonthlyAdjPriceData(symbol);

        assertThat(actualResult, is(expectedReturn));
    }
}
