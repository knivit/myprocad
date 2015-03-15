package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public interface TableDialogPanelSupport<E> {
    public AbstractTableModel getTableModel();

    public void setupCustomColumns(JTable table);

    public E get(int index);

    public TableDialogPanelSupport<E> getDeepClone();

    public int indexOf(Object obj);

    public int size();

    public E addDialog();

    public boolean deleteDialog(E obj);
}
