package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.menu.MenuAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermanentLoadTableDialogSupport extends TableDialogSupport {
    private static final String[] names = new String[] { L10.get(L10.CALCULATION_BEAM_PERMANENT_LOAD_TABLE_COLUMN_NAME), L10.get(L10.CALCULATION_BEAM_PERMANENT_LOAD_TABLE_COLUMN_DENSITY), L10.get(L10.CALCULATION_BEAM_PERMANENT_LOAD_TABLE_COLUMN_H) };
    private static final Class[] classes = { String.class, Double.class, Double.class };
    private static final boolean[] editable = { true, true, true };

    public PermanentLoadTableDialogSupport() {
        super(names, classes, editable);
    }

    public void setElements(List<PermanentLoad> list) {
        clear();
        for (PermanentLoad load : list) {
            addElement(load.name, load.density, load.h);
        }
    }

    public List<PermanentLoad> getElements() {
        List<PermanentLoad> list = new ArrayList<>();
        for (Object[] data : this) {
            PermanentLoad load = new PermanentLoad();
            load.name = (String)data[0];
            load.density = (Double)data[1];
            load.h = (Double)data[2];
            list.add(load);
        }
        return list;
    }

    public List<MenuAction> getCustomButtons() {
        MenuAction addDefaults = new MenuAction(Menu.ADD_DEFAULT_LOAD, e -> {
            List<PermanentLoad> list = new ArrayList<>();
            list.add(new PermanentLoad("Настил из половой доски", 600, 0.05));
            list.add(new PermanentLoad("Звукоизоляция из урсы", 15, 0.1));
            list.add(new PermanentLoad("Необрезная доска", 600, 0.019));
            list.add(new PermanentLoad("Гипсокартон", 1000, 0.01));
            setElements(list);
        });

        return Arrays.asList(addDefaults);
    }
}
