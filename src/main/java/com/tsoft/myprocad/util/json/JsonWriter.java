package com.tsoft.myprocad.util.json;

import com.tsoft.myprocad.util.StringUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

public class JsonWriter {
    private Writer out;
    private boolean isFirstField = true;

    public JsonWriter(OutputStream stream) throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
    }

    public void close() throws IOException { out.close(); }

    private JsonWriter write(Object value) throws IOException {
        if (value == null) out.write("null");
        else if (value instanceof Collection) {
            // a list, a set etc
            writeCollection((Collection) value, false);
        } else if (value instanceof JsonSerializable) {
            // an object
            out.write('{');
            isFirstField = true;
            ((JsonSerializable)value).toJson(this);
            out.write("}\n");
        }
        else {
            out.write('"');
            String str = value.toString();
            for (int i = 0; i < str.length(); i ++) {
                char ch = str.charAt(i);

                // Anything less than ASCII space, write either in \\u00xx form, or the special \t, \n, etc. form
                if (ch < ' ') {
                    if (ch == '\b') out.write("\\b");
                    else if (ch == '\t') out.write("\\t");
                    else if (ch == '\n') out.write("\\n");
                    else if (ch == '\f') out.write("\\f");
                    else if (ch == '\r') out.write("\\r");
                    else {
                        String hex = Integer.toHexString(ch);
                        out.write("\\u");
                        int pad = 4 - hex.length();
                        for (int k = 0; k < pad; k++) {
                            out.write('0');
                        }
                        out.write(hex);
                    }
                }
                else if (ch == '\\' || ch == '"') {
                    out.write('\\');
                    out.write(ch);
                } else {
                    // Anything else - write in UTF-8 form (multi-byte encoded) (OutputStreamWriter is UTF-8)
                    out.write(ch);
                }
            }
            out.write('"');
        }
        return this;
    }

    private void writeCollection(Collection collection, boolean writeType) throws IOException {
        out.write("[\n");
        boolean isFirst = true;
        for (Object item : collection) {
            if (!isFirst) out.write(',');
            if (item == null) {
                out.write("{}");
            } else {
                if (!(item instanceof JsonSerializable)) throw new IllegalStateException("Items int the list must implement " + JsonSerializable.class.getName());

                JsonSerializable jsi = (JsonSerializable)item;
                if (writeType) {
                    // write item's type
                    write(jsi.getTypeName());
                    out.write(':');
                }
                write(jsi);
            }
            isFirst = false;
        }
        out.write(']');
    }

    public JsonWriter write(String fieldName, Object value) throws IOException {
        if (StringUtil.isEmpty(fieldName)) throw new IllegalArgumentException("Field name can't be empty");

        if (!isFirstField) out.write(", ");
        isFirstField = false;

        write(fieldName);
        out.write(':');
        write(value);

        return this;
    }

    /** A collection contained objects of different types */
    public JsonWriter writeMixCollection(String fieldName, Collection collection) throws IOException {
        if (StringUtil.isEmpty(fieldName)) throw new IllegalArgumentException("Field name can't be empty");

        if (!isFirstField) out.write(", ");
        isFirstField = false;

        write(fieldName);
        out.write(':');
        writeCollection(collection, true);

        return this;
    }

    public void toJson(JsonSerializable obj) throws IOException {
        try {
            write(obj);
        } finally {
            close();
        }
    }
}
