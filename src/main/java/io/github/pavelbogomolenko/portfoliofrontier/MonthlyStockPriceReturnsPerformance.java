package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MonthlyStockPriceReturnsPerformance {
    private final StockMonthlyTimeSeriesResponse stockMonthlyTimeSeriesResponse;
    private final ArrayList<Double> closePriceGrowthRates;

    public MonthlyStockPriceReturnsPerformance(StockTimeSeriesService stockTimeSeriesService, StockTimeSeriesServiceParams params)
            throws InterruptedException, IOException, URISyntaxException {
        this.stockMonthlyTimeSeriesResponse = stockTimeSeriesService.getStockMonthlyTimeSeriesResponse(params);
        this.closePriceGrowthRates = calculateClosePriceGrowthRates();
    }

    public ArrayList<Double> getClosePriceGrowthRates() {
        return closePriceGrowthRates;
    }

    private ArrayList<Double> calculateClosePriceGrowthRates() {
        ArrayList<Double> result = new ArrayList<>();
        ArrayList<StockPriceTimeSeries> priceTimeSeries = this.stockMonthlyTimeSeriesResponse.getPrices();
        for(int currentIndex = priceTimeSeries.size() - 2; currentIndex >= 0; currentIndex--) {
            int prevIndex = currentIndex + 1;
            double growthRate = (priceTimeSeries.get(currentIndex).getClose() - priceTimeSeries.get(prevIndex).getClose()) /  priceTimeSeries.get(prevIndex).getClose();
            result.add(growthRate);
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        StockTimeSeriesService stockTimeSeriesService = new AVStockTimeSeriesService();
        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .dateFrom(LocalDate.parse("2015-01-01"))
                .dateTo(LocalDate.parse("2020-12-30"))
                .symbol("MSFT")
                .build();
        MonthlyStockPriceReturnsPerformance monthlyStockPerf = new MonthlyStockPriceReturnsPerformance(stockTimeSeriesService, params);
        System.out.println(Arrays.toString(monthlyStockPerf.getClosePriceGrowthRates().toArray()));
    }
}
