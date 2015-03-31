package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public interface TableDialogPanelSupport<E> {
    public AbstractTableModel getTableModel();

    public default void setupCustomColumns(JTable table) { }

    public E get(int index);

    public default TableDialogPanelSupport<E> getDeepClone() { return null; }

    public int indexOf(Object obj);

    public int size();

    public default E addDialog() { return null; };

    public default boolean deleteDialog(E obj) { return false; }
}
