package io.github.pavelbogomolenko.timeseries;


import io.github.pavelbogomolenko.portfolio.PortfolioHistoricalDatasetParams;
import io.github.pavelbogomolenko.portfolio.PortfolioHistoricalDataSetService;
import io.github.pavelbogomolenko.stockhistoricalprice.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSetsRelation {

    public double[][] varianceCovarianceMatrix(ArrayList<DataSet> dataSetList, int scale) {
        int matrixSize = dataSetList.size();
        int observedDataPointsNumber = dataSetList.get(0).getDataPoints().size() - 1;
        double[][] varCovarMatrix = new double[matrixSize][matrixSize];
        for (int col = 0; col < matrixSize; col++) {
            for(int row = 0; row < matrixSize; row++) {
                ArrayList<Double> growthRatesToAverageA = dataSetList.get(col).getGrowthRatesToAverage();
                ArrayList<Double> growthRatesToAverageB = dataSetList.get(row).getGrowthRatesToAverage();
                varCovarMatrix[col][row] = (dotProduct(growthRatesToAverageA, growthRatesToAverageB) / observedDataPointsNumber) * scale;
            }
        }
        return varCovarMatrix;
    }

    public double[][] annualizedVarCovarMatrix(ArrayList<DataSet> dataSetList) {
        return this.varianceCovarianceMatrix(dataSetList, 12);
    }

    public double[][] correlationMatrix(ArrayList<DataSet> dataSetList) {
        int matrixSize = dataSetList.size();
        double[][] correlationMatrix = new double[matrixSize][matrixSize];
        for (int col = 0; col < matrixSize; col++) {
            for( int row = 0; row < matrixSize; row++) {
                DataSet tsA = dataSetList.get(col);
                DataSet tsB = dataSetList.get(row);
                ArrayList<Double> growthRatesToAverageA = tsA.getGrowthRatesToAverage();
                ArrayList<Double> growthRatesToAverageB = tsB.getGrowthRatesToAverage();
                double divider = Math.sqrt(tsA.getGrowthRateToAverageSquared() * tsB.getGrowthRateToAverageSquared());
                correlationMatrix[col][row] = dotProduct(growthRatesToAverageA, growthRatesToAverageB) / divider;
            }
        }
        return correlationMatrix;
    }

    public void printVarianceCovarianceMatrix(ArrayList<DataSet> ts, ArrayList<String> headers) {
        printMatrixWithHeaders(varianceCovarianceMatrix(ts, 1), headers);
    }

    public void printCorrelationMatrix(ArrayList<DataSet> ts, ArrayList<String> headers) {
        printMatrixWithHeaders(correlationMatrix(ts), headers);
    }

    private void printMatrixWithHeaders(double[][] matrix, ArrayList<String> headers) {
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

    private double dotProduct(ArrayList<Double> a, ArrayList<Double> b) {
        double result = 0.0;
        for(int i = 0; i < a.size(); i++) {
            result += a.get(i) * b.get(i);
        }
        return result;
    }

    public static void main(String[] args) {
        AVStockHistoricalPriceProviderService stockHistoricalPriceProvider = AVStockHistoricalPriceProviderService.newBuilder()
                .buildWithFsDataSource();
        PortfolioHistoricalDataSetService portfolioHistoricalDataSetService = new PortfolioHistoricalDataSetService(stockHistoricalPriceProvider);
        ArrayList<String> symbols = new ArrayList<>(Arrays.asList(
                "AMZN",
                "EBAY",
                "AAPL",
                "ABC",
                "AMD",
                "GOOGL",
                "ABBV",
                "CDNS",
                "EA",
                "CSCO",
                "FANG",
                "GS",
                "BA",
                "MSFT",
                "BAC",
                "T"
        ));
        PortfolioHistoricalDatasetParams params = PortfolioHistoricalDatasetParams.newBuilder()
                .symbols(symbols)
                .dateFrom(YearMonth.parse("2013-02"))
                .dateTo(YearMonth.parse("2020-12"))
                .build();
        ArrayList<DataSet> dataSetList = portfolioHistoricalDataSetService.getDataSetListForStocksMonthlyClosePrices(params);

        for(DataSet tsMeasure: dataSetList) {
            System.out.println("Dates: " + Arrays.toString(tsMeasure.getDataPoints().stream().map(dataPoint -> dataPoint.getDate()).toArray()));
            System.out.println("Monthly Returns: " + Arrays.toString(tsMeasure.getGrowthRates().toArray()));
            System.out.println("Average Monthly Return: " + tsMeasure.getAverageGrowth());
            System.out.println("Average Annual Return: " + tsMeasure.getAverageGrowth() * 12);
            System.out.println("Monthly Returns Minus Average: " + tsMeasure.getGrowthRatesToAverage());
            System.out.println("Monthly Variance: " + tsMeasure.getVariance());
            System.out.println("Annual Variance: " + tsMeasure.getVariance() * 12);
            System.out.println("Monthly StdDev: " + tsMeasure.getStdDev());
        }

        DataSetsRelation dataSetsRelation = new DataSetsRelation();
        System.out.println("Portfolio Returns VAR-COVAR matrix");
        dataSetsRelation.printVarianceCovarianceMatrix(dataSetList, symbols);
        System.out.println("Portfolio Returns correlation matrix");
        dataSetsRelation.printCorrelationMatrix(dataSetList, symbols);
    }
}
