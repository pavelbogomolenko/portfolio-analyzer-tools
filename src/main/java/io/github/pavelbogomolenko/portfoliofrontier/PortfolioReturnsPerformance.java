package io.github.pavelbogomolenko.portfoliofrontier;


import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class PortfolioReturnsPerformance {
    private final StockTimeSeriesDataProviderService stockTimeSeriesDataProviderService;
    private final ArrayList<MonthlyStockPriceReturnsPerformance> stockPriceReturnsPerformances;
    private final PortfolioReturnsPerformanceParams params;

    public PortfolioReturnsPerformance(StockTimeSeriesDataProviderService stockTimeSeriesDataProviderService, PortfolioReturnsPerformanceParams params)
            throws InterruptedException, IOException, URISyntaxException {
        this.stockTimeSeriesDataProviderService = stockTimeSeriesDataProviderService;
        this.params = params;
        this.stockPriceReturnsPerformances = calculateStockPriceReturnsPerformances(this.params);
    }

    public ArrayList<MonthlyStockPriceReturnsPerformance> getMonthlyStockPriceReturnsPerformances() {
        return stockPriceReturnsPerformances;
    }

    public double[][] getReturnsVarianceCovarianceMatrix() {
        ArrayList<MonthlyStockPriceReturnsPerformance> portfolio = getMonthlyStockPriceReturnsPerformances();
        int porfolioSize = portfolio.size();
        int porfolioStockReturnsSize = portfolio.get(0).getMonthlyReturnsToAverageDiff().size();
        double[][] varCovarMatrix = new double[porfolioSize][porfolioSize];
        for (int col = 0; col < porfolioSize; col++) {
            for(int row = 0; row < porfolioSize; row++) {
                varCovarMatrix[col][row] = calculateDotProduct(portfolio.get(col), portfolio.get(row)) / porfolioStockReturnsSize;
            }
        }
        return varCovarMatrix;
    }

    public void printVarianceCovarianceMatrix() {
        double[][] varCovarMatrix = getReturnsVarianceCovarianceMatrix();

        int maxSymbolLength = params.getSymbols().stream()
                .map(s -> s.length())
                .max(Integer::compare).get() + 2;

        for(int i = 0; i < maxSymbolLength; i++) {
            System.out.print(" ");
        }
        for(String s: params.getSymbols()) {
            System.out.printf("%s", s);
            int symbolLength = s.length();
            for(int i = 0; i < 9 - symbolLength; i++) {
                System.out.print(" ");
            }
        }
        System.out.print("\n");

        for (int col = 0; col < varCovarMatrix.length; col++) {
            StringBuilder colStr = new StringBuilder();
            colStr.append(params.getSymbols().get(col));
            for(int i = 0; i < maxSymbolLength - params.getSymbols().get(col).length(); i++) {
                colStr.append(" ");
            }
            for(int row = 0; row < varCovarMatrix.length; row++) {
                colStr.append(String.format("%.6f", varCovarMatrix[col][row]) + " ");
            }
            System.out.println(colStr.toString());
        }
    }

    private double calculateDotProduct(MonthlyStockPriceReturnsPerformance stockPerfA, MonthlyStockPriceReturnsPerformance stockPerfB) {
        double result = 0.0;
        ArrayList<Double> stockAMonthlyReturnsToAverageDiff = stockPerfA.getMonthlyReturnsToAverageDiff();
        ArrayList<Double> stockBMonthlyReturnsToAverageDiff = stockPerfB.getMonthlyReturnsToAverageDiff();
        for(int i = 0; i < stockAMonthlyReturnsToAverageDiff.size(); i++) {
            result += stockAMonthlyReturnsToAverageDiff.get(i) * stockBMonthlyReturnsToAverageDiff.get(i);
        }
        return result;
    }

    private ArrayList<MonthlyStockPriceReturnsPerformance> calculateStockPriceReturnsPerformances(PortfolioReturnsPerformanceParams params)
            throws InterruptedException, IOException, URISyntaxException {
        ArrayList<MonthlyStockPriceReturnsPerformance> result = new ArrayList<>();
        int yearDiffCount = params.getDateTo().getYear() - params.getDateFrom().getYear();
        int monthCount = Math.abs(params.getDateTo().getMonthValue() - params.getDateFrom().getMonthValue());
        int expectedItemsCount = monthCount;
        if(yearDiffCount > 0) {
            expectedItemsCount = yearDiffCount * expectedItemsCount;
        }

        for(String symbol: params.getSymbols()) {
            StockTimeSeriesServiceParams serviceParams = StockTimeSeriesServiceParams.newBuilder()
                    .symbol(symbol)
                    .dateFrom(params.getDateFrom())
                    .dateTo(params.getDateTo())
                    .build();

            StockMonthlyTimeSeriesData data = this.stockTimeSeriesDataProviderService.getStockMonthlyTimeSeriesData(serviceParams);
            if(data.getPrices().size() <= expectedItemsCount) {
                throw new IllegalArgumentException(String.format("Stock data for '%s' less than given time range", symbol));
            }
            MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(data);
            result.add(stockPerformance);
        }
        return result;
    }

    public static void main(String[] args)
            throws InterruptedException, IOException, URISyntaxException {
        AVHttpApiStockDataFetcherImpl avHttpApiStockDataFetcher = new AVHttpApiStockDataFetcherImpl();
        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesService = new AVStockTimeSeriesDataProviderServiceImpl(avHttpApiStockDataFetcher);
        PortfolioReturnsPerformanceParams params = PortfolioReturnsPerformanceParams.newBuilder()
                .symbol("AMZN")
                .symbol("MSFT")
                .symbol("GOOGL")
                .symbol("IBM")
//                .symbol("BMW.DE")
//                .symbol("FB")
                .symbol("CSCO")
                .dateFrom(LocalDate.parse("2015-01-01"))
                .dateTo(LocalDate.parse("2020-12-30"))
                .build();
        PortfolioReturnsPerformance pRP = new PortfolioReturnsPerformance(avStockTimeSeriesService, params);

        for(MonthlyStockPriceReturnsPerformance stockReturnsPerformance: pRP.getMonthlyStockPriceReturnsPerformances()) {
            System.out.println("Stock: " + stockReturnsPerformance.getStockSymbol());
            System.out.println("Monthly Returns: " + Arrays.toString(stockReturnsPerformance.getMonthlyReturns().toArray()));
            System.out.println("Average Monthly Return: " + stockReturnsPerformance.getAverageReturn());
            System.out.println("Average Annual Return: " + stockReturnsPerformance.getAverageAnnualReturn());
            System.out.println("Monthly Variance: " + stockReturnsPerformance.getVariance());
            System.out.println("Annual Variance: " + stockReturnsPerformance.getAnnualVariance());
            System.out.println("Monthly StdDev: " + stockReturnsPerformance.getStdDev());
        }

        pRP.printVarianceCovarianceMatrix();
    }
}
