package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.PercentLayout;
import com.tsoft.myprocad.model.property.ObjectProperty;

import javax.swing.*;
import java.awt.*;

public class DialogButtonsPropertyEditor extends AbstractPropertyEditor {
    private JPanel editorComponent;

    public DialogButtonsPropertyEditor() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
    }

    @Override
    public void addButton(ObjectProperty.Button dialogButton) {
        JButton button = new JButton(dialogButton.name);
        button.setToolTipText(dialogButton.toolTipText);
        editorComponent.add(button);

        button.addActionListener(dialogButton.actionListener);
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }
}
