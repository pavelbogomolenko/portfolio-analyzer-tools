package io.github.pavelbogomolenko.stockhistoricalprice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AVStockTimeSeriesMonthlyIntTest {

    @Test
    @Disabled
    void GivenAVHttpApiStockDataFetcherImpl_WhenGetStockMonthlyTimeSeriesResponseWithSymbolIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() {
        AVHttpApiDataSource avHttpApiStockDataFetcher = new AVHttpApiDataSource();
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcher);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("GOOGL")
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("date"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("open"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("low"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("high"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("close"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("volume"));
    }

    @Test
    @Disabled
    void GivenAVFsApiStockDataFetcherImpl_WhenGetStockMonthlyTimeSeriesResponseWithSymbolIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() {
        AVFsApiDataSource avFsApiStockDataFetcher = new AVFsApiDataSource();
        AVStockHistoricalPriceProviderService avStockTimeSeriesServiceImpl = new AVStockHistoricalPriceProviderService(avFsApiStockDataFetcher);

        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .symbol("AMZN")
                .build();
        StockPriceTimeSeries stockPriceTimeSeries = avStockTimeSeriesServiceImpl.getStockMonthlyHistoricalPrices(params);

        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("info"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("symbol"));
        assertThat(stockPriceTimeSeries.getMeta(), hasProperty("timeZone"));

        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("date"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("open"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("low"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("high"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("close"));
        assertThat(stockPriceTimeSeries.getPrices().get(0), hasProperty("volume"));
    }
}
