package com.tsoft.myprocad.util.json;

public interface JsonReaderIntegerConsumer extends JsonReaderConsumer<Integer> {
    @Override
    public default Integer parse(String value) { return Integer.parseInt(value); }
}
