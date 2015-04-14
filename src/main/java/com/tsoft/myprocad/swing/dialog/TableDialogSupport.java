package com.tsoft.myprocad.swing.dialog;

import javax.swing.table.AbstractTableModel;

public class TableDialogSupport implements TableDialogPanelSupport {
    private TableDialogModel tableModel;

    public TableDialogSupport(String[] columnNames, Class[] columnClasses, boolean[] editable) {
        tableModel = new TableDialogModel(columnNames, columnClasses, editable);
    }

    public void addElement(Object ... values) { tableModel.addElement(values); }

    @Override
    public AbstractTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public Object[] get(int index) {
        return tableModel.getElement(index);
    }

    @Override
    public int size() {
        return tableModel.getRowCount();
    }
}
