package com.tsoft.myprocad.util.json;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    private Reader in;

    private int offset;
    private int lineNo = 1;
    private int posNo = 1;

    private int unreadCh = -1;

    enum FieldType { Primitive, Object, Collection }

    class Field {
        public FieldType type;
        public String fieldName;
        public JsonReaderConsumer consumer;

        // for lists
        public JsonReaderCollectionSupplier collectionSupplier; // list with items of the same class
        public JsonReaderMixCollectionSupplier mixListSupplier; // list with items of different classes

        public Field(String fieldName, JsonReaderConsumer consumer) {
            this.fieldName = fieldName;
            this.consumer = consumer;
        }

        public Field(String fieldName, JsonReaderCollectionSupplier collectionSupplier, JsonReaderListConsumer consumer) {
            this(fieldName, consumer);
            this.collectionSupplier = collectionSupplier;
        }

        public Field(String fieldName, JsonReaderMixCollectionSupplier mixListSupplier, JsonReaderListConsumer consumer) {
            this(fieldName, consumer);
            this.mixListSupplier = mixListSupplier;
        }
    }

    private List<Field> fields = new ArrayList<>();

    private JsonReader() { }

    public JsonReader(InputStream stream) throws IOException {
        in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    }

    private Field def(String fieldName, JsonReaderConsumer consumer) {
        Field field = new Field(fieldName, consumer);
        field.type = FieldType.Primitive;
        fields.add(field);
        return field;
    }

    public JsonReader defString(String fieldName, JsonReaderStringConsumer consumer) {
        def(fieldName, consumer);
        return this;
    }

    public JsonReader defInteger(String fieldName, JsonReaderIntegerConsumer consumer) {
        def(fieldName, consumer);
        return this;
    }

    public JsonReader defLong(String fieldName, JsonReaderLongConsumer consumer) {
        def(fieldName, consumer);
        return this;
    }

    public JsonReader defBoolean(String fieldName, JsonReaderBooleanConsumer consumer) {
        def(fieldName, consumer);
        return this;
    }

    public JsonReader defFloat(String fieldName, JsonReaderFloatConsumer consumer) {
        def(fieldName, consumer);
        return this;
    }

    public JsonReader defDouble(String fieldName, JsonReaderDoubleConsumer consumer) {
        def(fieldName, consumer);
        return this;
    }

    public JsonReader defObject(String fieldName, JsonReaderObjectConsumer consumer) {
        Field field = def(fieldName, consumer);
        field.type = FieldType.Object;
        return this;
    }

    public JsonReader defCollection(String fieldName, JsonReaderCollectionSupplier supplier, JsonReaderListConsumer consumer) {
        Field field = new Field(fieldName, supplier, consumer);
        field.type = FieldType.Collection;
        fields.add(field);
        return this;
    }

    public JsonReader defMixCollection(String fieldName, JsonReaderMixCollectionSupplier supplier, JsonReaderListConsumer consumer) {
        Field field = new Field(fieldName, supplier, consumer);
        field.type = FieldType.Collection;
        fields.add(field);
        return this;
    }

    public void read() throws IOException {
        try {
            doRead();
        } catch (Exception ex) {
            String text = String.format("Input file at line %d, pos %d, offset %d", lineNo, posNo, offset);
            System.err.println(text);
            throw ex;
        }
    }

    private void doRead() throws IOException {
        // an object begins
        char ch = readChar();
        if (ch != '{') throw new IOException("An object must start with '{'");

        while (true) {
            // read field name
            String fieldName = readString();

            // delimiter
            if (readChar() != ':') throw new IOException("Between field name and field value must be ':'");

            // find out field's consumer
            JsonReaderConsumer consumer;
            Field field = findField(fieldName);
            if (field == null) consumer = new IgnoreFieldConsumer();
            else consumer = field.consumer;

            // read value
            ch = readChar();
            if (ch == '[') {
                JsonReaderCollectionSupplier collectionSupplier = field.collectionSupplier;
                JsonReaderMixCollectionSupplier mixCollectionSupplier = field.mixListSupplier;
                if (collectionSupplier == null && mixCollectionSupplier == null)
                    throw new IOException("Field " + fieldName + " must define one of list suppliers");

                // an array
                while ((ch = readChar()) != ']') {
                    unreadChar(ch);

                    JsonSerializable item;
                    if (collectionSupplier != null) {
                        item = collectionSupplier.get();
                    } else {
                        String itemType = readString();
                        if (readChar() != ':') throw new IOException("Between item's type and an item must be ':'");
                        item = mixCollectionSupplier.apply(itemType);
                    }

                    readItem(item);
                    consumer.accept(item);

                    ch = readChar();
                    if (ch != ',') unreadChar(ch);
                }
            } else if (ch == '{') {
                // an object
                unreadChar(ch);

                JsonReader reader = cloneReader();
                consumer.accept(reader);
                restoreState(reader);
            } else {
                // an ordinary value
                unreadChar(ch);

                String value = readString();

                if (value == null && field.type.equals(FieldType.Object)) {
                    // do nothing, as passing the null will make the consumer complex
                    // let's assume that such fields initializing will be done in object's fromJson() method
                } else {
                    Object parsedValue = consumer.parse(value);
                    consumer.accept(parsedValue);
                }
            }

            // next field ?
            ch = readChar();
            if (ch != ',') break;
        }

        if (ch != '}') throw new IOException("An object must end with '}'");
    }

    private JsonReader cloneReader() {
        // create a new JsonReader as it will have its own fields
        JsonReader reader = new JsonReader();

        // pass the current state to it
        reader.in = in;
        reader.unreadCh = unreadCh;
        reader.offset = offset;
        reader.lineNo = lineNo;
        reader.posNo = posNo;
        return reader;
    }

    private void restoreState(JsonReader reader) {
        offset = reader.offset;
        lineNo = reader.lineNo;
        posNo = reader.posNo;
        unreadCh = reader.unreadCh;
    }

    private void readItem(JsonSerializable item) throws IOException {
        // create a new JsonReader as it will have its own fields
        JsonReader reader = cloneReader();
        item.fromJson(reader);
        restoreState(reader);
    }

    class IgnoreFieldConsumer implements JsonReaderConsumer {
        @Override
        public void accept(Object value) throws IOException {
            // just skip an ordinary value
            if ((value == null) || (value instanceof String)) return;

            // for an array, skip until final ']'
            int n = 1;
            boolean isToken = false;

            while (n > 0) {
                char ch = readChar();
                if (!isToken && ch == ']') {
                    n--;
                    if (n == 0) break;
                } else if (!isToken && ch == '[') {
                    n++;
                } else if (ch == '"') isToken = !isToken;
            }
        }
    }

    private Field findField(String fieldName) {
        for (Field field : fields) {
            if (field.fieldName.equals(fieldName)) return field;
        }
        return null;
    }

    public String readString() throws IOException {
        char ch = readChar();
        if (ch != '"') {
            unreadChar(ch);
            boolean isNull = true;
            String nullValue = "null";
            for (int i = 0; i < nullValue.length(); i ++) {
                if (readChar() != nullValue.charAt(i)) {
                    isNull = false;
                    break;
                }
            }
            if (isNull) return null;

            throw new IOException("A string must start with '\"'");
        }

        StringBuilder buf = new StringBuilder(128);
        while ((ch = readChar(false)) != '"') {
            if (ch == '\\') {
                ch = readCh();
                if (ch == 'n') buf.append('\n');
                else if (ch == 'r') buf.append('\r');
                else if (ch == 't') buf.append('\t');
                else if (ch == 'f') buf.append('\f');
                else if (ch == 'b') buf.append('\b');
                else if (ch == '\\') buf.append('\\');
                else if (ch == '"') buf.append('"');
                else if (ch == 'u') {
                    char[] hex = new char[4];
                    for (int i = 0; i < hex.length; i ++) {
                        hex[i] = readCh();
                    }
                    int value = Integer.parseInt(new String(hex), 16);
                    buf.append(Character.toChars(value));
                } else throw new IOException("Unknown sequence '\\" + ch + "'");
            } else buf.append(ch);
        }

        return buf.toString();
    }

    private char readChar() throws IOException {
        return readChar(true);
    }

    private char readChar(boolean skipWhites) throws IOException {
        if (unreadCh != -1) {
            char ch = (char)unreadCh;
            unreadCh = -1;
            return ch;
        }

        char ch;
        while ((ch = readCh()) <= ' ' && skipWhites) ;
        return ch;
    }

    private void unreadChar(char ch) {
        unreadCh = ch;
    }

    private char readCh() throws IOException {
        int n = in.read();
        if (n == -1) throw new IOException("Unexpected EOF");

        offset ++;
        if (n == '\n') { lineNo ++; posNo = 1; }
        else posNo ++;
        return (char)n;
    }
}
