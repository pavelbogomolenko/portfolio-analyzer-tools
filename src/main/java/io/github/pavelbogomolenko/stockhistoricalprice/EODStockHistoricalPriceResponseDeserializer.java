package io.github.pavelbogomolenko.stockhistoricalprice;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

public class EODStockHistoricalPriceResponseDeserializer implements JsonDeserializer<StockPriceTimeSeries> {
    private static final Logger logger = LoggerFactory.getLogger(EODStockHistoricalPriceResponseDeserializer.class);
    private final String symbol;

    public EODStockHistoricalPriceResponseDeserializer(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public StockPriceTimeSeries deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String info = "stock historical data ts";
        String symbol = this.symbol;
        String timezone = "local";
        StockPriceMeta monthlyTimeSeriesMetaResponse = new StockPriceMeta(info, symbol, timezone);

        ArrayList<StockPrice> stockPriceTimeSeries = new ArrayList<>();

        JsonArray priceData = json.getAsJsonArray();
        for(JsonElement price: priceData) {
            JsonObject priceJsonObject = price.getAsJsonObject();
            LocalDate date = LocalDate.parse(priceJsonObject.get("date").getAsString());
            stockPriceTimeSeries.add(StockPrice.newBuilder()
                    .date(date)
                    .adjClose(priceJsonObject.get("adjusted_close").getAsDouble())
                    .volume(priceJsonObject.get("volume").getAsDouble())
                    .build());
        }

        return new StockPriceTimeSeries(monthlyTimeSeriesMetaResponse, stockPriceTimeSeries);
    }
}
