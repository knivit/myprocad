package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;

import java.util.ArrayList;
import java.util.List;

public class MomentTableDialogSupport extends TableDialogSupport {
    private static final transient String[] names = new String[] { L10.get(L10.CALCULATION_BEAM_MOMENTS_TABLE_COLUMN_VM), L10.get(L10.CALCULATION_BEAM_MOMENTS_TABLE_COLUMN_ZM) };
    private static final Class[] classes = { Double.class, Double.class };
    private static final boolean[] editable = { true, true };

    public MomentTableDialogSupport() {
        super(names, classes, editable);
    }

    public void setElements(List<Moment> ms) {
        clear();
        for (Moment m : ms) {
            addElement(m.vm, m.zm);
        }
    }

    public List<Moment> getElements() {
        List<Moment> ms = new ArrayList<>();
        for (Object[] data : this) {
            Moment m = new Moment();
            m.setVm(data[0]);
            m.setZm(data[1]);
            ms.add(m);
        }
        return ms;
    }
}
