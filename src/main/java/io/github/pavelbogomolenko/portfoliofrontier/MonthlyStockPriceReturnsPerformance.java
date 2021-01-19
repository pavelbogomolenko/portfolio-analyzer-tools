package io.github.pavelbogomolenko.portfoliofrontier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MonthlyStockPriceReturnsPerformance {
    private final StockMonthlyTimeSeriesData stockMonthlyTimeSeriesData;
    private final ArrayList<Double> monthlyReturns;
    private final double averageReturn;
    private final ArrayList<Double> monthlyReturnsToAverageDiff;
    private final double variance;

    public MonthlyStockPriceReturnsPerformance(StockMonthlyTimeSeriesData data) {
        this.stockMonthlyTimeSeriesData = data;
        this.monthlyReturns = calculateMonthlyReturns();
        this.averageReturn = this.monthlyReturns.stream()
                .reduce(0.0, (acc, cur) -> acc + cur, Double::sum) / this.monthlyReturns.size();
        this.monthlyReturnsToAverageDiff = this.monthlyReturns.stream().map(e -> e - this.averageReturn)
                .collect(Collectors.toCollection(ArrayList::new));
        this.variance = this.monthlyReturnsToAverageDiff.stream()
                .reduce(0.0, (acc, cur) -> acc + Math.pow(cur, 2), Double::sum) / this.monthlyReturns.size();
    }

    public ArrayList<Double> getMonthlyReturns() {
        return monthlyReturns;
    }

    private ArrayList<Double> calculateMonthlyReturns() {
        ArrayList<Double> result = new ArrayList<>();
        ArrayList<StockPriceTimeSeries> priceTimeSeries = this.stockMonthlyTimeSeriesData.getPrices();
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
        return averageReturn * 12;
    }

    public double getVariance() {
        return variance;
    }

    public double getAnnualVariance() {
        return variance * 12;
    }

    public String getStockSymbol() {
        return this.stockMonthlyTimeSeriesData.getMeta().getSymbol();
    }

    public double getStdDev() {
        return Math.sqrt(this.variance);
    }

    public ArrayList<Double> getMonthlyReturnsToAverageDiff() {
        return this.monthlyReturnsToAverageDiff;
    }

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
        AVHttpApiStockDataFetcherImpl avHttpApiStockDataFetcher = new AVHttpApiStockDataFetcherImpl();
        StockTimeSeriesDataProviderService stockTimeSeriesDataProviderService = new AVStockTimeSeriesDataProviderServiceImpl(avHttpApiStockDataFetcher);
        StockTimeSeriesServiceParams params = StockTimeSeriesServiceParams.newBuilder()
                .dateFrom(LocalDate.parse("2015-01-01"))
                .dateTo(LocalDate.parse("2020-12-30"))
                .symbol("MSFT")
                .build();
        StockMonthlyTimeSeriesData data = stockTimeSeriesDataProviderService.getStockMonthlyTimeSeriesData(params);
        MonthlyStockPriceReturnsPerformance monthlyStockPerf = new MonthlyStockPriceReturnsPerformance(data);
        System.out.println("Stock: " + params.getSymbol());
        System.out.println("MonthlyReturns: " + Arrays.toString(monthlyStockPerf.getMonthlyReturns().toArray()));
        System.out.println("AverageReturn: " + monthlyStockPerf.getAverageReturn());
        System.out.println("AverageAnnualReturn: " + monthlyStockPerf.getAverageAnnualReturn());
        System.out.println("MonthlyReturnsToAverageDiff: " + monthlyStockPerf.getMonthlyReturnsToAverageDiff());
        System.out.println("Variance: " + monthlyStockPerf.getVariance());
        System.out.println("StdDev: " + monthlyStockPerf.getStdDev());
    }
}
