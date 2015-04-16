package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.TextDialog;

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
        Object oldValue = getValue();
        TextDialog dialog = new TextDialog();
        dialog.setText((String)oldValue, true);
        String title = getObjectProperty().getLabelName();
        if (dialog.displayView(title, DialogButton.SAVE, DialogButton.CANCEL) == DialogButton.SAVE) {
            String value = dialog.getText();
            setValue(value);
            firePropertyChange(oldValue, value);
        }
    }
}
