package io.github.pavelbogomolenko.timeseries;


import io.github.pavelbogomolenko.stockhistoricalpricedataset.StockHistoricalPriceDataSetParam;
import io.github.pavelbogomolenko.stockhistoricalprice.*;
import io.github.pavelbogomolenko.stockhistoricalpricedataset.StockPriceHistoricalDataSetService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSetsRelation {

    public static double[][] varianceCovarianceMatrix(List<DataSet> dataSetList, int scale) {
        int matrixSize = dataSetList.size();
        int observedDataPointsNumber = dataSetList.get(0).getDataPoints().size() - 1;
        double[][] varCovarMatrix = new double[matrixSize][matrixSize];
        for (int col = 0; col < matrixSize; col++) {
            for(int row = 0; row < matrixSize; row++) {
                List<Double> growthRatesToAverageA = dataSetList.get(col).getGrowthRatesToAverage();
                List<Double> growthRatesToAverageB = dataSetList.get(row).getGrowthRatesToAverage();
                varCovarMatrix[col][row] =
                        (dotProduct(growthRatesToAverageA, growthRatesToAverageB) / observedDataPointsNumber) * scale;
            }
        }
        return varCovarMatrix;
    }

    public static double[][] annualizedVarCovarMatrix(List<DataSet> dataSetList) {
        return varianceCovarianceMatrix(dataSetList, 12);
    }

    public static double[][] correlationMatrix(List<DataSet> dataSetList) {
        int matrixSize = dataSetList.size();
        double[][] correlationMatrix = new double[matrixSize][matrixSize];
        for (int col = 0; col < matrixSize; col++) {
            for(int row = 0; row < matrixSize; row++) {
                DataSet tsA = dataSetList.get(col);
                DataSet tsB = dataSetList.get(row);
                List<Double> growthRatesToAverageA = tsA.getGrowthRatesToAverage();
                List<Double> growthRatesToAverageB = tsB.getGrowthRatesToAverage();
                double divider = Math.sqrt(tsA.getGrowthRateToAverageSquared() * tsB.getGrowthRateToAverageSquared());
                correlationMatrix[col][row] = dotProduct(growthRatesToAverageA, growthRatesToAverageB) / divider;
            }
        }
        return correlationMatrix;
    }

    public static void printVarianceCovarianceMatrix(List<DataSet> ts, List<String> headers) {
        printMatrixWithHeaders(varianceCovarianceMatrix(ts, 1), headers);
    }

    public static void printCorrelationMatrix(List<DataSet> ts, List<String> headers) {
        printMatrixWithHeaders(correlationMatrix(ts), headers);
    }

    private static void printMatrixWithHeaders(double[][] matrix, List<String> headers) {
        int maxHeaderLength = headers.stream()
                .map(String::length)
                .max(Integer::compare).get() + 2;

        for(int i = 0; i < maxHeaderLength; i++) {
            System.out.print(" ");
        }
        for(String h: headers) {
            System.out.printf("%s", h);
            int headerLength = h.length();
            for(int i = 0; i < 9 - headerLength; i++) {
                System.out.print(" ");
            }
        }
        System.out.print("\n");

        for (int col = 0; col < matrix.length; col++) {
            StringBuilder colStr = new StringBuilder();
            colStr.append(headers.get(col));
            colStr.append(" ".repeat(Math.max(0, maxHeaderLength - headers.get(col).length())));
            for(int row = 0; row < matrix.length; row++) {
                colStr.append(String.format("%.6f", matrix[col][row]) + " ");
            }
            System.out.println(colStr.toString());
        }
    }

    private static double dotProduct(List<Double> a, List<Double> b) {
        double result = 0.0;
        for(int i = 0; i < a.size(); i++) {
            result += a.get(i) * b.get(i);
        }
        return result;
    }

    public static void main(String[] args) {
        EODStockHistoricalPriceProviderService stockHistoricalPriceProvider = EODStockHistoricalPriceProviderService.newBuilder()
                .buildWithFsDataSource();
        StockHistoricalPriceDataProviderFactory serviceFactory = new StockHistoricalPriceDataProviderFactory(stockHistoricalPriceProvider);
        StockPriceHistoricalDataSetService stockPriceHistoricalDataSetService = new StockPriceHistoricalDataSetService(serviceFactory);
        ArrayList<String> symbols = new ArrayList<>(Arrays.asList(
                "FB",
                "BABA",
                "PDD"
        ));
        StockHistoricalPriceRangeParam range = StockHistoricalPriceRangeParam.fromString("5y");
        StockHistoricalPriceDataSetParam params = StockHistoricalPriceDataSetParam.newBuilder()
                .symbols(symbols)
                .range(range)
                .period(StockHistoricalPricePeriodParam.MONTHLY)
                .build();
        List<DataSet> dataSetList = stockPriceHistoricalDataSetService.getMonthlyAdjustedDataSetListForProperty(params);
        for(DataSet tsMeasure: dataSetList) {
            System.out.println("Dates: " + Arrays.toString(tsMeasure.getDataPoints().stream().map(DataPoint::getDate).toArray()));
            System.out.println("Monthly Returns: " + Arrays.toString(tsMeasure.getGrowthRates().toArray()));
            System.out.println("Average Monthly Return: " + tsMeasure.getAverageGrowth());
            System.out.println("Average Annual Return: " + tsMeasure.getAverageGrowth() * 12);
            System.out.println("Monthly Returns Minus Average: " + tsMeasure.getGrowthRatesToAverage());
            System.out.println("Monthly Variance: " + tsMeasure.getVariance());
            System.out.println("Annual Variance: " + tsMeasure.getVariance() * 12);
            System.out.println("Monthly StdDev: " + tsMeasure.getStdDev());
            System.out.println("Items count: " + tsMeasure.getDataPoints().size());
        }

        System.out.println("Portfolio Returns VAR-COVAR matrix");
        DataSetsRelation.printVarianceCovarianceMatrix(dataSetList, symbols);
        System.out.println("Portfolio Returns correlation matrix");
        DataSetsRelation.printCorrelationMatrix(dataSetList, symbols);
    }
}
