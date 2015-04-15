package com.tsoft.myprocad.swing.dialog;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableDialogModel extends AbstractTableModel {
    public String[] columnNames;
    public Class[] columnClasses;
    public boolean[] editable;
    public List<Object[]> elements;

    public TableDialogModel(String[] columnNames, Class[] columnClasses, boolean[] editable) {
        super();

        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        this.editable = editable;
    }

    public void setElements(List<Object[]> elements) {
        this.elements = elements;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return elements.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object[] values = elements.get(row);
        return values[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return editable[col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Object[] values = elements.get(row);
        values[col] = value;
    }
}
