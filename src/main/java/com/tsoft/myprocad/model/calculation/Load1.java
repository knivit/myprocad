package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

/** Постояннвая нагрузка (например, перекрытия) */
public class Load1 implements Cloneable, JsonSerializable {
    public String name;
    public double density; // плотность, кгс/м3
    public double h;       // высота, м

    public Load1() { }

    public Load1(String name, double density, double h) {
        this.name = name;
        this.density = density;
        this.h = h;
    }

    public static Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) return String.class;
        return Double.class;
    }

    public Object getTableModelColumnValueAt(int columnIndex) {
        switch (columnIndex) {
            case 0: return name;
            case 1: return density;
            case 2: return h;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    public void setTableModelColumnValueAt(int col, Object value) {
        switch (col) {
            case 0: { name = (String)value; return; }
            case 1: { density = (double)value; return; }
            case 2: { h = (double)value; return; }
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + col);
    }

    @Override
    public Load1 clone() {
        try {
            Load1 clone = (Load1)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public String toString() {
        return StringUtil.toString(density * h, 3) + " кгс/м2";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Load1 load1 = (Load1) o;

        if (Double.compare(load1.density, density) != 0) return false;
        if (Double.compare(load1.h, h) != 0) return false;
        if (!name.equals(load1.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(density);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(h);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("name", name)
                .write("density", density)
                .write("h", h);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defString("name", ((value) -> name = value))
                .defDouble("density", ((value) -> density = value))
                .defDouble("h", ((value) -> h = value))
                .read();

    }
}
