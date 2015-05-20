package com.tsoft.myprocad.util.json;

public interface JsonReaderLongConsumer extends JsonReaderConsumer<Long> {
    public default Long parse(String value) { return value == null ? null : Long.parseLong(value); }
}
