package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;

public class InputTextElement extends AbstractInputElement<String, JTextField> {
    public InputTextElement(String initialValue) {
        this.initialValue = initialValue;
    }

    protected JTextField createComponent() {
        return new JTextField(initialValue);
    }

    public String getValue() {
        return component.getText();
    }
}
