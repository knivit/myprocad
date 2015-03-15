package com.l2fprod.common.beans.editor;

public class ShortPropertyEditor extends NumberPropertyEditor {
    public ShortPropertyEditor() {
        super();
    }

    @Override
    public Class getType() {
        return Short.class;
    }
}
