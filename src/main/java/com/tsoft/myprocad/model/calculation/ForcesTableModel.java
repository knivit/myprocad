package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;

import javax.swing.table.AbstractTableModel;

public class ForcesTableModel extends AbstractTableModel {
    private static final transient String[] COLUMN_NAMES = new String[]{
            L10.get(L10.CALCULATION_BEAM_FORCES_TABLE_COLUMN_VS),
            L10.get(L10.CALCULATION_BEAM_FORCES_TABLE_COLUMN_ZS)
    };

    private ForceList forceList;

    public ForcesTableModel(ForceList forceList) {
        this.forceList = forceList;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Force.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return forceList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Force force = forceList.get(rowIndex);
        return force.getTableModelColumnValueAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row >= 0 && row < forceList.size()) {
            Force force = forceList.get(row);
            force.setTableModelColumnValueAt(col, value);
        }
    }
}