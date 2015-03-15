package com.l2fprod.common.util.converter;

import com.tsoft.myprocad.util.math.Calculator;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Convert to and from numbers
 */
public class NumberConverters implements Converter {
    private static Map<Integer, NumberFormat> defaultFormats = new HashMap<>();

    private NumberFormat format;

    public NumberConverters() {
        this(getDefaultFormat(64, 3));
    }

    public NumberConverters(NumberFormat format) {
        this.format = format;
    }

    public static NumberFormat getDefaultFormat(int maximunIntegerDigits, int maximumFractionDigits) {
        NumberFormat value;

        synchronized (NumberConverters.class) {
            Integer key = maximunIntegerDigits * 1000 + maximumFractionDigits;
            value = defaultFormats.get(key);
            if (value == null) {
                value = NumberFormat.getNumberInstance();
                value.setMinimumIntegerDigits(1);
                value.setMaximumIntegerDigits(maximunIntegerDigits);
                value.setMinimumFractionDigits(0);
                value.setMaximumFractionDigits(maximumFractionDigits);

                defaultFormats.put(key, value);
            }
        }
        return value;
    }

    public void register(ConverterRegistry registry) {
        registry.addConverter(Number.class, Double.class, this);
        registry.addConverter(Number.class, Float.class, this);
        registry.addConverter(Number.class, Integer.class, this);
        registry.addConverter(Number.class, Long.class, this);
        registry.addConverter(Number.class, Short.class, this);

        registry.addConverter(Double.class, Double.class, this);
        registry.addConverter(Double.class, Float.class, this);
        registry.addConverter(Double.class, Integer.class, this);
        registry.addConverter(Double.class, Long.class, this);
        registry.addConverter(Double.class, Short.class, this);
        registry.addConverter(Double.class, String.class, this);

        registry.addConverter(Float.class, Double.class, this);
        registry.addConverter(Float.class, Float.class, this);
        registry.addConverter(Float.class, Integer.class, this);
        registry.addConverter(Float.class, Long.class, this);
        registry.addConverter(Float.class, Short.class, this);
        registry.addConverter(Float.class, String.class, this);

        registry.addConverter(Integer.class, Double.class, this);
        registry.addConverter(Integer.class, Float.class, this);
        registry.addConverter(Integer.class, Integer.class, this);
        registry.addConverter(Integer.class, Long.class, this);
        registry.addConverter(Integer.class, Short.class, this);
        registry.addConverter(Integer.class, String.class, this);

        registry.addConverter(Long.class, Double.class, this);
        registry.addConverter(Long.class, Float.class, this);
        registry.addConverter(Long.class, Integer.class, this);
        registry.addConverter(Long.class, Long.class, this);
        registry.addConverter(Long.class, Short.class, this);
        registry.addConverter(Long.class, String.class, this);

        registry.addConverter(Short.class, Double.class, this);
        registry.addConverter(Short.class, Float.class, this);
        registry.addConverter(Short.class, Integer.class, this);
        registry.addConverter(Short.class, Long.class, this);
        registry.addConverter(Short.class, Short.class, this);
        registry.addConverter(Short.class, String.class, this);

        registry.addConverter(String.class, Double.class, this);
        registry.addConverter(String.class, Float.class, this);
        registry.addConverter(String.class, Integer.class, this);
        registry.addConverter(String.class, Long.class, this);
        registry.addConverter(String.class, Short.class, this);
    }

    public Object convert(Class targetType, Object value) {
        // are we dealing with a number to number conversion?
        if ((value instanceof Number) && Number.class.isAssignableFrom(targetType)) {
            if (Double.class.equals(targetType)) {
                return new Double(((Number)value).doubleValue());
            } else if (Float.class.equals(targetType)) {
                return new Float(((Number)value).floatValue());
            } else if (Integer.class.equals(targetType)) {
                return new Integer(((Number)value).intValue());
            } else if (Long.class.equals(targetType)) {
                return new Long(((Number)value).longValue());
            } else if (Short.class.equals(targetType)) {
                return new Short(((Number)value).shortValue());
            } else {
                throw new IllegalArgumentException("this code must not be reached");
            }
        } else if ((value instanceof Number) && String.class.equals(targetType)) {
            if ((value instanceof Double) || (value instanceof Float)) {
                return format.format(((Number)value).doubleValue());
            } else {
                return format.format(((Number)value).longValue());
            }
        } else if ((value instanceof String) && Number.class.isAssignableFrom(targetType)) {
            Calculator calculator = new Calculator();
            Double result = calculator.calc((String)value);

            // In case an error, return 0
            if (result == null) return 0;

            if (Double.class.equals(targetType)) {
                return result.doubleValue();
            } else if (Float.class.equals(targetType)) {
                return result.floatValue();
            } else if (Integer.class.equals(targetType)) {
                return result.intValue();
            } else if (Long.class.equals(targetType)) {
                return result.longValue();
            } else if (Short.class.equals(targetType)) {
                return result.shortValue();
            } else {
                throw new IllegalArgumentException("this code must not be reached");
            }
        }
        throw new IllegalArgumentException("no conversion supported");
    }

}