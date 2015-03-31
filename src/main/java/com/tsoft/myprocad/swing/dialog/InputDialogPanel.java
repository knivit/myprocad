package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Multiply input elements */
public class InputDialogPanel extends AbstractDialogPanel {
    public InputDialogPanel(List<AbstractInputElement> inputElements) {
        super(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        for (AbstractInputElement element : inputElements) {
            panel.add(Box.createVerticalStrut(8));
            panel.add(new JLabel(element.getCaption()));
            panel.add(element.getComponent());
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return null;
    }
}
