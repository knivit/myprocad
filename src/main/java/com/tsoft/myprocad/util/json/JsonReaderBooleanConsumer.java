package com.tsoft.myprocad.util.json;

public interface JsonReaderBooleanConsumer extends JsonReaderConsumer<Boolean> {
    public default Boolean parse(String value) { return Boolean.parseBoolean(value); }
}
