package com.tsoft.myprocad.report;

import com.tsoft.myprocad.model.Material;
import java.util.ArrayList;
import java.util.List;

public class MaterialGroupData implements Comparable {
    public Material material;

    public MaterialRecord total = new MaterialRecord("__TOTAL__");

    /* By size */
    public List<MaterialRecord> records = new ArrayList<>();

    public MaterialGroupData(Material material) {
        this.material = material;
    }

    @Override
    public int compareTo(Object o) {
        return material.compareTo(((MaterialGroupData)o).material);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialGroupData that = (MaterialGroupData) o;

        if (!material.equals(that.material)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = material.hashCode();
        return result;
    }
}
