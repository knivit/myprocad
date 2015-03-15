package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;

public enum MaterialUnit {
    M(1, L10.get(L10.MATERIAL_UNIT_M)),
    M2(2, L10.get(L10.MATERIAL_UNIT_M2)),
    M3(3, L10.get(L10.MATERIAL_UNIT_M3));

    private int id;
    private String text;

    MaterialUnit(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() { return id; }

    @Override
    public String toString() { return text; }

    public static MaterialUnit findById(int unitId) {
        for (MaterialUnit unit : values()) {
            if (unit.id == unitId) return unit;
        }
        return null;
    }
}
