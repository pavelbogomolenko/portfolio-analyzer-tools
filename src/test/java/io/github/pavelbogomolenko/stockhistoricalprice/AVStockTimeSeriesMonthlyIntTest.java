package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AVStockTimeSeriesMonthlyIntTest {

    @Test
    @Disabled
    void GivenAVHttpApiStockDataFetcherImpl_WhenGetStockMonthlyTimeSeriesResponseWithSymbolIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() {
        AVStockHistoricalHttpApi avHttpApiStockDataFetcher = new AVStockHistoricalHttpApi();
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcher);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("GOOGL")
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        assertThat(stockPriceTimeSeries.meta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.meta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.meta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("date"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("open"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("low"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("high"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("close"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("volume"));
    }

    @Test
    @Disabled
    void GivenAVFsApiStockDataFetcherImpl_WhenGetStockMonthlyTimeSeriesResponseWithSymbolIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() {
        AVStockHistoricalFsApi avFsApiStockDataFetcher = new AVStockHistoricalFsApi();
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avFsApiStockDataFetcher);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("AMZN")
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        assertThat(stockPriceTimeSeries.meta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.meta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.meta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("date"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("open"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("low"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("high"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("close"));
        assertThat(stockPriceTimeSeries.prices().get(0), hasProperty("volume"));
    }
}
