package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;

import java.util.ArrayList;
import java.util.List;

public class DistributedForceTableDialogSupport extends TableDialogSupport {
    private static final String[] names = new String[] { L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Q1), L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Z1), L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Q2), L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_TABLE_COLUMN_Z2) };
    private static final Class[] classes = { Double.class, Double.class, Double.class, Double.class };
    private static final boolean[] editable = { true, true, true, true };

    public DistributedForceTableDialogSupport() {
        super(names, classes, editable);
    }

    public void setElements(List<DistributedForce> dfs) {
        clear();
        for (DistributedForce df : dfs) {
            addElement(df.q1, df.z1, df.q2, df.z2);
        }
    }

    public List<DistributedForce> getElements() {
        List<DistributedForce> list = new ArrayList<>();
        for (Object[] data : this) {
            DistributedForce df = new DistributedForce();
            df.setQ1(data[0]);
            df.setZ1(data[1]);
            df.setQ2(data[2]);
            df.setZ2(data[3]);
            list.add(df);
        }
        return list;
    }
}
