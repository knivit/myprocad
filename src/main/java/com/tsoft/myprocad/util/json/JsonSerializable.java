package com.tsoft.myprocad.util.json;

import java.io.IOException;

public interface JsonSerializable {
    public void toJson(JsonWriter writer) throws IOException;

    public void fromJson(JsonReader reader) throws IOException;

    // Should be overridden to be used in mixed collections
    public default String getTypeName() { return null; }
}
