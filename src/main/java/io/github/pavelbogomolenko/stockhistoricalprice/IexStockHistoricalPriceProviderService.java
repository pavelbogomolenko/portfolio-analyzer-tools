package io.github.pavelbogomolenko.stockhistoricalprice;

import com.google.gson.GsonBuilder;
import io.github.pavelbogomolenko.simplehttprequestwrapper.Request;

import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class IexStockHistoricalPriceProviderService implements StockHistoricalPriceProviderService {
    private final Request request;
    private final String iexCloudApiKey;

    public IexStockHistoricalPriceProviderService(Request request) {
        this.request = request;
        Objects.requireNonNull(System.getenv("IEX_CLOUD_API_KEY"));
        this.iexCloudApiKey = System.getenv("IEX_CLOUD_API_KEY");
    }

    @Override
    public StockPriceTimeSeries getStockDailyHistoricalPrices(StockHistoricalPriceParams params) {
        String url = String.format("https://cloud.iexapis.com/stable/stock/%s/chart/%s?token=%s",
                params.getSymbol(),
                params.getRange(),
                this.iexCloudApiKey);
        String response = this.request.get(url);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(StockPriceTimeSeries.class, new IexStockHistoricalPriceResponseDeserializer(params.getSymbol()));
        StockPriceTimeSeries stockTimeSeriesData = gsonBuilder.create().fromJson(response, StockPriceTimeSeries.class);
        return stockTimeSeriesData;
    }

    @Override
    public StockPriceTimeSeries getStockWeeklyHistoricalPrices(StockHistoricalPriceParams params) {
        throw new UnsupportedOperationException("getStockWeeklyHistoricalPrices is not implemented");
    }

    @Override
    public StockPriceTimeSeries getStockYearlyHistoricalPrices(StockHistoricalPriceParams params) {
        throw new UnsupportedOperationException("getStockYearlyHistoricalPrices is not implemented");
    }

    @Override
    public StockPriceTimeSeries getStockMonthlyHistoricalPrices(StockHistoricalPriceParams params) {
        StockPriceTimeSeries stockPriceDailyTimeSeriesData = this.getStockDailyHistoricalPrices(params);

        StockPrice firstPrice = stockPriceDailyTimeSeriesData.prices().get(0);
        Iterator<StockPrice> priceIterator = stockPriceDailyTimeSeriesData.prices().iterator();

        ArrayList<StockPrice> monthlyPrices = new ArrayList<>();
        StockPrice prevPrice = firstPrice;
        monthlyPrices.add(prevPrice);
        while (priceIterator.hasNext()) {
            StockPrice curPrice = priceIterator.next();
            Month curMonth = curPrice.getDate().getMonth();
            Month prevMonth = prevPrice.getDate().getMonth();
            if(!curMonth.equals(prevMonth)) {
                monthlyPrices.add(curPrice);
            }
            prevPrice = curPrice;
        }
        StockPriceMeta meta = stockPriceDailyTimeSeriesData.meta();
        return new StockPriceTimeSeries(meta, monthlyPrices);
    }
}
