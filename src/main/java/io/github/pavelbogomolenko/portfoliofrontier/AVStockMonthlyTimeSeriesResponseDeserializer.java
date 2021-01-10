package io.github.pavelbogomolenko.portfoliofrontier;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Set;

public class AVStockMonthlyTimeSeriesResponseDeserializer implements JsonDeserializer<AVStockMonthlyTimeSeriesResponse> {
    @Override
    public AVStockMonthlyTimeSeriesResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject responseJsonObject = json.getAsJsonObject();
        JsonObject metaJsonObject = responseJsonObject.get("Meta Data").getAsJsonObject();

        String info = metaJsonObject.get("1. Information").getAsString();
        String symbol = metaJsonObject.get("2. Symbol").getAsString();
        String timezone = metaJsonObject.get("4. Time Zone").getAsString();
        StockMetaTimeSeries monthlyTimeSeriesMetaResponse = new StockMetaTimeSeries(info, symbol, timezone);

        JsonObject stockPriceJsonObject = responseJsonObject.get("Monthly Time Series").getAsJsonObject();

        Set<String> allKeys = stockPriceJsonObject.keySet();
        StockPriceTimeSeries[] stockPriceTimeSeries = new StockPriceTimeSeries[allKeys.size()];
        int counter = 0;
        for(String dateKey: allKeys) {
            LocalDate date = LocalDate.parse(dateKey);;
            double openPrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("1. open").getAsDouble();
            double highPrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("2. high").getAsDouble();
            double lowPrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("3. low").getAsDouble();
            double closePrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("4. close").getAsDouble();
            double volume = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("5. volume").getAsDouble();
            stockPriceTimeSeries[counter++] = StockPriceTimeSeries.newBuilder()
                    .date(date)
                    .open(openPrice)
                    .high(highPrice)
                    .low(lowPrice)
                    .close(closePrice)
                    .volume(volume)
                    .build();
        }

        return new AVStockMonthlyTimeSeriesResponse(monthlyTimeSeriesMetaResponse, stockPriceTimeSeries);
    }
}
