package com.l2fprod.common.beans.editor;

/**
 * DoublePropertyEditor.<br>
 *
 */
public class DoublePropertyEditor extends NumberPropertyEditor {
    public DoublePropertyEditor() {
        super();
    }

    @Override
    public Class getType() {
        return Double.class;
    }
}
