package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.LookAndFeelTweaks;

import java.awt.Component;
import javax.swing.JTextField;

public class StringPropertyEditor extends AbstractPropertyEditor {
    private JTextField editorComponent;

    public StringPropertyEditor() {
        editorComponent = new JTextField();
        editorComponent.setBorder(LookAndFeelTweaks.EMPTY_BORDER);
    }

    public Object getValue() {
        return editorComponent.getText();
    }

    public void setValue(Object value) {
        if (value == null) {
            editorComponent.setText("");
        } else {
            editorComponent.setText(String.valueOf(value));
        }
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }
}
