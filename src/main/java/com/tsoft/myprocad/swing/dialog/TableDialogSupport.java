package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.l10n.L10;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableDialogSupport extends ArrayList<Object[]> implements TableDialogPanelSupport {
    private TableDialogModel tableModel;

    public TableDialogSupport(String[] columnNames, Class[] columnClasses, boolean[] editable) {
        assert(columnNames.length == columnClasses.length && columnClasses.length == editable.length);
        tableModel = new TableDialogModel(columnNames, columnClasses, editable);
        tableModel.setElements(this);
    }

    public Object[] addElement(Object ... values) {
        add(values);
        return values;
    }

    @Override
    public AbstractTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public TableDialogPanelSupport getDeepClone() {
        TableDialogSupport clone = (TableDialogSupport)this.clone();
        clone.tableModel.setElements(clone);
        clone.clear();

        for (Object[] element : this) {
            Object[] copy = element.clone();
            clone.add(copy);
        }
        return clone;
    }

    @Override
    public Object addDialog() {
        Object[] element = new Object[tableModel.getColumnCount()];
        return addElement(element);
    }

    @Override
    public boolean deleteDialog(int row) {
        remove(row);
        return true;
    }

    @Override
    public String toString() {
        return L10.get(L10.NUMBER_OF_ITEMS, size());
    }
}
