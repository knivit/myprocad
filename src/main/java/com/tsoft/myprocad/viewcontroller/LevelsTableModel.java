package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Level;
import com.tsoft.myprocad.model.LevelList;
import javax.swing.table.AbstractTableModel;

public class LevelsTableModel extends AbstractTableModel {
    private static final transient String[] COLUMN_NAMES = new String[] {
            L10.get(L10.LEVEL_COLUMN_NAME),
            L10.get(L10.LEVEL_COLUMN_START),
            L10.get(L10.LEVEL_COLUMN_END)
    };

    private static final transient Class[] COLUMN_CLASSES = { String.class, Integer.class, Integer.class };
    private static final transient boolean[] COLUMN_EDITABLE = { false, true, true };

    private LevelList levelList;

    public LevelsTableModel(LevelList levelList) {
        this.levelList = levelList;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public Class<?> getColumnClass(int col) { return COLUMN_CLASSES[col]; }

    @Override
    public int getRowCount() {
        return levelList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Level level = levelList.get(rowIndex);
        return level.getTableModelColumnValueAt(columnIndex);
    }

    public boolean isCellEditable(int row, int col) {
        return COLUMN_EDITABLE[col];
    }

    public void setValueAt(Object value, int row, int col) {
        Level level = levelList.get(row);
        level.setTableModelColumnValueAt(col, value);
    }
}