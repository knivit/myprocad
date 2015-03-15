package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

public class Level implements Comparable<Level>, Cloneable, JsonSerializable {
    private long id;
    private String name;
    private int start;
    private int end;

    // Don't remove this constructor, it is needed for JsonReader
    public Level() { }

    public Level(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public static Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;
            case 1: return Integer.class;
            case 2: return Integer.class;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    public Object getTableModelColumnValueAt(int columnIndex) {
        switch (columnIndex) {
            case 0: return name;
            case 1: return start;
            case 2: return end;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    public void setTableModelColumnValueAt(int columnIndex, Object value) {
        switch (columnIndex) {
            case 0: name = value.toString(); return;
            case 1: start = new Integer(value.toString()); return;
            case 2: end = new Integer(value.toString()); return;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    @Override
    public int compareTo(Level level) {
        return start < level.start ? -1 : (start == level.start ? 0 : 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Level level = (Level) o;

        if (!name.equals(level.name)) return false;

        return true;
    }

    @Override
    public Level clone() {
        try {
            Level clone = (Level)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + " [" + start + ", " + end + "]";
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("id", id)
                .write("name", name)
                .write("start", start)
                .write("end", end);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defLong("id", ((value) -> id = value))
                .defString("name", ((value) -> name = value))
                .defInteger("start", ((value) -> start = value))
                .defInteger("end", ((value) -> end = value))
                .read();
    }
}
