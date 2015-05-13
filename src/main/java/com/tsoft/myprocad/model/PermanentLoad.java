package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

/** Постояннвая нагрузка (например, перекрытия) */
public class PermanentLoad implements Cloneable, JsonSerializable {
    public String name;
    public double density; // плотность, кгс/м3
    public double h;       // высота, м

    public PermanentLoad() { }

    public PermanentLoad(String name, double density, double h) {
        this.name = name;
        this.density = density;
        this.h = h;
    }

    @Override
    public PermanentLoad clone() {
        try {
            PermanentLoad clone = (PermanentLoad)super.clone();
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

        PermanentLoad permanentLoad = (PermanentLoad) o;

        if (Double.compare(permanentLoad.density, density) != 0) return false;
        if (Double.compare(permanentLoad.h, h) != 0) return false;
        if (!name.equals(permanentLoad.name)) return false;

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
