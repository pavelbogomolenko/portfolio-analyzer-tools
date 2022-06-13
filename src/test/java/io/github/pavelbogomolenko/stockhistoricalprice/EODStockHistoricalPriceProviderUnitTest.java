package io.github.pavelbogomolenko.stockhistoricalprice;

import net.andreinc.mockneat.abstraction.MockUnitString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static net.andreinc.mockneat.unit.text.Formatter.fmt;
import static net.andreinc.mockneat.unit.seq.Seq.fromIterable;
import static net.andreinc.mockneat.unit.types.Floats.floats;
import static net.andreinc.mockneat.unit.types.Longs.longs;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class EODStockHistoricalPriceProviderUnitTest {

    @Test
    void getStockDailyHistoricalTimeSeriesDataBySymbol() {
        String symbol = "AAPL";
        String apiResponse = "[{\n" +
                "    \"date\": \"2022-03-23\",\n" +
                "    \"open\": 115.7,\n" +
                "    \"high\": 124.1112,\n" +
                "    \"low\": 112.68,\n" +
                "    \"close\": 117.24,\n" +
                "    \"adjusted_close\": 117.24,\n" +
                "    \"volume\": 78886244\n" +
                "  },\n" +
                "  {\n" +
                "    \"date\": \"2022-03-22\",\n" +
                "    \"open\": 114.01,\n" +
                "    \"high\": 118.24,\n" +
                "    \"low\": 112.37,\n" +
                "    \"close\": 114.99,\n" +
                "    \"adjusted_close\": 114.99,\n" +
                "    \"volume\": 88193400\n" +
                "  }]";
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawDailyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .build();
        StockPriceTimeSeries result = sut.getStockDailyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo( symbol)));
        assertThat(result.prices().size(), is(equalTo(2)));
    }

    @ParameterizedTest
    @CsvSource({
            "7, 'SEVEN_DAYS'",
            "30, 'THIRTY_DAYS'",
            "90, 'NINETY_DAYS'",
            "180, 'HUNDRED_EIGHTY_DAYS'",
            "360, 'THREE_HUNDRED_SIXTY_DAYS'",
            "7, 'ONE_WEEK'",
            "30, 'ONE_MONTH'"
    })
    void getStockDailyHistoricalTimeSeriesDataBySymbolFilteredByDifferentRanges(int daysCount, String enumValue) {
        String symbol = "BABA";
        String apiResponse = this.buildFsApiResponseForDateUnit(365, ChronoUnit.DAYS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawDailyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .range(StockHistoricalPriceRangeParam.valueOf(enumValue))
                .period(StockHistoricalPricePeriodParam.DAILY)
                .build();
        StockPriceTimeSeries result = sut.getStockDailyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(daysCount)));
    }

    @Test
    void getStockWeeklyHistoricalTimeSeriesDataBy7dDateRange() {
        String symbol = "BABA";
        String apiResponse = this.buildFsApiResponseForDateUnit(14, ChronoUnit.DAYS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawWeeklyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .range(StockHistoricalPriceRangeParam.SEVEN_DAYS)
                .period(StockHistoricalPricePeriodParam.WEEKLY)
                .build();
        StockPriceTimeSeries result = sut.getStockWeeklyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(1)));
    }

    @Test
    void getStockWeeklyHistoricalTimeSeriesDataBy14dDateRange() {
        String symbol = "BABA";
        String apiResponse = this.buildFsApiResponseForDateUnit(14, ChronoUnit.DAYS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawWeeklyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .range(StockHistoricalPriceRangeParam.FOURTEEN_DAYS)
                .period(StockHistoricalPricePeriodParam.WEEKLY)
                .build();
        StockPriceTimeSeries result = sut.getStockWeeklyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(2)));
    }

    @Test
    void getStockMonthlyHistoricalTimeSeriesDataBy90dRangeFilter() {
        String symbol = "BABA";
        String apiResponse = this.buildFsApiResponseForDateUnit(12, ChronoUnit.MONTHS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawMonthlyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .range(StockHistoricalPriceRangeParam.NINETY_DAYS)
                .period(StockHistoricalPricePeriodParam.MONTHLY)
                .build();
        StockPriceTimeSeries result = sut.getStockMonthlyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(3)));
    }

    @Test
    void getStockMonthlyHistoricalTimeSeriesDataBy30dRangeFilter() {
        String symbol = "BABA";
        String apiResponse = this.buildFsApiResponseForDateUnit(12, ChronoUnit.MONTHS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawMonthlyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .range(StockHistoricalPriceRangeParam.THIRTY_DAYS)
                .period(StockHistoricalPricePeriodParam.MONTHLY)
                .build();
        StockPriceTimeSeries result = sut.getStockMonthlyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(1)));
    }

    @Test
    void getStockMonthlyHistoricalTimeSeriesDataBy14dRangeFilter() {
        String symbol = "BABA";
        String apiResponse = this.buildFsApiResponseForDateUnit(12, ChronoUnit.MONTHS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawMonthlyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .range(StockHistoricalPriceRangeParam.FOURTEEN_DAYS)
                .period(StockHistoricalPricePeriodParam.MONTHLY)
                .build();
        StockPriceTimeSeries result = sut.getStockMonthlyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(0)));
    }

    @Test
    void getStockMonthlyHistoricalTimeSeriesDataBySymbol() {
        String symbol = "AAPL";
        String apiResponse = this.buildFsApiResponseForDateUnit(2, ChronoUnit.MONTHS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawMonthlyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .build();
        StockPriceTimeSeries result = sut.getStockMonthlyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo( symbol)));
        assertThat(result.prices().size(), is(equalTo(2)));
    }

    @Test
    void getStockWeeklyHistoricalTimeSeriesDataBySymbol() {
        String symbol = "AAPL";
        String apiResponse = this.buildFsApiResponseForDateUnit(2, ChronoUnit.WEEKS);
        IStockHistoricalApi eodHistoricalPriceFsApi = mock(EODHistoricalPriceFsApi.class);
        when(eodHistoricalPriceFsApi.getRawWeeklyAdjPriceData(symbol)).thenReturn(apiResponse);

        EODStockHistoricalPriceProviderService sut = new EODStockHistoricalPriceProviderService(eodHistoricalPriceFsApi);

        StockHistoricalPriceParams params = StockHistoricalPriceParams
                .newBuilder()
                .symbol(symbol)
                .build();
        StockPriceTimeSeries result = sut.getStockWeeklyHistoricalPrices(params);
        assertThat(result.meta().getSymbol(), is(equalTo(symbol)));
        assertThat(result.prices().size(), is(equalTo(2)));
    }

    private String buildFsApiResponseForDateUnit(int count, ChronoUnit chronoUnit) {
        LocalDate to = LocalDate.now();
        List<String> dates = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            dates.add(to.minus(i, chronoUnit).toString());
        }
        String template = "{\"date\": \"#{date}\", \"adjusted_close\": #{adjClose}, \"volume\": #{volume}}";
        MockUnitString fsApiResponseGenerator = fmt(template)
                .param("date", fromIterable(dates))
                .param("adjClose", floats().range(110.0f, 120.0f))
                .param("volume", longs().range(86209300L, 156235319L));

        return "[" + fsApiResponseGenerator.accumulate(count, ",\n").get() + "]";
    }
}
