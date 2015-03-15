package com.l2fprod.common.beans.editor;

/**
 * BooleanPropertyEditor.<br>
 *
 */
public class BooleanPropertyEditor extends ComboBoxPropertyEditor {
    public BooleanPropertyEditor() {
        super();
        setAvailableValues(new Object[] { Boolean.TRUE, Boolean.FALSE });
    }
}
