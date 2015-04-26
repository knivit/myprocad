package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;

import javax.swing.*;
import java.awt.*;

public class TextPropertyViewer extends AbstractPropertyEditor {
    private JPanel editorComponent;
    private JTextField text;

    public TextPropertyViewer() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));

        text = new JTextField();
        editorComponent.add("*", text);

        JButton button = ComponentFactory.Helper.getFactory().createMiniButton();
        editorComponent.add(button);
      //  button.addActionListener(e -> getObjectProperty().getDialogSupport().showDialog());
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }
}
