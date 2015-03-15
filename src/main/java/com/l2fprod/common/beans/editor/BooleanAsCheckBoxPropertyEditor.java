package com.l2fprod.common.beans.editor;

import java.awt.Component;

import javax.swing.JCheckBox;

public class BooleanAsCheckBoxPropertyEditor extends AbstractPropertyEditor {
    private JCheckBox editorComponent;

    public BooleanAsCheckBoxPropertyEditor() {
        editorComponent = new JCheckBox();
        editorComponent.setOpaque(false);
        editorComponent.addActionListener(e -> {
            boolean value = editorComponent.isSelected() ? Boolean.TRUE : Boolean.FALSE;
            firePropertyChange(!value, value);
            editorComponent.transferFocus();
        });
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    public Object getValue() {
        return editorComponent.isSelected() ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setValue(Object value) {
        editorComponent.setSelected(Boolean.TRUE.equals(value));
    }

}
