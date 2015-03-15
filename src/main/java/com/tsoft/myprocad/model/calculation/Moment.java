package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

public class Moment implements Cloneable, JsonSerializable {
    public double vm; // Изгибающий момент M (кНм)
    public double zm; // точка приложения (м)

    public void setVm(Object value) {
        double vm = (Double)value;
        this.vm = vm;
    }

    public void setZm(Object value) {
        double zm = (Double)value;
        this.zm = zm;
    }

    public static Class<?> getColumnClass(int columnIndex) {
        return Double.class;
    }

    public Object getTableModelColumnValueAt(int columnIndex) {
        switch (columnIndex) {
            case 0: return vm;
            case 1: return zm;
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + columnIndex);
    }

    public void setTableModelColumnValueAt(int col, Object value) {
        switch (col) {
            case 0: { setVm(value); return; }
            case 1: { setZm(value); return; }
        }
        throw new IllegalArgumentException("Wrong columnIndex=" + col);
    }

    public String getName() { return L10.get(L10.CALCULATION_BEAM_BENDING_MOMENTS_PROPERTY); }

    @Override
    public Moment clone() {
        try {
            Moment clone = (Moment)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public String toString() {
        return String.format("(%.3f; %.3f)", vm, zm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moment moment = (Moment) o;

        if (Double.compare(moment.vm, vm) != 0) return false;
        if (Double.compare(moment.zm, zm) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(vm);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(zm);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("vm", vm)
                .write("zm", zm);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defDouble("vm", ((value) -> vm = value))
                .defDouble("zm", ((value) -> zm = value))
                .read();
    }
}
