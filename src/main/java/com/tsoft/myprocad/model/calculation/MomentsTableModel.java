package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;

import javax.swing.table.AbstractTableModel;

public class MomentsTableModel extends AbstractTableModel {
    private static final transient String[] COLUMN_NAMES = new String[] {
            L10.get(L10.CALCULATION_BEAM_MOMENTS_TABLE_COLUMN_VM),
            L10.get(L10.CALCULATION_BEAM_MOMENTS_TABLE_COLUMN_ZM)
    };

    private MomentList momentList;

    public MomentsTableModel(MomentList momentList) {
        this.momentList = momentList;
    }

    @Override
    public String getColumnName(int col) { return COLUMN_NAMES[col]; }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Moment.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return momentList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Moment moment = momentList.get(rowIndex);
        return moment.getTableModelColumnValueAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row >= 0 && row < momentList.size()) {
            Moment moment = momentList.get(row);
            moment.setTableModelColumnValueAt(col, value);
        }
    }
}
