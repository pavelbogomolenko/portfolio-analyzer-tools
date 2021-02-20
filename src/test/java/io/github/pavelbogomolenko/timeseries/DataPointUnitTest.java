package io.github.pavelbogomolenko.timeseries;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataPointUnitTest {
    @Test
    void should_compare_to_should_return_minus_when_prev_datapoint_less_than_current_datapoint() {
        LocalDate now = LocalDate.now();
        DataPoint prevDataPoint = new DataPoint(1, now.minusDays(1));
        DataPoint currentDataPoint = new DataPoint(2, now);

        assertThat(prevDataPoint.compareTo(currentDataPoint), is(-1));
    }

    @Test
    void should_compare_to_should_return_when_prev_datapoint_less_than_current_datapoint() {
        LocalDate now = LocalDate.now();
        DataPoint prevDataPoint = new DataPoint(1, now);
        DataPoint currentDataPoint = new DataPoint(2, now);

        assertThat(prevDataPoint.compareTo(currentDataPoint), is(0));
    }

    @Test
    void should_compare_to_should_return_plus_when_current_datapoint_greater_prev_datapoint_() {
        LocalDate now = LocalDate.now();
        DataPoint prevDataPoint = new DataPoint(1, now.minusDays(1));
        DataPoint currentDataPoint = new DataPoint(2, now);

        assertThat(currentDataPoint.compareTo(prevDataPoint), is(1));
    }
}
