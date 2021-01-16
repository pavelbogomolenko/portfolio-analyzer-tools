package io.github.pavelbogomolenko.portfoliofrontier;


import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PortfolioReturnsPerformance {
    private final StockTimeSeriesDataProviderService stockTimeSeriesDataProviderService;

    public PortfolioReturnsPerformance(StockTimeSeriesDataProviderService stockTimeSeriesDataProviderService) {
        this.stockTimeSeriesDataProviderService = stockTimeSeriesDataProviderService;
    }

    public ArrayList<MonthlyStockPriceReturnsPerformance> getMonthlyStockPriceReturnsPerformances(PortfolioReturnsPerformanceParams params)
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
                throw new IllegalArgumentException("Stock data for '%s' less than given time range");
            }
            MonthlyStockPriceReturnsPerformance stockPerformance = new MonthlyStockPriceReturnsPerformance(data);
            result.add(stockPerformance);
        }
        return result;
    }

    public double[][] getReturnsVarianceCovarianceMatrix(PortfolioReturnsPerformanceParams params)
            throws InterruptedException, IOException, URISyntaxException {
        ArrayList<MonthlyStockPriceReturnsPerformance> portfolioPerf = getMonthlyStockPriceReturnsPerformances(params);

        int porfolioSize = portfolioPerf.size();
        int porfolioStockReturnsSize = portfolioPerf.get(0).getMonthlyReturnsToAverageDiff().size();
        double[][] varCovarMatrix = new double[porfolioSize][porfolioSize];
        for (int col = 0; col < porfolioSize; col++) {
            for(int row = 0; row < porfolioSize; row++) {
                varCovarMatrix[col][row] = calculateDotProduct(portfolioPerf.get(col), portfolioPerf.get(row)) / porfolioStockReturnsSize;
            }
        }
        return varCovarMatrix;
    }

    public void printVarianceCovarianceMatrix(PortfolioReturnsPerformanceParams params)
            throws InterruptedException, IOException, URISyntaxException {
        double[][] varCovarMatrix = getReturnsVarianceCovarianceMatrix(params);

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

    public static void main(String[] args)
            throws InterruptedException, IOException, URISyntaxException {
        AVStockTimeSeriesDataProviderServiceImpl avStockTimeSeriesService = new AVStockTimeSeriesDataProviderServiceImpl();
        PortfolioReturnsPerformance pRP = new PortfolioReturnsPerformance(avStockTimeSeriesService);

        PortfolioReturnsPerformanceParams params = PortfolioReturnsPerformanceParams.newBuilder()
                .symbol("AMZN")
                .symbol("MSFT")
                .symbol("GOOGL")
                .symbol("IBM")
                .symbol("BMW.DE")
                .dateFrom(LocalDate.parse("2015-01-01"))
                .dateTo(LocalDate.parse("2020-12-30"))
                .build();
//        for(MonthlyStockPriceReturnsPerformance stockReturnsPerformance: pRP.getMonthlyStockPriceReturnsPerformances(params)) {
//            System.out.println("Stock: " + stockReturnsPerformance.getStockSymbol());
//            System.out.println("Monthly Returns: " + Arrays.toString(stockReturnsPerformance.getMonthlyReturns().toArray()));
//        }

        pRP.printVarianceCovarianceMatrix(params);
    }
}
