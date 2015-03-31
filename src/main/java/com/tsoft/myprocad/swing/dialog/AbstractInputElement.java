package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;

public abstract class AbstractInputElement<T, E extends JComponent> {
    private String caption;
    protected String initialValue;

    protected E component;
    protected abstract E createComponent();
    public abstract T getValue();

    public AbstractInputElement(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public E getComponent() {
        if (component == null) component = createComponent();
        return component;
    }
}
