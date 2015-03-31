package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;

public class InputTextElement extends AbstractInputElement<String, JTextField> {
    public InputTextElement(String caption, String initialValue) {
        super(caption);
        this.initialValue = initialValue;
    }

    @Override
    protected JTextField createComponent() {
        return new JTextField(initialValue);
    }

    @Override
    public String getValue() {
        return component.getText();
    }
}
