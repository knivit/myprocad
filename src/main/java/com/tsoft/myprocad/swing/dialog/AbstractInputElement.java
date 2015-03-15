package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;

public abstract class AbstractInputElement<T, E extends JComponent> {
    protected String initialValue;

    protected E component;
    protected abstract E createComponent();
    public abstract T getValue();

    public E getComponent() {
        if (component == null) component = createComponent();
        return component;
    }
}
