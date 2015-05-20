package com.tsoft.myprocad.util.json;

public interface JsonReaderFloatConsumer extends JsonReaderConsumer<Float> {
    public default Float parse(String value) { return value == null ? null : Float.parseFloat(value); }
}
