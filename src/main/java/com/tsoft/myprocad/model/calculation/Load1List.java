package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class Load1List extends ArrayList<Load1> implements TableDialogPanelSupport<Load1> {
    private transient Load1TableModel tableModel;

    public Load1List() { super(); }

    @Override
    public TableDialogPanelSupport<Load1> getDeepClone() {
        Load1List copyList = new Load1List();
        for (Load1 load : this) {
            Load1 copy = load.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new Load1TableModel(this);
        return tableModel;
    }

    @Override
    public Object addDialog() {
        Load1 load = new Load1("Новая нагрузка", 1000, 0.1);

        add(load);
        return load;
    }

    @Override
    public boolean deleteDialog(int row) {
        remove(row);
        return true;
    }
}