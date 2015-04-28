package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;

import java.util.ArrayList;
import java.util.List;

public class ForceTableDialogSupport extends TableDialogSupport {
    private static final String[] names = new String[]{ L10.get(L10.CALCULATION_BEAM_FORCES_TABLE_COLUMN_VS), L10.get(L10.CALCULATION_BEAM_FORCES_TABLE_COLUMN_ZS) };
    private static final Class[] classes = { Double.class, Double.class };
    private static final boolean[] editable = { true, true };

    public ForceTableDialogSupport() {
        super(names, classes, editable);
    }

    public void setElements(List<Force> fs) {
        clear();
        for (Force f : fs) {
            addElement(f.vs, f.zs);
        }
    }

    public List<Force> getElements() {
        List<Force> fs = new ArrayList<>();
        for (Object[] data : this) {
            Force f = new Force();
            f.setVs(data[0]);
            f.setZs(data[1]);
            fs.add(f);
        }
        return fs;
    }
}
