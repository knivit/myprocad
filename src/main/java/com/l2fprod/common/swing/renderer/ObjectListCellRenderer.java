package com.l2fprod.common.swing.renderer;

import java.util.List;

/**
 *  To render lists of objects, i.e. ArrayList<? extends Object>
 */
public class ObjectListCellRenderer extends DefaultCellRenderer {
    @Override
    protected String convertToString(Object value) {
        if (value == null) return null;
        if (!(value instanceof List)) return null;

        return value.toString();
    }
}
