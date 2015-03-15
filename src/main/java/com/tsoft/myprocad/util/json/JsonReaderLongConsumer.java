package com.tsoft.myprocad.util.json;

public interface JsonReaderLongConsumer extends JsonReaderConsumer<Long> {
    public default Long parse(String value) { return Long.parseLong(value); }
}
