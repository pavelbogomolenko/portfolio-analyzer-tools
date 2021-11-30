package io.github.pavelbogomolenko.timeseries;

import io.github.pavelbogomolenko.stockhistoricalprice.*;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DataSet {
    private final List<DataPoint> dataPoints;
    private final ArrayList<Double> growthRates;
    private final double averageGrowth;
    private final ArrayList<Double> growthRatesToAverage;
    private final double growthRateToAverageSquared;
    private final double variance;

    public DataSet(List<DataPoint> dataPoints) {
        if(!this.isSortedDesc(dataPoints)) {
            throw new IllegalArgumentException("TSDataPoints should be sorted in desc order");
        }
        this.dataPoints = dataPoints;
        this.growthRates = calculateGrowthRates();
        this.averageGrowth = this.growthRates.stream()
                .reduce(0.0, (acc, cur) -> acc + cur, Double::sum) / this.growthRates.size();
        this.growthRatesToAverage = this.growthRates.stream().map(e -> e - this.averageGrowth)
                .collect(Collectors.toCollection(ArrayList::new));
        this.growthRateToAverageSquared = this.growthRatesToAverage.stream()
                .reduce(0.0, (acc, cur) -> acc + Math.pow(cur, 2), Double::sum);
        this.variance = this.growthRateToAverageSquared / this.growthRates.size();
    }

    public ArrayList<Double> getGrowthRates() {
        return growthRates;
    }

    private ArrayList<Double> calculateGrowthRates() {
        ArrayList<Double> result = new ArrayList<>();
        for(int currentIndex = this.dataPoints.size() - 2; currentIndex >= 0; currentIndex--) {
            int prevIndex = currentIndex + 1;
            double growthRate = (this.dataPoints.get(currentIndex).getValue() - this.dataPoints.get(prevIndex).getValue()) /  this.dataPoints.get(prevIndex).getValue();
            result.add(growthRate);
        }
        return result;
    }

    public double getAverageGrowth() {
        return averageGrowth;
    }

    public double getVariance() {
        return variance;
    }

    public double getStdDev() {
        return Math.sqrt(this.variance);
    }

    public ArrayList<Double> getGrowthRatesToAverage() {
        return this.growthRatesToAverage;
    }

    public double getGrowthRateToAverageSquared() {
        return this.growthRateToAverageSquared;
    }

    private boolean isSortedDesc(Iterable<DataPoint> tsDataPoints) {
        Iterator<DataPoint> iterator = tsDataPoints.iterator();
        if(iterator.hasNext()) {
            DataPoint current = iterator.next();
            while (iterator.hasNext()) {
                DataPoint next = iterator.next();
                if(current.compareTo(next) < 1) {
                    return false;
                }
                current = next;
            }
        }
        return true;
    }

    public List<DataPoint> getDataPoints() {
        return this.dataPoints;
    }

    public double getAverage() {
        double expectedSum = 0;
        for(DataPoint dp: this.getDataPoints()) {
            expectedSum += dp.getValue();
        }
        return expectedSum / this.getDataPoints().size();
    }

    public double getNthAverage(int n) {
        if(n > this.dataPoints.size() || n <= 0) {
            throw new IndexOutOfBoundsException("n is out of the bound of data points");
        }
        double expectedSum = 0;
        for(int i = 0; i < n; i++) {
            expectedSum += this.dataPoints.get(i).getValue();
        }
        return expectedSum / n;
    }

    public DataSet sliceFromHead(int toIndex) {
        return new DataSet(this.dataPoints.subList(0, toIndex));
    }

    public double getMedian() {
        List<DataPoint> newDataPoints = new ArrayList<>(this.dataPoints);
        Collections.sort(newDataPoints, DataPoint.valueOrder());
        int median = (int) Math.floor(newDataPoints.size() / 2.0);
        if(this.dataPoints.size() % 2 == 0) {
            double firstMiddle = newDataPoints.get(median).getValue();
            double secondMiddle = newDataPoints.get(median - 1).getValue();
            return (firstMiddle + secondMiddle) / 2;
        } else {
            return newDataPoints.get(median).getValue();
        }
    }

    public static void main(String[] args) {
        AVHttpApiDataSource avHttpApiStockDataFetcher = new AVHttpApiDataSource();
        StockHistoricalPriceProviderService stockHistoricalPriceProviderService = new AVStockHistoricalPriceProviderService(avHttpApiStockDataFetcher);
        StockHistoricalPriceParams params = StockHistoricalPriceParams.newBuilder()
                .dateFrom(YearMonth.parse("2015-01-01"))
                .dateTo(YearMonth.parse("2020-12-30"))
                .symbol("MSFT")
                .build();
        StockPriceTimeSeries data = stockHistoricalPriceProviderService.getStockMonthlyHistoricalPrices(params);
        DataSet ds = ListToDataSet.convert(data.getPrices(), "date", "close");
        System.out.println("Stock: " + params.getSymbol());
        System.out.println("Returns: " + Arrays.toString(ds.getGrowthRates().toArray()));
        System.out.println("AverageReturn: " + ds.getAverageGrowth());
        System.out.println("ReturnsToAverageDiff: " + ds.getGrowthRatesToAverage());
        System.out.println("Variance: " + ds.getVariance());
        System.out.println("StdDev: " + ds.getStdDev());
    }
}
