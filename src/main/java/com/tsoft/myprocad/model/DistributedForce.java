package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

public class DistributedForce implements Cloneable, JsonSerializable {
    public double z1; // начало a (м)
    public double q1; // Распределённая нагрузка в начале qa (кН/м)
    public double z2; // конец b (м)
    public double q2; // Распределённая нагрузка в конце qb (кН/м)

    public DistributedForce() { }

    public void setZ1(Object value) {
        this.z1 = (double)value;
    }

    public void setQ1(Object value) {
        this.q1 = (double)value;
    }

    public void setZ2(Object value) {
        this.z2 = (double)value;
    }

    public void setQ2(Object value) {
        this.q2 = (double)value;
    }

    public void normalize() {
        if (z2 < z1) {
            double a = q1;
            q1 = q2;
            q2 = a;
            a = z1;
            z1 = z2;
            z2 = a;
        }
    }

    public String getName() { return L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_PROPERTY); }

    @Override
    public DistributedForce clone() {
        try {
            DistributedForce clone = (DistributedForce)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s, %s) - (%s, %s)", StringUtil.toString(q1, 3), StringUtil.toString(z1, 3),
                StringUtil.toString(q2, 3), StringUtil.toString(z2, 3));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistributedForce that = (DistributedForce) o;

        if (Double.compare(that.q1, q1) != 0) return false;
        if (Double.compare(that.q2, q2) != 0) return false;
        if (Double.compare(that.z1, z1) != 0) return false;
        if (Double.compare(that.z2, z2) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(z1);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(q1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(q2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("z1", z1)
                .write("q1", q1)
                .write("z2", z2)
                .write("q2", q2);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defDouble("z1", ((value) -> z1 = value))
                .defDouble("q1", ((value) -> q1 = value))
                .defDouble("z2", ((value) -> z2 = value))
                .defDouble("q2", ((value) -> q2 = value))
                .read();
    }
}
