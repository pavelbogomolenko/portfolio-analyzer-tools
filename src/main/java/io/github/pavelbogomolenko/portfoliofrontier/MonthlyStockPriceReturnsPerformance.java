package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MonthlyStockPriceReturnsPerformance {
    private final StockMonthlyTimeSeriesResponse stockMonthlyTimeSeriesResponse;
    private final ArrayList<Double> monthlyReturns;
    private final double averageReturn;
    private final double averageAnnualReturn;

    public MonthlyStockPriceReturnsPerformance(StockTimeSeriesService stockTimeSeriesService, StockTimeSeriesServiceParams params)
            throws InterruptedException, IOException, URISyntaxException {
        this.stockMonthlyTimeSeriesResponse = stockTimeSeriesService.getStockMonthlyTimeSeriesResponse(params);
        this.monthlyReturns = calculateMonthlyReturns();
        this.averageReturn = this.monthlyReturns.stream()
                .reduce(0.0, (acc, cur) -> acc + cur, Double::sum) / this.monthlyReturns.size();
        this.averageAnnualReturn = this.averageReturn * 12;
    }

    public ArrayList<Double> getMonthlyReturns() {
        return monthlyReturns;
    }

    private ArrayList<Double> calculateMonthlyReturns() {
        ArrayList<Double> result = new ArrayList<>();
        ArrayList<StockPriceTimeSeries> priceTimeSeries = this.stockMonthlyTimeSeriesResponse.getPrices();
        for(int currentIndex = priceTimeSeries.size() - 2; currentIndex >= 0; currentIndex--) {
            int prevIndex = currentIndex + 1;
            double growthRate = (priceTimeSeries.get(currentIndex).getClose() - priceTimeSeries.get(prevIndex).getClose()) /  priceTimeSeries.get(prevIndex).getClose();
            result.add(growthRate);
        }
        return result;
    }

    public double getAverageReturn() {
        return averageReturn;
    }

    public double getAverageAnnualReturn() {
        return averageAnnualReturn;
    }

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        StockTimeSeriesService stockTimeSeriesService = new AVStockTimeSeriesService();
        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .dateFrom(LocalDate.parse("2015-01-01"))
                .dateTo(LocalDate.parse("2020-12-30"))
                .symbol("MSFT")
                .build();
        MonthlyStockPriceReturnsPerformance monthlyStockPerf = new MonthlyStockPriceReturnsPerformance(stockTimeSeriesService, params);
        System.out.println("Stock: " + params.getSymbol());
        System.out.println("MonthlyReturns: " + Arrays.toString(monthlyStockPerf.getMonthlyReturns().toArray()));
        System.out.println("AverageReturn: " +monthlyStockPerf.getAverageReturn());
        System.out.println("AverageAnnualReturn: " +monthlyStockPerf.getAverageAnnualReturn());
    }
}
