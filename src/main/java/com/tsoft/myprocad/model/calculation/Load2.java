package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

/** Временная нагрузка (люди, перегородки, снеговая и ветровая нагрузки) */
public class Load2 implements Cloneable, JsonSerializable {
    public String name;
    public double value; // кгс/м2

    public Load2() { }

    public Load2(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public static Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return String.class;
        return Double.class;
    }

    public Object getTableModelColumnValueAt(int columnIndex) {
        switch (columnIndex) {
            case 0: return name;
            case 1: return value;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    public void setTableModelColumnValueAt(int col, Object value) {
        switch (col) {
            case 0: { name = (String)value; return; }
            case 1: { this.value = (double)value; return; }
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + col);
    }

    @Override
    public Load2 clone() {
        try {
            Load2 clone = (Load2)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public String toString() {
        return StringUtil.toString(value, 3) + " кгс/м2";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Load2 load2 = (Load2) o;

        if (Double.compare(load2.value, value) != 0) return false;
        if (!name.equals(load2.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("name", name)
                .write("value", value);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defString("name", ((value) -> name = value))
                .defDouble("value", ((v) -> value = v))
                .read();

    }
}
