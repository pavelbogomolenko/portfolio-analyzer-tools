package io.github.pavelbogomolenko.timeseries;

import java.time.LocalDate;
import java.util.Comparator;

public class DataPoint implements Comparable<DataPoint> {
    private final LocalDate date;
    private final double value;

    public DataPoint(double value, LocalDate date) {
        this.value = value;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(DataPoint o) {
        return this.date.compareTo(o.getDate());
    }

    public static Comparator<DataPoint> valueOrder() {
        return new ByValueOrder();
    }

    private static class ByValueOrder implements Comparator<DataPoint> {

        @Override
        public int compare(DataPoint o1, DataPoint o2) {
            return Double.compare(o1.value, o2.value);
        }
    }
}
