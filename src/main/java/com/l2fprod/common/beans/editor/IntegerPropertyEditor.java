package com.l2fprod.common.beans.editor;

public class IntegerPropertyEditor extends NumberPropertyEditor {
    public IntegerPropertyEditor() {
        super();
    }

    @Override
    public Class getType() {
        return Integer.class;
    }
}
