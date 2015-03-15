package com.tsoft.myprocad.model.calculation;

import javax.swing.table.AbstractTableModel;

public class Load1TableModel extends AbstractTableModel {
    private static final transient String[] COLUMN_NAMES = new String[]{
            "Название нагрузки",
            "Плотность, кгс/м3",
            "Толщина слоя, м"
    };

    private Load1List loadList;

    public Load1TableModel(Load1List loadList) {
        this.loadList = loadList;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Load1.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return loadList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Load1 load = loadList.get(rowIndex);
        return load.getTableModelColumnValueAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (row >= 0 && row < loadList.size()) {
            Load1 load = loadList.get(row);
            load.setTableModelColumnValueAt(col, value);
        }
    }
}