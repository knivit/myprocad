package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class MomentList extends ArrayList<Moment> implements TableDialogPanelSupport<Moment> {
    private transient MomentsTableModel tableModel;

    public MomentList() { super(); }

    @Override
    public TableDialogPanelSupport<Moment> getDeepClone() {
        MomentList copyList = new MomentList();
        for (Moment moment : this) {
            Moment copy = moment.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new MomentsTableModel(this);
        return tableModel;
    }

    @Override
    public Moment addDialog() {
        Moment moment = new Moment();
        add(moment);
        return moment;
    }

    @Override
    public boolean deleteDialog(Moment moment) {
        return remove(moment);
    }
}
