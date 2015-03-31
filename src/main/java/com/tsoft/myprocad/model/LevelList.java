package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.LevelsTableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class LevelList extends ArrayList<Level> implements TableDialogPanelSupport<Level> {
    private transient Plan plan;
    private transient LevelsTableModel tableModel;

    public LevelList(Plan plan) {
        super();
        this.plan = plan;
    }

    @Override
    public TableDialogPanelSupport<Level> getDeepClone() {
        LevelList copyList = new LevelList(plan);
        for (Level level : this) {
            Level copy = level.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    /** Sort the levels by Z coordinate */
    public void sort() {
        Collections.sort(this);
    }

    public Level findById(long id) {
        Optional<Level> optional = stream().filter(e -> e.getId() == id).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    public Optional<Level> findByName(String levelName) {
        return stream().filter(e -> e.getName().equalsIgnoreCase(levelName)).findFirst();
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new LevelsTableModel(this);
        return tableModel;
    }

    @Override
    public Level addDialog() {
        Level level = new Level();
        level.setId(plan.getProject().generateNextId());
        add(level);
        return level;
    }

    @Override
    public boolean deleteDialog(Level level) {
        if (SwingTools.showConfirmDialog(L10.get(L10.CONFIRM_LEVEL_REMOVAL, level.getName()))) {
            return remove(level);
        }
        return false;
    }
}
