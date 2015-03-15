package com.tsoft.myprocad.util.json;

public interface JsonReaderStringConsumer extends JsonReaderConsumer<String> {
    @Override
    public default String parse(String value) { return value; }
}
