package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;;
import java.util.ArrayList;

public class Load2List extends ArrayList<Load2> implements TableDialogPanelSupport<Load2> {
    private transient Load2TableModel tableModel;

    public Load2List() { super(); }

    @Override
    public TableDialogPanelSupport<Load2> getDeepClone() {
        Load2List copyList = new Load2List();
        for (Load2 load : this) {
            Load2 copy = load.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new Load2TableModel(this);
        return tableModel;
    }

    @Override
    public Load2 addDialog() {
        Load2 load = new Load2("Новая нагрузка", 100);

        add(load);
        return load;
    }

    @Override
    public boolean deleteDialog(Load2 load) {
        return remove(load);
    }
}
