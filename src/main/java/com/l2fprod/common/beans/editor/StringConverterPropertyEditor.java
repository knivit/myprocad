package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.converter.ConverterRegistry;

import java.awt.Component;
import javax.swing.JTextField;

/**
 * StringConverterPropertyEditor. <br>A comma separated list of values.
 */
public abstract class StringConverterPropertyEditor extends AbstractPropertyEditor {
    private JTextField editorComponent;
    private Object oldValue;

    protected abstract Object convertFromString(String text);

    public StringConverterPropertyEditor() {
        editorComponent = new JTextField();
        editorComponent.setBorder(LookAndFeelTweaks.EMPTY_BORDER);
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    @Override
    public Object getValue() {
        String text = editorComponent.getText();
        if (text == null || text.trim().length() == 0) {
            return null;
        }

        try {
            return convertFromString(text.trim());
        } catch (Exception e) {
            return oldValue;
        }
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            editorComponent.setText("");
        } else {
            oldValue = value;
            editorComponent.setText(convertToString(value));
        }
    }

    protected String convertToString(Object value) {
        return (String)ConverterRegistry.instance().convert(String.class, value);
    }
}
