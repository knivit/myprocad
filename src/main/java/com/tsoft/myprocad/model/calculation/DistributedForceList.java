package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class DistributedForceList  extends ArrayList<DistributedForce> implements TableDialogPanelSupport<DistributedForce> {
    private transient DistributedForcesTableModel tableModel;

    public DistributedForceList() { super(); }

    @Override
    public TableDialogPanelSupport<DistributedForce> getDeepClone() {
        DistributedForceList copyList = new DistributedForceList();
        for (DistributedForce df : this) {
            DistributedForce copy = df.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new DistributedForcesTableModel(this);
        return tableModel;
    }

    @Override
    public DistributedForce addDialog() {
        DistributedForce distributedForce = new DistributedForce();

        add(distributedForce);
        return distributedForce;
    }

    @Override
    public boolean deleteDialog(DistributedForce distributedForce) {
        return remove(distributedForce);
    }
}
