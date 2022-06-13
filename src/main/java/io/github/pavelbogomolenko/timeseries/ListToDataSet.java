package io.github.pavelbogomolenko.timeseries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListToDataSet {

    public static <T> DataSet convert(List<T> from, String dateProp, String valueProp) {
        List<DataPoint> dataPoints = new ArrayList<>();
        for (T item: from) {
            LocalDate date = ListToDataSet.getProperty(item, dateProp, null);
            double value = ListToDataSet.getProperty(item, valueProp, 0.0);
            dataPoints.add(new DataPoint(value, date));
        }
        return new DataSet(dataPoints);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getProperty(Object obj, String property, T defaultValue) {
        try {
            Class cls = obj.getClass();
            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
            Method method = cls.getMethod(methodName);
            return (T) method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return defaultValue;
        }
    }
}
