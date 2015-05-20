package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.converter.ConverterRegistry;
import com.l2fprod.common.util.converter.NumberConverters;

import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public abstract class NumberPropertyEditor extends AbstractPropertyEditor {
    private JFormattedTextField editorComponent;
    private Object lastGoodValue;

    public abstract Class getType();

    public NumberPropertyEditor(NumberFormat format) {
        editorComponent = new JFormattedTextField();

        editorComponent.setBorder(LookAndFeelTweaks.EMPTY_BORDER);
        NumberFormatter formatter = new NumberFormatter(format);
        editorComponent.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    public NumberPropertyEditor() {
        this(NumberConverters.getDefaultFormat(64, 6));
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    private Object parseValue() {
        String text = editorComponent.getText();
        if (text == null || text.trim().length() == 0) {
            return null;
        }

        try {
            return ConverterRegistry.instance().convert(getType(), text);
        } catch (Exception e) {
            UIManager.getLookAndFeel().provideErrorFeedback(editorComponent);
        }

        return null;
    }

    @Override
    public Object getValue() {
        Object value = parseValue();
        if (value != null) lastGoodValue = value;
        return lastGoodValue;
    }

    public void setValue(Object value) {
        if (value == null && getObjectProperty().isNullable()) editorComponent.setText(null);
        else if (value instanceof Number) editorComponent.setText(value.toString());
        lastGoodValue = value;
    }
}