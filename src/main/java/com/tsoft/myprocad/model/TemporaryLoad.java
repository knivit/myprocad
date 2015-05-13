package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

/** Временная нагрузка (люди, перегородки, снеговая и ветровая нагрузки) */
public class TemporaryLoad implements Cloneable, JsonSerializable {
    public String name;
    public double value; // кгс/м2

    public TemporaryLoad() { }

    public TemporaryLoad(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public TemporaryLoad clone() {
        try {
            TemporaryLoad clone = (TemporaryLoad)super.clone();
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

        TemporaryLoad temporaryLoad = (TemporaryLoad) o;

        if (Double.compare(temporaryLoad.value, value) != 0) return false;
        if (!name.equals(temporaryLoad.name)) return false;

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
