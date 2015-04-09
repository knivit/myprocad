package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

public class Material implements Comparable<Material>, Cloneable, JsonSerializable {
    private long id;
    private String name;
    private float density = 1.0f; // tn/m3
    private float price = 1.0f;
    private int unitId = MaterialUnit.M3.getId();
    private boolean isDefault;

    public transient MaterialUnit unit;
    private transient String lowerCaseName;

    // Don't remove this constructor, it is needed for JsonReader
    public Material() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public MaterialUnit getUnit() {
        if (unit == null) unit = MaterialUnit.findById(unitId);
        return unit;
    }

    public void setUnit(MaterialUnit unit) {
        unitId = unit.getId();
        this.unit = unit;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public static Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;
            case 1: return Float.class;
            case 2: return Float.class;
            case 3: return MaterialUnit.class;
        }
        throw new IllegalArgumentException("Wrong columnIndex = " + columnIndex);
    }

    public Object getTableModelColumnValueAt(int columnIndex) {
        switch (columnIndex) {
            case 0: return name;
            case 1: return density;
            case 2: return price;
            case 3: return getUnit();
        }
        throw new IllegalArgumentException("Wrong columnIndex = " + columnIndex);
    }

    public void setTableModelColumnValueAt(int columnIndex, Object value) {
        switch (columnIndex) {
            case 0: name = value.toString(); return;
            case 1: density = new Float(value.toString()); return;
            case 2: price = new Float(value.toString()); return;
            case 3: setUnit((MaterialUnit)value); return;
        }
        throw new IllegalArgumentException("Wrong columnIndex = " + columnIndex);
    }

    public boolean equalByName(String otherName) {
        return name.equalsIgnoreCase(otherName);
    }

    public String getLowerCaseName() {
        if (lowerCaseName == null) {
            lowerCaseName = name.toLowerCase();
        }
        return lowerCaseName;
    }

    @Override
    public int compareTo(Material material) {
        return getLowerCaseName().compareTo(material.getLowerCaseName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;

        if (!getLowerCaseName().equals(material.getLowerCaseName())) return false;

        return true;
    }

    @Override
    public Material clone() {
        try {
            Material clone = (Material)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public int hashCode() {
        int result = getLowerCaseName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("id", id)
                .write("name", name)
                .write("density", density)
                .write("price", price)
                .write("unitId", unitId)
                .write("isDefault", isDefault);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defLong("id", ((value) -> id = value))
                .defString("name", ((value) -> name = value))
                .defFloat("density", ((value) -> density = value))
                .defFloat("price", ((value) -> price = value))
                .defInteger("unitId", ((value) -> unitId = value))
                .defBoolean("isDefault", ((value) -> isDefault = value))
                .read();
    }
}
