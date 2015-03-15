package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

public class Force implements Cloneable, JsonSerializable {
    public double vs; // Сосредоточенная сила F (кН)
    public double zs; // точка приложения (м)

    public void setVs(Object value) {
        this.vs = (double)value;
    }

    public void setZs(Object value) {
        this.zs = (double)value;
    }

    public static Class<?> getColumnClass(int columnIndex) {
        return Double.class;
    }

    public Object getTableModelColumnValueAt(int columnIndex) {
        switch (columnIndex) {
            case 0: return vs;
            case 1: return zs;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    public void setTableModelColumnValueAt(int col, Object value) {
        switch (col) {
            case 0: { setVs(value); return; }
            case 1: { setZs(value); return; }
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + col);
    }

    public String getName() { return L10.get(L10.CALCULATION_BEAM_FORCES_PROPERTY); }

    @Override
    public Force clone() {
        try {
            Force clone = (Force)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s; %s)", StringUtil.toString(vs, 3), StringUtil.toString(zs, 3));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Force force = (Force) o;

        if (Double.compare(force.vs, vs) != 0) return false;
        if (Double.compare(force.zs, zs) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(vs);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(zs);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("vs", vs)
                .write("zs", zs);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defDouble("vs", ((value) -> vs = value))
                .defDouble("zs", ((value) -> zs = value))
                .read();
    }
}
