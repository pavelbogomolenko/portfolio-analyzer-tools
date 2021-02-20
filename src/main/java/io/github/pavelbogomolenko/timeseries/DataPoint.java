package io.github.pavelbogomolenko.timeseries;

import java.time.LocalDate;

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
}
