package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.tsoft.myprocad.swing.dialog.AbstractDialogPanel;
import com.tsoft.myprocad.swing.dialog.DialogButton;

import javax.swing.*;
import java.awt.*;

public class TextPropertyViewer extends AbstractPropertyEditor {
    private JPanel editorComponent;
    private JTextField text;
    private String data;

    public TextPropertyViewer() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));

        text = new JTextField();
        editorComponent.add("*", text);

        JButton button = ComponentFactory.Helper.getFactory().createMiniButton();
        editorComponent.add(button);
        button.addActionListener(e -> showTextViewer());
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    @Override
    public Object getValue() {
        return "Mechanics Solution";
    }

    @Override
    public void setValue(Object value) {
        data = (String)value;
    }

    private void showTextViewer() {
        AbstractDialogPanel dialog = getObjectProperty().getDialogPanel();
        dialog.setText(data);
        String title = getObjectProperty().getLabelName();
        dialog.displayView(title, DialogButton.CLOSE);
    }
}
