package com.tsoft.myprocad.util.json;

import java.io.IOException;

@FunctionalInterface
public interface JsonReaderConsumer<T> {
    public void accept(T value) throws IOException;

    public default T parse(String value) { return null; };
}
