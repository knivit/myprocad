package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ForceList extends ArrayList<Force> implements TableDialogPanelSupport<Force> {
    private transient ForcesTableModel tableModel;

    public ForceList() { super(); }

    @Override
    public TableDialogPanelSupport<Force> getDeepClone() {
        ForceList copyList = new ForceList();
        for (Force force : this) {
            Force copy = force.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new ForcesTableModel(this);
        return tableModel;
    }

    @Override
    public Force addDialog() {
        Force force = new Force();

        add(force);
        return force;
    }

    @Override
    public boolean deleteDialog(Force force) {
        return remove(force);
    }
}
