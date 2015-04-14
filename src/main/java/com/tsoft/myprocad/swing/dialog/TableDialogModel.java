package com.tsoft.myprocad.swing.dialog;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableDialogModel extends AbstractTableModel {
    class Element {
        public Object[] values;
    }

    private List<Element> elements = new ArrayList<>();

    private String[] columnNames;
    private Class[] columnClasses;
    private boolean[] editable;

    public TableDialogModel(String[] columnNames, Class[] columnClasses, boolean[] editable) {
        super();

        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        this.editable = editable;
    }

    public Object[] getElement(int index) {
        return elements.get(index).values;
    }

    public void addElement(Object ... values) {
        Element element = new Element();
        element.values = values;
        elements.add(element);
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
        Element element = elements.get(row);
        return element.values[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return editable[col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Element element = elements.get(row);
        element.values[col] = value;
    }
}
