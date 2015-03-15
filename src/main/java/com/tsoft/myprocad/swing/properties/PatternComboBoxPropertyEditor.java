package com.tsoft.myprocad.swing.properties;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

import java.awt.event.ActionListener;

public class PatternComboBoxPropertyEditor extends ComboBoxPropertyEditor {
    public PatternComboBoxPropertyEditor() {
        super();
        combobox.setRenderer(new PatternComboBoxRenderer());
    }

    @Override
    public void addButton(ActionListener listener) { }
}
