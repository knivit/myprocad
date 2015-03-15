package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;

import javax.swing.table.AbstractTableModel;

public class DistributedForcesTableModel extends AbstractTableModel {
    private static final transient String[] COLUMN_NAMES = new String[] {
            L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Q1),
            L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Z1),
            L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Q2),
            L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Z2)
    };

    private DistributedForceList distributedForceList;

    public DistributedForcesTableModel(DistributedForceList distributedForceList) {
        this.distributedForceList = distributedForceList;
    }

    @Override
    public String getColumnName(int col) { return COLUMN_NAMES[col]; }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return DistributedForce.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return distributedForceList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DistributedForce distributedForce = distributedForceList.get(rowIndex);
        return distributedForce.getTableModelColumnValueAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row >= 0 && row < distributedForceList.size()) {
            DistributedForce distributedForce = distributedForceList.get(row);
            distributedForce.setTableModelColumnValueAt(col, value);
        }
    }
}
