package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;

import java.util.ArrayList;
import java.util.List;

public class TemporaryLoadTableDialogSupport extends TableDialogSupport {
    private static final String[] names = new String[] { L10.get(L10.CALCULATION_BEAM_TEMPORARY_LOAD_TABLE_COLUMN_NAME), L10.get(L10.CALCULATION_BEAM_TEMPORARY_LOAD_TABLE_COLUMN_VALUE) };
    private static final Class[] classes = { String.class, Double.class };
    private static final boolean[] editable = { true, true };

    public TemporaryLoadTableDialogSupport() {
        super(names, classes, editable);
    }

    public void setElements(List<TemporaryLoad> list) {
        clear();
        for (TemporaryLoad load : list) {
            addElement(load.name, load.value);
        }
    }

    public List<TemporaryLoad> getElements() {
        List<TemporaryLoad> list = new ArrayList<>();
        for (Object[] data : this) {
            TemporaryLoad load = new TemporaryLoad();
            load.name = (String)data[0];
            load.value = (Double)data[1];
            list.add(load);
        }
        return list;
    }

    public void addDefaults() {
        List<TemporaryLoad> list = new ArrayList<>();
        list.add(new TemporaryLoad("Нормативная нагрузка на межэтажное перекрытие", 250));
        list.add(new TemporaryLoad("Нормативная нагрузка от веса перегородок", 75));
        list.add(new TemporaryLoad("Снеговая нагрузка", 100));
        list.add(new TemporaryLoad("Ветровая нагрузка", 20));
        setElements(list);
    }
}