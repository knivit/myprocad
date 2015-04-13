package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Material;
import com.tsoft.myprocad.model.MaterialList;
import com.tsoft.myprocad.model.MaterialUnit;

import javax.swing.table.AbstractTableModel;

public class MaterialsTableModel extends AbstractTableModel {
    private static final transient String[] COLUMN_NAMES = new String[] {
            L10.get(L10.MATERIAL_COLUMN_NAME),
            L10.get(L10.MATERIAL_COLUMN_SG),
            L10.get(L10.MATERIAL_COLUMN_PRICE),
            L10.get(L10.MATERIAL_COLUMN_UNIT)
    };

    private static final transient Class[] COLUMN_CLASSES = { String.class, Float.class, Float.class, MaterialUnit.class };

    private MaterialList materialList;

    public MaterialsTableModel(MaterialList materialList) {
        this.materialList = materialList;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public int getRowCount() {
        return materialList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Class<?> getColumnClass(int col) { return COLUMN_CLASSES[col]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Material material = materialList.get(rowIndex);
        return material.getTableModelColumnValueAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Material material = materialList.get(row);
        material.setTableModelColumnValueAt(col, value);
    }
}