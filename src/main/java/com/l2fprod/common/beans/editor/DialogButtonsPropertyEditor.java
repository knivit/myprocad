package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.PercentLayout;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.menu.MenuAction;

import javax.swing.*;
import java.awt.*;

public class DialogButtonsPropertyEditor extends AbstractPropertyEditor {
    private JPanel editorComponent;

    public DialogButtonsPropertyEditor() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
    }

    @Override
    public void addButton(MenuAction dialogButton) {
        JButton button = dialogButton.getMenu().createMenuButton();
        button.addActionListener(dialogButton);
        editorComponent.add(button);
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }
}
