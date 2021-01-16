package io.github.pavelbogomolenko.portfoliofrontier;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class AVStockMonthlyTimeSeriesResponseDeserializer implements JsonDeserializer<StockMonthlyTimeSeriesData> {
    private static final Logger logger = LoggerFactory.getLogger(AVStockMonthlyTimeSeriesResponseDeserializer.class);

    @Override
    public StockMonthlyTimeSeriesData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject responseJsonObject = json.getAsJsonObject();
        JsonElement metaJsonElement = responseJsonObject.get("Meta Data");
        if(Objects.isNull(metaJsonElement)) {
            logger.error(responseJsonObject.toString());
            throw new RuntimeException("AV API has returned unexpected response");
        }
        JsonObject metaJsonObject = metaJsonElement.getAsJsonObject();

        String info = metaJsonObject.get("1. Information").getAsString();
        String symbol = metaJsonObject.get("2. Symbol").getAsString();
        String timezone = metaJsonObject.get("4. Time Zone").getAsString();
        StockMetaTimeSeries monthlyTimeSeriesMetaResponse = new StockMetaTimeSeries(info, symbol, timezone);

        JsonObject stockPriceJsonObject = responseJsonObject.get("Monthly Time Series").getAsJsonObject();

        Set<String> allKeys = stockPriceJsonObject.keySet();
        ArrayList<StockPriceTimeSeries> stockPriceTimeSeries = new ArrayList<>();
        for(String dateKey: allKeys) {
            LocalDate date = LocalDate.parse(dateKey);
            double openPrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("1. open").getAsDouble();
            double highPrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("2. high").getAsDouble();
            double lowPrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("3. low").getAsDouble();
            double closePrice = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("4. close").getAsDouble();
            double volume = stockPriceJsonObject.get(dateKey).getAsJsonObject().get("5. volume").getAsDouble();
            StockPriceTimeSeries serie = StockPriceTimeSeries.newBuilder()
                    .date(date)
                    .open(openPrice)
                    .high(highPrice)
                    .low(lowPrice)
                    .close(closePrice)
                    .volume(volume)
                    .build();
            stockPriceTimeSeries.add(serie);
        }

        return new StockMonthlyTimeSeriesData(monthlyTimeSeriesMetaResponse, stockPriceTimeSeries);
    }
}
