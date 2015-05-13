package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.swing.menu.MenuAction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public interface TableDialogPanelSupport<E> {
    public AbstractTableModel getTableModel();

    public E get(int index);

    public int size();

    public default TableDialogPanelSupport<E> getDeepClone() { return null; }

    public default void setupCustomColumns(JTable table) { }

    public default Object addDialog() { return null; };

    public default boolean deleteDialog(int row) { return false; }

    public default List<MenuAction> getCustomButtons() { return null; }
}
