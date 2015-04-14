package com.tsoft.myprocad.swing.dialog;

import javax.swing.table.AbstractTableModel;

public class TableDialogSupport implements TableDialogPanelSupport {
    private TableDialogModel tableModel;

    public TableDialogSupport(String[] columnNames, Class[] columnClasses, boolean[] editable) {
        assert(columnNames.length == columnClasses.length && columnClasses.length == editable.length);
        tableModel = new TableDialogModel(columnNames, columnClasses, editable);
    }

    public Object addElement(Object ... values) { return tableModel.addElement(values); }

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

    @Override
    public Object addDialog() {
        Object[] element = new Object[tableModel.getColumnCount()];
        return addElement(element);
    }

    @Override
    public boolean deleteDialog(int row) {
        tableModel.removeElement(row);
        return true;
    }
}
