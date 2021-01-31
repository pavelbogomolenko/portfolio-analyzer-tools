package io.github.pavelbogomolenko.portfoliofrontier;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AVStockTimeSeriesMonthlyIntTest {

    @Test
    void GivenAVHttpApiStockDataFetcherImpl_WhenGetStockMonthlyTimeSeriesResponseWithSymbolIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() {
        AVHttpApiStockDataFetcherImpl avHttpApiStockDataFetcher = new AVHttpApiStockDataFetcherImpl();
        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesServiceImpl = new AVStockTimeSeriesDataProviderServiceImpl(avHttpApiStockDataFetcher);

        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .symbol("GOOGL")
                .build();
        StockMonthlyTimeSeriesData stockMonthlyTimeSeriesData = avStockTimeSeriesServiceImpl.getStockMonthlyTimeSeriesData(params);

        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("info"));
        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("symbol"));
        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("timeZone"));

        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("date"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("open"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("low"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("high"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("close"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("volume"));
    }

    @Test
    void GivenAVFsApiStockDataFetcherImpl_WhenGetStockMonthlyTimeSeriesResponseWithSymbolIsCalled_ThenReturnAVTimeSeriesMonthlyResponse() {
        AVFsApiStockDataFetcherImpl avFsApiStockDataFetcher = new AVFsApiStockDataFetcherImpl();
        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesServiceImpl = new AVStockTimeSeriesDataProviderServiceImpl(avFsApiStockDataFetcher);

        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .symbol("AMZN")
                .build();
        StockMonthlyTimeSeriesData stockMonthlyTimeSeriesData = avStockTimeSeriesServiceImpl.getStockMonthlyTimeSeriesData(params);

        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("info"));
        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("symbol"));
        assertThat(stockMonthlyTimeSeriesData.getMeta(), hasProperty("timeZone"));

        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("date"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("open"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("low"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("high"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("close"));
        assertThat(stockMonthlyTimeSeriesData.getPrices().get(0), hasProperty("volume"));
    }
}
