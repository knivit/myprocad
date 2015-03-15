package com.tsoft.myprocad.util.json;

public interface JsonReaderDoubleConsumer extends JsonReaderConsumer<Double> {
    public default Double parse(String value) { return Double.parseDouble(value); }
}
