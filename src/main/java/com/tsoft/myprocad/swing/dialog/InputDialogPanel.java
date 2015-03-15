package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Multiply input elements */
public class InputDialogPanel extends AbstractDialogPanel {
    public InputDialogPanel(List<AbstractInputElement> inputElements) {
        super(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (AbstractInputElement element : inputElements) {
            JComponent component = element.getComponent();
            panel.add(component);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return null;
    }
}
