package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Multiply input elements */
public class InputDialogPanel extends AbstractDialogPanel {
    private JComponent activeComponent;

    public InputDialogPanel(List<AbstractInputElement> inputElements) {
        super(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        boolean first = true;
        for (AbstractInputElement element : inputElements) {
            panel.add(Box.createVerticalStrut(8));
            panel.add(new JLabel(element.getCaption()));
            panel.add(element.getComponent());

            if (first) {
                element.getComponent().requestFocus();
                first = false;
            }
        }

        activeComponent = inputElements.get(0).getComponent();

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public JComponent getActiveComponent() {
        return activeComponent;
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return null;
    }
}
